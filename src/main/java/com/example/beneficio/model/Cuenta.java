package com.example.beneficio.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas", schema = "beneficio")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcuenta;

    @Column(name = "nocuenta")
    private String noCuenta;

    private Integer estado;

    @Column(name = "nitagricultor")
    private String nitAgricultor;

    private String tipocuenta;
    private String banco;

    @Column(name = "fechacreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "pesototalesperado")
    private BigDecimal pesoTotalEsperado;

    @Column(name = "id")
    private Long idPesajeExterno;

    @Column(name = "eliminado")
    private Boolean eliminado = false;

    // Forzamos explícitamente el nombre exacto de la columna en la BD
    @Column(name = "estadopesaje", nullable = true)
    private Integer idEstadoPesaje;

    @Column(name = "idunidadpeso")
    private Integer idUnidadPeso;

    @Column(name = "creadopor")
    private Integer creadoPor;

    @Column(name = "modificadopor")
    private Integer modificadoPor;

    // ==========================================
    // NUEVOS CAMPOS AGREGADOS (SIN DUPLICADOS)
    // ==========================================

    @Column(name = "pesototalrecibido")
    private BigDecimal pesoTotalRecibido;

    @Column(name = "diferenciatotal")
    private BigDecimal diferenciaTotal;

    @Column(name = "resultadotolerancia")
    private String resultadoTolerancia;

    @Column(name = "tolerancia")
    private BigDecimal tolerancia;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;


    // ==========================================
    // GETTERS Y SETTERS ORIGINALES
    // ==========================================

    public Long getIdcuenta() { return idcuenta; }
    public void setIdcuenta(Long idcuenta) { this.idcuenta = idcuenta; }

    public String getNoCuenta() { return noCuenta; }
    public void setNoCuenta(String noCuenta) { this.noCuenta = noCuenta; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public String getNitAgricultor() { return nitAgricultor; }
    public void setNitAgricultor(String nitAgricultor) { this.nitAgricultor = nitAgricultor; }

    public String getTipocuenta() { return tipocuenta; }
    public void setTipocuenta(String tipocuenta) { this.tipocuenta = tipocuenta; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public BigDecimal getPesoTotalEsperado() { return pesoTotalEsperado; }
    public void setPesoTotalEsperado(BigDecimal pesoTotalEsperado) { this.pesoTotalEsperado = pesoTotalEsperado; }

    public Long getIdPesajeExterno() { return idPesajeExterno; }
    public void setIdPesajeExterno(Long idPesajeExterno) { this.idPesajeExterno = idPesajeExterno; }

    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }

    public Integer getIdEstadoPesaje() { return idEstadoPesaje; }
    public void setIdEstadoPesaje(Integer idEstadoPesaje) { this.idEstadoPesaje = idEstadoPesaje; }

    public Integer getIdUnidadPeso() { return idUnidadPeso; }
    public void setIdUnidadPeso(Integer idUnidadPeso) { this.idUnidadPeso = idUnidadPeso; }

    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }

    public Integer getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(Integer modificadoPor) { this.modificadoPor = modificadoPor; }

    // ==========================================
    // GETTERS Y SETTERS DE LOS NUEVOS CAMPOS
    // ==========================================

    public BigDecimal getPesoTotalRecibido() { return pesoTotalRecibido; }
    public void setPesoTotalRecibido(BigDecimal pesoTotalRecibido) { this.pesoTotalRecibido = pesoTotalRecibido; }

    public BigDecimal getDiferenciaTotal() { return diferenciaTotal; }
    public void setDiferenciaTotal(BigDecimal diferenciaTotal) { this.diferenciaTotal = diferenciaTotal; }

    public String getResultadoTolerancia() { return resultadoTolerancia; }
    public void setResultadoTolerancia(String resultadoTolerancia) { this.resultadoTolerancia = resultadoTolerancia; }

    public BigDecimal getTolerancia() { return tolerancia; }
    public void setTolerancia(BigDecimal tolerancia) { this.tolerancia = tolerancia; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}