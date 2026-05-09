package com.example.beneficio.repository;

import com.example.beneficio.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransportistaBeneficioRepository extends JpaRepository<Transportista, Long> {

    /**
     * Verifica si ya existe un transportista con el mismo CUI
     * para evitar registros duplicados en Beneficio.
     */
    boolean existsByCui(String cui);
    long countByNitAgricultor(String nitAgricultor);
    @Query(value = "SELECT " +
            "    t.idtransportista, " +
            "    COALESCE(a.nombrecomercial, 'SIN NOMBRE') as agricultor, " +
            "    t.cui, " +
            "    t.nombrecompleto, " +
            "    c.detallecatalogo as estado, " +
            "    t.observaciones " +
            "FROM beneficio.transportistas t " +
            "INNER JOIN beneficio.catalogos c ON t.estado = c.id " +
            "LEFT JOIN beneficio.agricultores a ON t.agricultor = a.nit " +
            "WHERE t.eliminado = false " +
            "ORDER BY t.idtransportista DESC",
            nativeQuery = true)
    List<Map<String, Object>> listarTodoConDetalle();

}