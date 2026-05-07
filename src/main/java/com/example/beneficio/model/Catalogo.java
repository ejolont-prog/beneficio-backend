package com.example.beneficio.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "catalogos", schema = "beneficio")
@Data
public class Catalogo {
    @Id
    @Column(name = "id") // Asegúrate que coincida con el nombre en la BD
    private Integer id;

    @Column(name = "detallecatalogo") // Asegúrate que coincida con el nombre en la BD
    private String nombre;

    @Column(name = "idcatalogo")
    private Integer idCatalogo;
}