package com.example.beneficio.dto;

public class RespuestaBeneficioDTO {
    private String nocuenta;
    private Long id; // El ID del pesaje que recibimos

    // Constructor, Getters y Setters
    public RespuestaBeneficioDTO(String nocuenta, Long id) {
        this.nocuenta = nocuenta;
        this.id = id;
    }
    public String getNocuenta() { return nocuenta; }
    public void setNocuenta(String nocuenta) { this.nocuenta = nocuenta; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}