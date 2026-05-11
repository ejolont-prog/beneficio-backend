package com.example.beneficio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "detallecuenta", schema = "beneficio")
public class DetalleCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddetallecuenta;

    private Integer noparcialidad; // Aquí guardamos el idParcialidad del agricultor
    private String nocuenta;
    private String placa;
    private String cuitransportista;

    @Column(columnDefinition = "TEXT")
    private String textorechazado;

    private BigDecimal pesoestimado;
    private BigDecimal pesorecibido;

    private Integer estado; // Ejemplo: 1 para "En Tránsito" o "Pendiente"
    private Integer estadopesaje;

    private LocalDateTime fecharecepcion;

    private Integer creadopor;
    private Integer modificadopor;

    @Column(name = "fechacreacion", insertable = false, updatable = false)
    private LocalDateTime fechacreacion;

    private LocalDateTime fechamodificacion;

    private Boolean eliminado = false;
}