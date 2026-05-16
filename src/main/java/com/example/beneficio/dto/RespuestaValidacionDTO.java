package com.example.beneficio.dto;

public class RespuestaValidacionDTO {
    private boolean exito;
    private String mensaje;
    private Integer nuevoEstado;

    public RespuestaValidacionDTO(boolean exito, String mensaje, Integer nuevoEstado) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nuevoEstado = nuevoEstado;
    }

    // Getters y Setters
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Integer getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(Integer nuevoEstado) { this.nuevoEstado = nuevoEstado; }
}