package com.example.beneficio.repository;

import com.example.beneficio.model.DetalleCuenta;
import com.example.beneficio.dto.DetalleCuentaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleCuentaRepository extends JpaRepository<DetalleCuenta, Integer> {

    @Query(value = "SELECT " +
            "d.iddetallecuenta AS iddetallecuenta, " +
            "d.noparcialidad AS noparcialidad, " +
            "d.nocuenta AS nocuenta, " +
            "d.placa AS placa, " +
            "d.cuitransportista AS cuitransportista, " +
            "d.textorechazado AS textorechazado, " +
            "d.pesoestimado AS pesoestimado, " +
            "d.pesorecibido AS pesorecibido, " +
            "d.estado AS estado, " +
            "d.estadopesaje AS estadopesaje, " +
            "d.fecharecepcion AS fecharecepcion, " +
            "c.detallecatalogo AS detallecatalogo " + // Alias idéntico al método de la interfaz
            "FROM beneficio.detallecuenta d " +
            "LEFT JOIN beneficio.catalogos c ON d.estadopesaje = c.id " +
            "WHERE d.nocuenta = :nocuenta AND d.eliminado = false " +
            "ORDER BY d.noparcialidad ASC", nativeQuery = true)
    List<DetalleCuentaDTO> findByNocuenta(@Param("nocuenta") String nocuenta);
}