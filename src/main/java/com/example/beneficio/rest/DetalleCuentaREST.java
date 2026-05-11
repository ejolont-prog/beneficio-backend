package com.example.beneficio.rest;

import com.example.beneficio.model.DetalleCuenta;
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
    public ResponseEntity<List<DetalleCuenta>> listarParcialidades(@PathVariable String nocuenta) {
        try {
            List<DetalleCuenta> lista = detalleCuentaRepository.findByNocuenta(nocuenta);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}