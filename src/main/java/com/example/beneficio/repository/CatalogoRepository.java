package com.example.beneficio.repository;

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

    // Cambiamos findByDetallecatalogo por findByNombre
    // (Spring buscará automáticamente en la columna mapeada a 'nombre')
    Optional<Catalogo> findByNombre(String nombre);
}