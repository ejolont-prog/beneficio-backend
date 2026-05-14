package com.example.beneficio.rest;

import com.example.beneficio.model.Agricultor;
import com.example.beneficio.repository.AgricultorRepository;
import com.example.beneficio.repository.CuentasRepository;
import com.example.beneficio.repository.TransporteBeneficioRepository;
import com.example.beneficio.repository.TransportistaBeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agricultores")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class AgricultorREST {

    @Autowired
    private AgricultorRepository agricultorRepository;

    // AÑADE ESTAS LÍNEAS (Asegúrate de que los nombres de las clases coincidan con tus archivos .java)
    @Autowired
    private CuentasRepository cuentasRepository;

    @Autowired
    private TransporteBeneficioRepository transportesRepository;

    @Autowired
    private TransportistaBeneficioRepository transportistasRepository;

    @GetMapping("/listar")
    public List<Agricultor> getAll() {
        return agricultorRepository.findAll();
    }

    @GetMapping("/conteos/{nit}")
    public Map<String, Long> obtenerConteos(@PathVariable String nit) {
        Map<String, Long> conteos = new HashMap<>();
        conteos.put("cuentas", cuentasRepository.countByNitAgricultorNative(nit));
        conteos.put("transportes", transportesRepository.countByAgricultorNative(nit));
        conteos.put("transportistas", transportistasRepository.countByAgricultorNative(nit));
        return conteos;
    }
}