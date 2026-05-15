package com.example.beneficio.rest;

import com.example.beneficio.dto.DetalleCuentaDTO;
import com.example.beneficio.repository.DetalleCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-cuenta")
@CrossOrigin(origins = "*")
public class DetalleCuentaREST {

    @Autowired
    private DetalleCuentaRepository detalleCuentaRepository;

    @GetMapping("/listar/{nocuenta}")
    public ResponseEntity<List<DetalleCuentaDTO>> listarParcialidades(@PathVariable String nocuenta) {
        try {
            // Spring ejecuta la proyección basándose en los métodos Get de la interfaz
            List<DetalleCuentaDTO> lista = detalleCuentaRepository.findByNocuenta(nocuenta);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace(); // Te ayudará a ver si hay algún problema de nombres en consola
            return ResponseEntity.internalServerError().build();
        }
    }
}