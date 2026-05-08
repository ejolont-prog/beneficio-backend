package com.example.beneficio.dto;

import lombok.Data;

@Data
public class UpdateEstadoDTO {
    private Long idtransporte;
    private String placa;
    private Integer nuevoEstadoIdBeneficio;
    private String nombreEstado; // "Activo" o "Inactivo"
    private String observaciones;
}