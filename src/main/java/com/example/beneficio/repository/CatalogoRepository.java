package com.example.beneficio.repository;

import com.example.beneficio.dto.EstadoCatalogoDTO;
import com.example.beneficio.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {

    @Query(value = "SELECT * FROM beneficio.catalogos WHERE idcatalogo = 1", nativeQuery = true)
    List<Catalogo> findEstadosTransporte();

    Optional<Catalogo> findByNombre(String nombre);


    @Query(value = "SELECT id, detallecatalogo FROM beneficio.catalogos WHERE idcatalogo = 4", nativeQuery = true)
    List<Object[]> findEstadosCuentaCatalogo();
}