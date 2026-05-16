package com.example.beneficio.dto;

import java.math.BigDecimal;

public class CambiarEstadoDTO {
    private Integer idEstadoPesaje; // Coincide con el JSON de Angular
    private String estadoNombre;
    private BigDecimal pesoCabalTotal;

    // Getters y Setters
    public Integer getIdEstadoPesaje() { return idEstadoPesaje; }
    public void setIdEstadoPesaje(Integer idEstadoPesaje) { this.idEstadoPesaje = idEstadoPesaje; }
    public String getEstadoNombre() { return estadoNombre; }
    public void setEstadoNombre(String estadoNombre) { this.estadoNombre = estadoNombre; }
    public BigDecimal getPesoCabalTotal() { return pesoCabalTotal; }
    public void setPesoCabalTotal(BigDecimal pesoCabalTotal) { this.pesoCabalTotal = pesoCabalTotal; }
}