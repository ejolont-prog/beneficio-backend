package com.example.beneficio.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importante para el peso
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas", schema = "beneficio")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcuenta; // Esta es la PK serial4 de tu tabla

    @Column(name = "nocuenta") // Varchar(50) en BD
    private String noCuenta;

    private Integer estado; // int4 en BD

    @Column(name = "nitagricultor")
    private String nitAgricultor;

    private String tipocuenta;
    private String banco;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    // --- NUEVOS CAMPOS PARA EL JSON (Sin borrar lo anterior) ---

    @Column(name = "pesototalesperado")
    private BigDecimal pesoTotalEsperado;

    @Column(name = "id") // El campo int8 que viste en la imagen
    private Long idPesajeExterno;

    @Column(name = "eliminado")
    private Boolean eliminado = false;

    @Column(name = "idestadopesaje") // Para el ID 27
    private Integer idEstadoPesaje;

    @Column(name = "idunidadpeso")   // Para el ID del catálogo (Quintal, etc.)
    private Integer idUnidadPeso;

    @Column(name = "creadopor")      // Para el ID 1
    private Integer creadoPor;

    @Column(name = "modificadopor")  // Para el ID 1
    private Integer modificadoPor;

    // --- GETTERS Y SETTERS EXISTENTES ---
    public Long getIdcuenta() { return idcuenta; }
    public void setIdcuenta(Long idcuenta) { this.idcuenta = idcuenta; }

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    public String getTipocuenta() { return tipocuenta; }
    public void setTipocuenta(String tipocuenta) { this.tipocuenta = tipocuenta; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // --- NUEVOS GETTERS Y SETTERS ---
    public BigDecimal getPesoTotalEsperado() { return pesoTotalEsperado; }
    public void setPesoTotalEsperado(BigDecimal pesoTotalEsperado) { this.pesoTotalEsperado = pesoTotalEsperado; }

    public Long getIdPesajeExterno() { return idPesajeExterno; }
    public void setIdPesajeExterno(Long idPesajeExterno) { this.idPesajeExterno = idPesajeExterno; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public String getNoCuenta() { return noCuenta; }
    public void setNoCuenta(String noCuenta) { this.noCuenta = noCuenta; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Integer getIdEstadoPesaje() { return idEstadoPesaje; }
    public void setIdEstadoPesaje(Integer idEstadoPesaje) { this.idEstadoPesaje = idEstadoPesaje; }

    public Integer getIdUnidadPeso() { return idUnidadPeso; }
    public void setIdUnidadPeso(Integer idUnidadPeso) { this.idUnidadPeso = idUnidadPeso; }

    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }

    public Integer getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(Integer modificadoPor) { this.modificadoPor = modificadoPor; }
}
