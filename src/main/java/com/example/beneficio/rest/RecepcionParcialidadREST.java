package com.example.beneficio.rest;

import com.example.beneficio.dto.ParcialidadEntranteDTO;
import com.example.beneficio.service.DetalleCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recepcion-parcialidad") // Esta ruta debe estar permitida en SecurityConfig o validada por el API Key Filter
public class RecepcionParcialidadREST {

    @Autowired
    private DetalleCuentaService detalleCuentaService;

    @PostMapping("/guardar-externo")
    @CrossOrigin(origins = "*") // Permitir que el Agricultor mande datos
    public ResponseEntity<?> recibirParcialidad(@RequestBody ParcialidadEntranteDTO dto) {
        try {
            detalleCuentaService.procesarRecepcionParcialidad(dto);
            return ResponseEntity.ok().body("{\"mensaje\": \"Parcialidad guardada en Beneficio\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}