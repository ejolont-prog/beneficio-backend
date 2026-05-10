package com.example.beneficio.rest;

import com.example.beneficio.dto.RespuestaBeneficioDTO;
import com.example.beneficio.model.Catalogo;
import com.example.beneficio.model.Cuenta;
import com.example.beneficio.repository.CatalogoRepository;
import com.example.beneficio.repository.CuentasRepository;
import com.example.beneficio.dto.PesajeExternoDTO; // <--- Importa el de este proyecto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/recepcion-pesaje")
public class RecepcionPesajeREST {

    @Autowired
    private CuentasRepository cuentasRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;
    /*
    @PostMapping("/guardar-externo")
    public ResponseEntity<?> recibirDesdeAgricultor(@RequestBody PesajeExternoDTO dto) {
        try {
            Cuenta nuevaCuenta = new Cuenta();

            // 1. Generar el correlativo para nocuenta
            Integer ultimoCorrelativo = cuentasRepository.findMaxNoCuenta();
            int siguiente = (ultimoCorrelativo != null) ? ultimoCorrelativo + 1 : 1;
            String nuevoNoCuenta = String.format("%05d", siguiente);

            // 2. Mapear datos del JSON
            nuevaCuenta.setNitAgricultor(dto.getNitagricultor());
            nuevaCuenta.setPesoTotalEsperado(dto.getPesototalesperado());
            nuevaCuenta.setIdPesajeExterno(dto.getIdPesaje());

            // 3. Datos automáticos
            nuevaCuenta.setNoCuenta(nuevoNoCuenta);
            nuevaCuenta.setFechaCreacion(LocalDateTime.now());
            nuevaCuenta.setEliminado(false);
            nuevaCuenta.setBanco("SISTEMA AGRICULTOR");
            nuevaCuenta.setTipocuenta("AUTOMÁTICO");
            nuevaCuenta.setEstado(1);

            cuentasRepository.save(nuevaCuenta);

            // RESPUESTA: Devolvemos el objeto con el nuevo número de cuenta y el ID original
            return ResponseEntity.ok(new RespuestaBeneficioDTO(nuevoNoCuenta, dto.getIdPesaje()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

     */

    @PostMapping("/guardar-externo")
    public ResponseEntity<?> recibirDesdeAgricultor(@RequestBody PesajeExternoDTO dto) {
        try {
            Cuenta nuevaCuenta = new Cuenta();

            // 1. Generar el correlativo para nocuenta
            Integer ultimoCorrelativo = cuentasRepository.findMaxNoCuenta();
            int siguiente = (ultimoCorrelativo != null) ? ultimoCorrelativo + 1 : 1;
            String nuevoNoCuenta = String.format("%05d", siguiente);

            // 2. Mapear datos básicos del DTO
            nuevaCuenta.setNitAgricultor(dto.getNitagricultor());
            nuevaCuenta.setPesoTotalEsperado(dto.getPesototalesperado());
            nuevaCuenta.setIdPesajeExterno(dto.getIdPesaje());

            // 3. Lógica para Unidad de Peso (Comparación de String con BD)
            // Buscamos el catálogo donde 'detallecatalogo' sea igual a 'Quintal Maduro'
            Optional<Catalogo> catUnidad = catalogoRepository.findByNombre(dto.getUnidadpeso());

            if (catUnidad.isPresent()) {
                nuevaCuenta.setIdUnidadPeso(catUnidad.get().getId());
            } else {
                return ResponseEntity.badRequest()
                        .body("Error: La unidad de peso '" + dto.getUnidadpeso() + "' no existe en los catálogos.");
            }

            // 4. Asignación de valores por defecto y automáticos
            nuevaCuenta.setNoCuenta(nuevoNoCuenta);
            nuevaCuenta.setFechaCreacion(LocalDateTime.now());
            nuevaCuenta.setEliminado(false);
            nuevaCuenta.setBanco("SISTEMA AGRICULTOR");
            nuevaCuenta.setTipocuenta("AUTOMÁTICO");

            // Campos específicos solicitados:
            nuevaCuenta.setEstado(1);           // estado = 1
            nuevaCuenta.setIdEstadoPesaje(27);   // estadopesaje = 27
            nuevaCuenta.setCreadoPor(1);         // creado por = 1
            nuevaCuenta.setModificadoPor(1);     // modificado por = 1

            cuentasRepository.save(nuevaCuenta);

            return ResponseEntity.ok(new RespuestaBeneficioDTO(nuevoNoCuenta, dto.getIdPesaje()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}