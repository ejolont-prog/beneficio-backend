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


    @Autowired
    private com.example.beneficio.service.ValidacionEnvioService validacionEnvioService;

    @PutMapping("/validar-recepcion/{idDetalle}")
    public ResponseEntity<com.example.beneficio.dto.RespuestaValidacionDTO> procesarRecepcion(
            @PathVariable Long idDetalle,
            @RequestParam String placa,
            @RequestParam String cui) {

        com.example.beneficio.dto.RespuestaValidacionDTO resultado =
                validacionEnvioService.validarChoferYCamion(idDetalle, placa, cui);

        return ResponseEntity.ok(resultado);
    }
}