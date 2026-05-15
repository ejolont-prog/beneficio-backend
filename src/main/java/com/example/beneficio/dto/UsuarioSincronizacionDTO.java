package com.example.beneficio.dto;

import lombok.Data;

@Data
public class UsuarioSincronizacionDTO {
    private String usuario;
    private String correo;
    private String contrasena;
    private String nit;
    private Integer idrol;
}