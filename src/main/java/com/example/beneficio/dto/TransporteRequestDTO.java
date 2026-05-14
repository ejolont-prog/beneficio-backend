package com.example.beneficio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;



@Data

public class TransporteRequestDTO {
    private String placa;
    private Long idTipoPlaca;
    private Long idMarca;
    private Long idLinea;
    private Long idColor;
    private Integer idModelo;
    private String nitAgricultor;
    private String nombreTipoPlaca;

    @JsonProperty("nombreMarca")
    private String nombreMarca;

    @JsonProperty("nombreLinea")
    private String nombreLinea;

    @JsonProperty("nombreColor")
    private String nombreColor;

    public TransporteRequestDTO() {}
}