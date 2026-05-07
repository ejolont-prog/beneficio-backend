package com.example.beneficio.dto;

import lombok.Data;

@Data
public class UpdateEstadoTransportistaDTO {

    private Long idtransportista;

    private String cui;

    private Integer nuevoEstadoIdBeneficio;

    private String nombreEstado;

    private String observaciones;
}