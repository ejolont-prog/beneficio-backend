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

    // Instanciamos RestTemplate para hacer peticiones HTTP directas a otros microservicios
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void actualizarEstadoParcialidad(Long idDetalle, Integer nuevoEstado) {

        String sqlUpdate = "UPDATE beneficio.detallecuenta SET estadopesaje = ? WHERE iddetallecuenta = ?";
        jdbcTemplate.update(sqlUpdate, nuevoEstado, idDetalle);
    }

    public RespuestaValidacionDTO validarChoferYCamion(Long idDetalle, String placa, String cui) {
        String noParcialidad = "";
        try {
            // 0. Obtener el número de parcialidad de esta fila antes de procesar para la notificación posterior
            String sqlInfo = "SELECT noparcialidad FROM beneficio.detallecuenta WHERE iddetallecuenta = ? LIMIT 1";
            noParcialidad = jdbcTemplate.queryForObject(sqlInfo, String.class, idDetalle);

            // 1. Validar Estado del Vehículo
            String sqlVehiculo = "SELECT estado FROM beneficio.transportes WHERE placa = ? LIMIT 1";
            Integer estadoVehiculo = jdbcTemplate.queryForObject(sqlVehiculo, Integer.class, placa);

            // 2. Validar Estado del Piloto
            String sqlPiloto = "SELECT estado FROM beneficio.transportistas WHERE cui = ? LIMIT 1";
            Integer estadoPiloto = jdbcTemplate.queryForObject(sqlPiloto, Integer.class, cui);

            if (estadoVehiculo != null && estadoVehiculo == 7 && estadoPiloto != null && estadoPiloto == 7) {
                // ÉXITO LOCAL: Pasa al ID 21
                actualizarEstadoParcialidad(idDetalle, 21);

                // 🌐 SINCRONIZAR CON AGRICULTOR (ACEPTADO)
                notificarAlAgricultor(noParcialidad, "ACEPTADO");

                return new RespuestaValidacionDTO(true, "Se confirma la recepción de la parcialidad. Sincronizado con Agricultor.", 21);
            } else {
                // RECHAZO LOCAL: Pasa al ID 68
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
            // Si el error ocurrió después de conseguir el noparcialidad, intentamos notificar el rechazo forzado
            if (noParcialidad != null && !noParcialidad.isEmpty()) {
                notificarAlAgricultor(noParcialidad, "RECHAZADO");
            }
            return new RespuestaValidacionDTO(false, "Recepción Rechazada: Placa o CUI inválidos. Sincronizado con Agricultor.", 68);
        }
    }

    /**
     * Envía la notificación en formato JSON al API de Agricultor de forma síncrona
     */
    private void notificarAlAgricultor(String noParcialidad, String resultado) {
        try {
            String urlAgricultor = "http://localhost:8081/api/externo/actualizar-parcialidad";

            // Creamos el mapa que Jackson convertirá automáticamente en el JSON esperado
            Map<String, String> bodyJson = new HashMap<>();
            bodyJson.put("noparcialidad", noParcialidad);
            bodyJson.put("resultado", resultado);

            // Ejecutamos la petición PUT hacia el Agricultor
            restTemplate.put(urlAgricultor, bodyJson);
            System.out.println(" Sincronización exitosa enviada a Agricultor para la parcialidad: " + noParcialidad);

        } catch (Exception e) {
            // Imprimimos el error por consola para no tumbar la transacción del beneficio si el servidor de Agricultor está apagado
            System.err.println("❌ No se pudo sincronizar el estado con el servidor de Agricultor: " + e.getMessage());
        }
    }
}