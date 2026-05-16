package com.example.beneficio.service;

import com.example.beneficio.dto.RespuestaValidacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidacionEnvioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void actualizarEstadoParcialidad(Long idDetalle, Integer nuevoEstado) {
        String sqlUpdate = "UPDATE beneficio.detallecuenta SET estadopesaje = ? WHERE iddetallecuenta = ?";
        jdbcTemplate.update(sqlUpdate, nuevoEstado, idDetalle);
    }

    // 🚨 NUEVO MÉTODO: Actualiza el estado de la cuenta principal al ID 29
    @Transactional
    public void actualizarEstadoCuenta(String noCuenta, Integer nuevoEstado) {
        // NOTA: Ajusta "beneficio.cuentas" y "nocuenta" según se llamen exactamente en tu BD de Beneficio
        String sqlUpdateCuenta = "UPDATE beneficio.cuentas SET estadopesaje = ? WHERE nocuenta = ?";
        jdbcTemplate.update(sqlUpdateCuenta, nuevoEstado, noCuenta);
        System.out.println("🔄 Cuenta " + noCuenta + " actualizada al estado ID: " + nuevoEstado);
    }

    public RespuestaValidacionDTO validarChoferYCamion(Long idDetalle, String placa, String cui) {
        String noParcialidad = "";
        String noCuenta = ""; // 👈 Variable para almacenar el número de cuenta asociado

        try {
            // 0. Obtener el número de parcialidad Y el número de cuenta de esta fila antes de procesar
            // 🚨 NOTA: Asegúrate de que la columna se llame "nocuenta" en beneficio.detallecuenta
            String sqlInfo = "SELECT noparcialidad, nocuenta FROM beneficio.detallecuenta WHERE iddetallecuenta = ? LIMIT 1";

            Map<String, Object> datosDetalle = jdbcTemplate.queryForMap(sqlInfo, idDetalle);
            noParcialidad = String.valueOf(datosDetalle.get("noparcialidad"));
            noCuenta = String.valueOf(datosDetalle.get("nocuenta"));

            // 1. Validar Estado del Vehículo
            String sqlVehiculo = "SELECT estado FROM beneficio.transportes WHERE placa = ? LIMIT 1";
            Integer estadoVehiculo = jdbcTemplate.queryForObject(sqlVehiculo, Integer.class, placa);

            // 2. Validar Estado del Piloto
            String sqlPiloto = "SELECT estado FROM beneficio.transportistas WHERE cui = ? LIMIT 1";
            Integer estadoPiloto = jdbcTemplate.queryForObject(sqlPiloto, Integer.class, cui);

            if (estadoVehiculo != null && estadoVehiculo == 7 && estadoPiloto != null && estadoPiloto == 7) {
                // ÉXITO LOCAL: Parcialidad pasa al ID 21
                actualizarEstadoParcialidad(idDetalle, 21);

                // 🚨 ¡CUMPLIENDO REQUERIMIENTO!: Si todo está OK, la cuenta pasa al ID 29
                if (noCuenta != null && !noCuenta.isEmpty() && !"null".equals(noCuenta)) {
                    actualizarEstadoCuenta(noCuenta, 29);
                }

                // 🌐 SINCRONIZAR CON AGRICULTOR (ACEPTADO)
                notificarAlAgricultor(noParcialidad, "ACEPTADO");

                return new RespuestaValidacionDTO(true, "Se confirma la recepción de la parcialidad. Cuenta y Agricultor sincronizados.", 21);
            } else {
                // RECHAZO LOCAL: Pasa al ID 68 (Aquí no tocamos la cuenta general, solo rechazamos este viaje)
                actualizarEstadoParcialidad(idDetalle, 68);

                String motivo = "";
                if (estadoVehiculo != 7) motivo += "[Vehículo INACTIVO] ";
                if (estadoPiloto != 7) motivo += "[Piloto INACTIVO]";

                // 🌐 SINCRONIZAR CON AGRICULTOR (RECHAZADO)
                notificarAlAgricultor(noParcialidad, "RECHAZADO");

                return new RespuestaValidacionDTO(false, "Recepción Rechazada: " + motivo + ". Sincronizado con Agricultor.", 68);
            }

        } catch (Exception e) {
            actualizarEstadoParcialidad(idDetalle, 68);
            if (noParcialidad != null && !noParcialidad.isEmpty()) {
                notificarAlAgricultor(noParcialidad, "RECHAZADO");
            }
            return new RespuestaValidacionDTO(false, "Recepción Rechazada: Error interno o datos inválidos. " + e.getMessage(), 68);
        }
    }

    /**
     * Envía la notificación en formato JSON al API de Agricultor de forma síncrona
     */
    private void notificarAlAgricultor(String noParcialidad, String resultado) {
        try {
            String urlAgricultor = "http://localhost:8081/api/externo/actualizar-parcialidad";

            Map<String, String> bodyJson = new HashMap<>();
            bodyJson.put("noparcialidad", noParcialidad);
            bodyJson.put("resultado", resultado);

            restTemplate.put(urlAgricultor, bodyJson);
            System.out.println(" Sincronización exitosa enviada a Agricultor para la parcialidad: " + noParcialidad);

        } catch (Exception e) {
            System.err.println("❌ No se pudo sincronizar el estado con el servidor de Agricultor: " + e.getMessage());
        }
    }
}