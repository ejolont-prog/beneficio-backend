package com.example.beneficio.repository;

import java.util.List;
import com.example.beneficio.dto.CuentaDetalleDTO;
import com.example.beneficio.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentasRepository extends JpaRepository<Cuenta, Long> {
    // Cambiamos la 'a' minúscula por 'A' mayúscula para que coincida con la variable de la entidad
    long countByNitAgricultor(String nit);

    // En CuentasRepository.java
    @Query(value = "SELECT COALESCE(MAX(CAST(nocuenta AS INTEGER)), 0) FROM beneficio.cuentas", nativeQuery = true)
    Integer findMaxNoCuenta();

    @Query(value = "SELECT " +
            "c.idcuenta as idCuenta, " + // <-- AGREGAR ESTA LÍNEA
            "c.nocuenta as noCuenta, " +
            "a.razonsocial as razonSocial, " +
            "c.pesototalesperado as pesoTotal, " +
            "0 as cantParcialidades, " +
            "c.fechacreacion as fechaEnvio, " +
            "cat.detallecatalogo as estadoNombre " +
            "FROM beneficio.cuentas c " +
            "LEFT JOIN beneficio.agricultores a ON c.nitagricultor = a.nit " +
            "LEFT JOIN beneficio.catalogos cat ON c.idestadopesaje = cat.id " +
            "WHERE c.eliminado = false", nativeQuery = true)
    List<CuentaDetalleDTO> findAllDetalles();
}