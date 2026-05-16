package com.example.beneficio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DetalleCuentaDTO {
    Integer getIddetallecuenta();
    Integer getNoparcialidad();
    String getNocuenta();
    String getPlaca();
    String getCuitransportista();
    String getTextorechazado();
    BigDecimal getPesoestimado();
    BigDecimal getPesorecibido();
    Integer getEstado();
    Integer getEstadopesaje();
    LocalDateTime getFecharecepcion();

    // Este alias debe coincidir exactamente con el de la consulta SQL
    String getDetallecatalogo();
}