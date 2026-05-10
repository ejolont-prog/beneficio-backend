package com.example.beneficio.rest;

import java.util.List;
import com.example.beneficio.dto.CuentaDetalleDTO;
import com.example.beneficio.repository.CuentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/cuentas")
public class CuentaREST {

    @Autowired
    private CuentasRepository cuentasRepository;

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
}