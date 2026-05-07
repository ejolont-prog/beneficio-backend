package com.example.beneficio.repository;

import com.example.beneficio.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {

    // Aquí añades el query que filtrará solo los estados (idcatalogo = 1)
    @Query(value = "SELECT * FROM beneficio.catalogos WHERE idcatalogo = 1", nativeQuery = true)
    List<Catalogo> findEstadosTransporte();
}