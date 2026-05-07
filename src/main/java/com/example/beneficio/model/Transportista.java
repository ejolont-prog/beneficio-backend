package com.example.beneficio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transportistas", schema = "beneficio")
@Data
public class Transportista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtransportista;

    private String cui;
    private String nombrecompleto;
    private LocalDate fechanacimiento;
    private String tipolicencia; // Aquí guardaremos el TEXTO
    private LocalDate fechavencimientolicencia;
    private Integer estado;

    @Column(name = "creadopor", insertable = false, updatable = false)
    private Integer creadopor; // La DB pondrá el 1

    @Column(name = "agricultor")
    private String nitAgricultor;

    private Boolean disponible = true;

    private String observaciones;
    private Integer modificadopor;
    private LocalDateTime fechamodificacion;

    @PreUpdate
    protected void onUpdate() {
        this.fechamodificacion = LocalDateTime.now();
    }
}