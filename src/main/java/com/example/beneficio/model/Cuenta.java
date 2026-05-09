package com.example.beneficio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas", schema = "beneficio") // Apunta al esquema beneficio
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // O el nombre de tu llave primaria

    // Este es el campo clave para el conteo
    // En el repositorio usamos 'Nitagricultor', por lo tanto aquí debe llamarse 'nitagricultor'


    private String tipocuenta; // Ejemplo: monetaria, ahorros, etc.

    private String banco;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "nitagricultor")
    private String nitAgricultor;

    // También tendrías que actualizar los getters y setters para que coincidan
    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getTipocuenta() { return tipocuenta; }
    public void setTipocuenta(String tipocuenta) { this.tipocuenta = tipocuenta; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}