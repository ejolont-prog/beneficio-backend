package com.example.beneficio.rest;

import com.example.beneficio.dto.UsuarioSincronizacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sincronizar")
@CrossOrigin(origins = "*")
public class UsuarioBeneficioREST {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/crear-desde-agricultor")
    public ResponseEntity<?> crearDesdeAgricultor(@RequestBody UsuarioSincronizacionDTO dto) {
        try {
            // 1. VALIDACIÓN: Verificar si el correo ya existe en el ecosistema (Ej: Tabla de usuarios o agricultores si tuviera)
            // Como beneficio.agricultores no guarda correo directo, lo validamos en la tabla de control de usuarios centralizada
            String sqlCheckCorreo = "SELECT COUNT(*) FROM agricultor.usuario WHERE correo = ?";
            Integer count = jdbcTemplate.queryForObject(sqlCheckCorreo, Integer.class, dto.getCorreo());

            if (count != null && count > 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El correo electrónico ya se encuentra registrado actualmente."));
            }

            // 2. INSERT NATIVO EN BENEFICIO.AGRICULTORES
            // razonsocial y nombrecomercial guardan el nombre de usuario. creadopor = 1 por defecto. eliminado = false por defecto.
            String sqlInsert = "INSERT INTO beneficio.agricultores " +
                    "(nit, razonsocial, nombrecomercial, creadopor, eliminado) " +
                    "VALUES (?, ?, ?, 1, false)";

            jdbcTemplate.update(sqlInsert,
                    dto.getNit(),
                    dto.getUsuario(),
                    dto.getUsuario()
            );

            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Agricultor registrado con éxito en Beneficio."));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error en Beneficio: " + e.getMessage()));
        }
    }
}