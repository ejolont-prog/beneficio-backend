package com.example.beneficio.repository;

import com.example.beneficio.model.DetalleCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleCuentaRepository extends JpaRepository<DetalleCuenta, Integer> {

    // Consulta para obtener todas las parcialidades de una cuenta específica
    @Query(value = "SELECT * FROM beneficio.detallecuenta WHERE nocuenta = :nocuenta AND eliminado = false ORDER BY noparcialidad ASC", nativeQuery = true)
    List<DetalleCuenta> findByNocuenta(@Param("nocuenta") String nocuenta);
}