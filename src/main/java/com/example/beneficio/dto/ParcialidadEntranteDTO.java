package com.example.beneficio.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ParcialidadEntranteDTO {
    private String noCuenta;
    private String transporte;    // Viene la placa
    private String transportista; // Viene el CUI
    private BigDecimal pesoParcial;
    private String medida;
    private String nitAgricultor;
    private List<Integer> fechaEnvio; // Jackson a veces mapea LocalDateTime como array [yyyy, mm, dd...]
    private Integer idParcialidad;   // ID original del agricultor
}