package com.example.beneficio.rest;

import com.example.beneficio.dto.ParcialidadEntranteDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/beneficio")
@CrossOrigin(origins = "http://localhost:4200")
// Solo para pruebas, si no usas Spring Security completo

public class RecepcionREST {

    @Autowired
    private JdbcTemplate jdbcTemplate; // Este debe estar configurado a tu DB de Beneficio

    @PostMapping("/consulta-qr")
    public ResponseEntity<?> consultarEnBeneficio(@RequestBody ParcialidadEntranteDTO dto) {

        // SQL directo a las tablas de beneficio
        String sql = "SELECT dc.noparcialidad AS no_parcialidad, " +
                "dc.fecharecepcion AS fecha_recepcion, " + // <-- Agregamos esta columna
                "t.placa AS transporte_placa, ct.detallecatalogo AS transporte_estado, " +
                "t.observaciones AS transporte_observaciones, tr.cui AS transportista_cui, " +
                "tr.nombrecompleto AS transportista_nombre, ctr.detallecatalogo AS transportista_estado, " +
                "tr.observaciones AS transportista_observaciones " +
                "FROM beneficio.detallecuenta dc " +
                "INNER JOIN beneficio.transportes t ON dc.placa = t.placa " +
                "INNER JOIN beneficio.transportistas tr ON dc.cuitransportista = tr.cui " +
                "LEFT JOIN beneficio.catalogos ct ON CAST(t.estado AS INTEGER) = ct.id " +
                "LEFT JOIN beneficio.catalogos ctr ON tr.estado = ctr.id " +
                "WHERE dc.noparcialidad = ? " +
                "AND dc.nocuenta = ? " +
                "AND dc.placa = ? " +
                "AND dc.eliminado = false";

        try {
            // Se usan exclusivamente los datos que vienen en el QR para buscar en beneficio
            Map<String, Object> resultado = jdbcTemplate.queryForMap(sql,
                    dto.getIdParcialidad(),
                    dto.getNoCuenta(),
                    dto.getTransporte());

            return ResponseEntity.ok(resultado);
        } catch (EmptyResultDataAccessException e) {
            // Si no existe en beneficio, devolvemos un error claro
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: La parcialidad no existe en los registros de Beneficio.");
        }
    }

    @PostMapping("/movimiento-talanquera")
    @Transactional
    public ResponseEntity<?> registrarMovimiento(@RequestBody Map<String, Object> payload) {
        Integer noParcialidad = (Integer) payload.get("noParcialidad");
        Integer idUsuario = (Integer) payload.get("idUsuario");

        try {
            // 1. "Le decimos" a la base de datos quién es el usuario de esta conexión
            jdbcTemplate.execute("SET LOCAL app.current_user_id = '" + idUsuario + "'");

            // 2. Consultar estado actual
            String sqlSelect = "SELECT ubicacion FROM beneficio.detallecuenta WHERE noparcialidad = ?";
            Boolean ubicacionActual = jdbcTemplate.queryForObject(sqlSelect, Boolean.class, noParcialidad);

            Boolean nuevaUbicacion = (ubicacionActual == null) ? true : !ubicacionActual;

            // 3. Ejecutar el UPDATE (El trigger ahora leerá el app.current_user_id)
            String sqlUpdate = "UPDATE beneficio.detallecuenta SET ubicacion = ? WHERE noparcialidad = ?";
            jdbcTemplate.update(sqlUpdate, nuevaUbicacion, noParcialidad);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "nuevaUbicacion", nuevaUbicacion,
                    "mensaje", nuevaUbicacion ? "Ingreso registrado" : "Salida registrada"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}