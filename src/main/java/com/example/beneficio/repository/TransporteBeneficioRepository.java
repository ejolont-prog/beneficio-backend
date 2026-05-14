package com.example.beneficio.repository;

import com.example.beneficio.model.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransporteBeneficioRepository extends JpaRepository<Transporte, Long> {
    boolean existsByPlaca(String placa);

    @Query(value = "SELECT COUNT(*) FROM beneficio.transportes WHERE agricultor = :nit", nativeQuery = true)
    long countByAgricultorNative(@Param("nit") String nit);

    @Query(value = "SELECT " +
            "    t.idtransporte AS idtransporte, " +
            "    t.placa AS placa, " +
            "    COALESCE(a.nombrecomercial, 'SIN NOMBRE') AS agricultor, " +
            "    c.detallecatalogo AS estado, " +
            "    t.observaciones AS observaciones " +
            "FROM beneficio.transportes t " +
            "INNER JOIN beneficio.catalogos c ON (CAST(t.estado AS TEXT) = CAST(c.id AS TEXT)) " +
            "LEFT JOIN beneficio.agricultores a ON (CAST(t.agricultor AS TEXT) = CAST(a.nit AS TEXT)) " +
            "WHERE t.eliminado = false",
            nativeQuery = true)
    List<Map<String, Object>> listarTodoConDetalle();
}