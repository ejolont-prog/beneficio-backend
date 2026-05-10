package com.example.beneficio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CuentaDetalleDTO {
    Long getIdCuenta();        // Cambiado a Long para coincidir con el serial8/int8 de BD
    String getNoCuenta();
    String getRazonSocial();
    BigDecimal getPesoTotal();
    Integer getCantParcialidades();
    LocalDateTime getFechaEnvio();
    String getEstadoNombre();
}