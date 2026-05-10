package com.example.beneficio.rest;

import com.example.beneficio.dto.RespuestaBeneficioDTO;
import com.example.beneficio.model.Cuenta;
import com.example.beneficio.repository.CuentasRepository;
import com.example.beneficio.dto.PesajeExternoDTO; // <--- Importa el de este proyecto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/recepcion-pesaje")
public class RecepcionPesajeREST {

    @Autowired
    private CuentasRepository cuentasRepository;

    @PostMapping("/guardar-externo")
    public ResponseEntity<?> recibirDesdeAgricultor(@RequestBody PesajeExternoDTO dto) {
        try {
            Cuenta nuevaCuenta = new Cuenta();

            // 1. Generar el correlativo para nocuenta
            Integer ultimoCorrelativo = cuentasRepository.findMaxNoCuenta();
            int siguiente = (ultimoCorrelativo != null) ? ultimoCorrelativo + 1 : 1;
            String nuevoNoCuenta = String.format("%05d", siguiente);

            // 2. Mapear datos del JSON
            nuevaCuenta.setNitAgricultor(dto.getNitagricultor());
            nuevaCuenta.setPesoTotalEsperado(dto.getPesototalesperado());
            nuevaCuenta.setIdPesajeExterno(dto.getIdPesaje());

            // 3. Datos automáticos
            nuevaCuenta.setNoCuenta(nuevoNoCuenta);
            nuevaCuenta.setFechaCreacion(LocalDateTime.now());
            nuevaCuenta.setEliminado(false);
            nuevaCuenta.setBanco("SISTEMA AGRICULTOR");
            nuevaCuenta.setTipocuenta("AUTOMÁTICO");
            nuevaCuenta.setEstado(1);

            cuentasRepository.save(nuevaCuenta);

            // RESPUESTA: Devolvemos el objeto con el nuevo número de cuenta y el ID original
            return ResponseEntity.ok(new RespuestaBeneficioDTO(nuevoNoCuenta, dto.getIdPesaje()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}