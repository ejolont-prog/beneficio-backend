package com.example.beneficio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agricultores", schema = "beneficio") // <--- Obliga a usar el esquema beneficio
public class Agricultor {

    @Id
    private String nit;

    @Column(name = "nombrecomercial") // Mapea el nombre de la columna de tu BD
    private String nombre;

    private String observaciones;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}