package com.example.beneficio.rest;

import com.example.beneficio.dto.TransportistaRequestDTO;
import com.example.beneficio.dto.UpdateEstadoTransportistaDTO;
import com.example.beneficio.service.TransportistaBeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transportistas-beneficio")
public class TransportistaBeneficioREST {
    @Autowired
    private TransportistaBeneficioService service;

    @PostMapping("/validar-y-crear")
    public ResponseEntity<?> validarYCrear(@RequestBody TransportistaRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.procesarRegistro(dto));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/todo-detalle")
    public ResponseEntity<?> listarTodoDetalle() {

        try {

            return ResponseEntity.ok(service.listarTodoDetalle());

        } catch (Exception e) {

            return ResponseEntity.status(500).body(e.getMessage());

        }
    }

    @PutMapping("/actualizar-estado-completo")
    public ResponseEntity<?> actualizarEstado(
            @RequestBody UpdateEstadoTransportistaDTO dto,
            @RequestHeader("Authorization") String token
    ) {

        try {

            service.actualizarEstadoSincronizado(dto, token);

            return ResponseEntity.ok(
                    Map.of("message", "Estado actualizado correctamente")
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }
}