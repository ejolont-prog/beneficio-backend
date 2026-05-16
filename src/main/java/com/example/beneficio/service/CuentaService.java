package com.example.beneficio.service;

import com.example.beneficio.dto.EstadoCatalogoDTO;
import com.example.beneficio.model.Cuenta;
import com.example.beneficio.repository.CuentasRepository;
import com.example.beneficio.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    @Autowired
    private CuentasRepository cuentasRepository;
    @Autowired
    private CatalogoRepository catalogoRepository;
    /**
     * ACCIÓN: Cambiar Estado (A nivel de Cuenta) con Regla de Restricción y cálculo de Tolerancia.
     */
    @Transactional
    public Cuenta cambiarEstadoCiclo(String noCuenta, Integer nuevoEstadoId, String nuevoEstadoNombre, BigDecimal pesoCabalTotal) {

        // 1. Buscar la cuenta por su número
        Cuenta cuenta = cuentasRepository.findByNoCuenta(noCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con el número: " + noCuenta));

        // 2. Obtener el nombre del estado actual desde el catálogo para aplicar la Regla de Restricción
        String estadoActual = cuentasRepository.findEstadoNombreByNoCuenta(noCuenta);
        if (estadoActual == null) {
            throw new IllegalArgumentException("La cuenta no posee un estado rastreable en el catálogo.");
        }

        // REGLA DE RESTRICCIÓN: Solo se permite si está en "Pesaje finalizado" o "Cuenta cerrada"
        String estadoClean = estadoActual.toLowerCase().trim();
        if (!estadoClean.contains("pesaje finalizado") && !estadoClean.contains("cuenta cerrada")) {
            throw new IllegalStateException("Error: Solo se permite cambiar el estado si la cuenta está originalmente en 'Pesaje finalizado' o 'Cuenta cerrada'. Estado actual: " + estadoActual);
        }

        // 3. Aplicar el nuevo estado a la columna funcional que ya usas para los JOINS
        cuenta.setIdEstadoPesaje(nuevoEstadoId);
        cuenta.setFechaModificacion(LocalDateTime.now());

        // 4. Si el beneficio la cambia a "Cuenta confirmada", calculamos la tolerancia
        if (nuevoEstadoNombre.toLowerCase().trim().contains("cuenta confirmada")) {
            BigDecimal pesoEsperado = cuenta.getPesoTotalEsperado(); // Peso del Agricultor

            if (pesoEsperado == null || pesoEsperado.compareTo(BigDecimal.ZERO) == 0) {
                throw new IllegalArgumentException("El peso esperado del agricultor no puede ser cero o nulo.");
            }

            // Guardamos el peso real total de la báscula de Peso Cabal
            cuenta.setPesoTotalRecibido(pesoCabalTotal);

            // diferenciatotal = pesototalrecibido - pesototalesperado
            BigDecimal diferencia = pesoCabalTotal.subtract(pesoEsperado);
            cuenta.setDiferenciaTotal(diferencia);

            // tolerancia = (diferencia / pesoEsperado) * 100
            BigDecimal variacionPorcentaje = diferencia
                    .multiply(new BigDecimal("100"))
                    .divide(pesoEsperado, 2, RoundingMode.HALF_UP);
            cuenta.setTolerancia(variacionPorcentaje);

            // Definición de límites del +/- 5%
            BigDecimal limiteSuperior = new BigDecimal("5.00");
            BigDecimal limiteInferior = new BigDecimal("-5.00");

            // Añadir la etiqueta automática a 'resultadotolerancia'
            if (variacionPorcentaje.compareTo(limiteInferior) >= 0 && variacionPorcentaje.compareTo(limiteSuperior) <= 0) {
                cuenta.setResultadoTolerancia("Aceptado, en parámetro");
            } else if (variacionPorcentaje.compareTo(limiteSuperior) > 0) {
                cuenta.setResultadoTolerancia("Sobrante");
            } else {
                cuenta.setResultadoTolerancia("Faltante");
            }
        }

        return cuentasRepository.save(cuenta);
    }

    public List<EstadoCatalogoDTO> obtenerEstadosCatalogo() {
        // 1. Traemos las filas crudas desde el repositorio
        List<Object[]> resultados = catalogoRepository.findEstadosCuentaCatalogo();

        // 2. Mapeamos cada fila directamente al constructor de tu DTO
        return resultados.stream()
                .map(row -> new EstadoCatalogoDTO(
                        ((Number) row[0]).intValue(), // Convierte el ID a Integer de forma segura
                        (String) row[1]               // Convierte el detallecatalogo a String
                ))
                .collect(Collectors.toList());
    }
}