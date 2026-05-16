package com.example.beneficio.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.beneficio.dto.NotificacionEstadoDTO;
import org.springframework.web.client.RestTemplate;
import com.example.beneficio.dto.CambiarEstadoDTO;
import com.example.beneficio.dto.CuentaDetalleDTO;
import com.example.beneficio.dto.EstadoCatalogoDTO;
import com.example.beneficio.model.Cuenta;
import com.example.beneficio.repository.CuentasRepository;
import com.example.beneficio.service.CuentaService; // Importamos el nuevo servicio

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*") // Permite peticiones globales de forma segura para desarrollo
public class CuentaREST {

    @Autowired
    private CuentasRepository cuentasRepository;

    @Autowired
    private CuentaService cuentaService; // Inyectamos el servicio con las reglas de negocio

    // =========================================================================
    //
    // =========================================================================
    @GetMapping("/listar")
    @CrossOrigin(origins = "http://localhost:4600")
    public ResponseEntity<?> listarCuentas() {
        try {
            // Llama a la consulta nativa que une Cuentas, Agricultores y Catálogos
            List<CuentaDetalleDTO> lista = cuentasRepository.findAllDetalles();

            if (lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            // Un toque de depuración por si el SQL falla por nombres de columnas
            return ResponseEntity.internalServerError()
                    .body("Error al obtener el listado: " + e.getMessage());
        }
    }

    // =========================================================================
    // NUEVOS ENDPOINTS: REGLAS DE NEGOCIO PARA EL BENEFICIO
    // =========================================================================


    /*
    @PutMapping("/{noCuenta}/cambiar-estado")
    public ResponseEntity<?> cambiarEstadoCuenta(@PathVariable String noCuenta, @RequestBody CambiarEstadoDTO dto) {
        try {
            Cuenta cuentaActualizada = cuentaService.cambiarEstadoCiclo(
                    noCuenta,
                    dto.getIdEstadoPesaje(),
                    dto.getEstadoNombre(),
                    dto.getPesoCabalTotal()
            );
            return ResponseEntity.ok(cuentaActualizada);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
*/

    @PutMapping("/{noCuenta}/cambiar-estado")
    public ResponseEntity<?> cambiarEstadoCuenta(@PathVariable String noCuenta, @RequestBody CambiarEstadoDTO dto) {
        try {
            // 1. Primero se realiza la actualización y validación local en el Beneficio
            Cuenta cuentaActualizada = cuentaService.cambiarEstadoCiclo(
                    noCuenta,
                    dto.getIdEstadoPesaje(),
                    dto.getEstadoNombre(),
                    dto.getPesoCabalTotal()
            );

            // 2. CONVERSIÓN Y ENVÍO AL OTRO SISTEMA
            // Creamos el objeto con el 'noCuenta' y la descripción del estado ('estadoNombre' / 'detallecatalogo')
            NotificacionEstadoDTO notificacion = new NotificacionEstadoDTO(noCuenta, dto.getEstadoNombre());

            // Instanciamos RestTemplate para efectuar la petición externa
            RestTemplate restTemplate = new RestTemplate();

            // 🚨 REEMPLAZA ESTA URL por la dirección y endpoint real del otro sistema (ej: Módulo Agricultor)
            String urlOtroSistema = "http://localhost:8081/api/externo/actualizar-estado";

            try {
                // Enviamos el objeto; Spring Boot lo convierte automáticamente a JSON en el Body
                restTemplate.postForEntity(urlOtroSistema, notificacion, String.class);
                System.out.println("Sincronización exitosa enviada al otro sistema para la cuenta: " + noCuenta);
            } catch (Exception ex) {
                // Logeamos el error si el otro sistema está apagado, para que no bote la operación del Beneficio
                System.err.println("No se pudo sincronizar con el otro sistema, pero la cuenta local fue guardada. Error: " + ex.getMessage());
            }

            // 3. Retornamos la respuesta exitosa al frontend (Angular)
            return ResponseEntity.ok(cuentaActualizada);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/parcialidades/{idDetalleCuenta}/validar-recepcion")
    public ResponseEntity<?> validarRecepcionParcialidad(
            @PathVariable Long idDetalleCuenta,
            @RequestBody Map<String, Object> payload) {
        try {
            boolean choferActivo = (boolean) payload.get("choferActivo");
            boolean camionActivo = (boolean) payload.get("camionActivo");
            boolean licenciaVigente = (boolean) payload.get("licenciaVigente");

            // Si los datos del chofer y camión coinciden, están activos y la licencia vigente
            if (choferActivo && camionActivo && licenciaVigente) {
                return ResponseEntity.ok(Map.of(
                        "status", "CONFIRMADO",
                        "mensaje", "Se confirma la recepción de la parcialidad"
                ));
            } else {
                // Si el transporte/transportista están inactivos, o la licencia vencida
                return ResponseEntity.ok(Map.of(
                        "status", "RECHAZADO",
                        "mensaje", "Se rechaza la parcialidad"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensaje", "Error al procesar la validación: " + e.getMessage()));
        }
    }

    @GetMapping("/estados-catalogo")
    public ResponseEntity<List<EstadoCatalogoDTO>> getEstadosCatalogo() {
        List<EstadoCatalogoDTO> estados = cuentaService.obtenerEstadosCatalogo();
        return ResponseEntity.ok(estados);
    }
}