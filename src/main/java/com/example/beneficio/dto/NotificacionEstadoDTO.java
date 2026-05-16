package com.example.beneficio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificacionEstadoDTO {

    @JsonProperty("noCuenta")
    private String noCuenta;

    @JsonProperty("detalleCatalogo")
    private String detalleCatalogo;

    public NotificacionEstadoDTO(String noCuenta, String detalleCatalogo) {
        this.noCuenta = noCuenta;
        this.detalleCatalogo = detalleCatalogo;
    }

    // Getters y Setters
    public String getNoCuenta() { return noCuenta; }
    public void setNoCuenta(String noCuenta) { this.noCuenta = noCuenta; }
    public String getDetailleCatalogo() { return detalleCatalogo; }
    public void setDetalleCatalogo(String detalleCatalogo) { this.detalleCatalogo = detalleCatalogo; }
}