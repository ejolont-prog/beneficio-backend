package com.example.beneficio.dto; // Nota que ahora el paquete es de Beneficio

import java.math.BigDecimal;

public class PesajeExternoDTO {
    private String nitagricultor;
    private BigDecimal pesototalesperado;
    private Long idPesaje;
    private String unidadpeso;

    // Getters y Setters
    public String getNitagricultor() { return nitagricultor; }
    public void setNitagricultor(String nitagricultor) { this.nitagricultor = nitagricultor; }
    public BigDecimal getPesototalesperado() { return pesototalesperado; }
    public void setPesototalesperado(BigDecimal pesototalesperado) { this.pesototalesperado = pesototalesperado; }
    public Long getIdPesaje() { return idPesaje; }
    public void setIdPesaje(Long idPesaje) { this.idPesaje = idPesaje; }


    public String getUnidadpeso(){return unidadpeso;}
    public void setUnidadpeso(String unidadpeso){this.unidadpeso = unidadpeso;}
}