package com.example.beneficio.rest;

import com.example.beneficio.dto.EstadoCatalogoDTO;
import com.example.beneficio.model.Catalogo;
import com.example.beneficio.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = "*")
public class CatalogoREST {

    @Autowired
    private CatalogoRepository repository;

    @GetMapping("/estados")
    public ResponseEntity<List<Catalogo>> listarEstados() {
        return ResponseEntity.ok(repository.findEstadosTransporte());
    }


}