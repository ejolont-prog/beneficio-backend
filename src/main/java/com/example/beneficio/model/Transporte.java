package com.example.beneficio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Table(name = "transportes", schema = "beneficio")
@Data
public class Transporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtransporte;

    private String placa;
    private String marca;
    private String color;
    private String linea;
    private String modelo;
    private Integer estado;

    @Column(name = "agricultor")
    private String nitAgricultor;

    // Al poner insertable = false, forzamos a la DB a usar su DEFAULT 1
    @Column(name = "creadopor", insertable = false, updatable = false)
    private Integer creadopor;

    private Boolean disponible = true;

    @Column(name = "fechacreacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.disponible == null) this.disponible = true;
    }

    // En Transporte.java
    private String observaciones;
    private Integer modificadopor;
    private LocalDateTime fechamodificacion;

    // Actualizamos el método para que se llene automático al actualizar
    @PreUpdate
    protected void onUpdate() {
        this.fechamodificacion = LocalDateTime.now();
    }

}