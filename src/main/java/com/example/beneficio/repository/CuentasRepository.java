package com.example.beneficio.repository;

import java.util.List;
import com.example.beneficio.dto.CuentaDetalleDTO;
import com.example.beneficio.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentasRepository extends JpaRepository<Cuenta, Long> {

    // CORRECCIÓN: Usamos TRIM en la columna y en el parámetro para evitar errores por espacios
    @Query(value = "SELECT COUNT(*) FROM beneficio.cuentas WHERE TRIM(nitagricultor) = TRIM(:nit)", nativeQuery = true)
    long countByNitAgricultorNative(@Param("nit") String nit);

    @Query(value = "SELECT COALESCE(MAX(CAST(nocuenta AS INTEGER)), 0) FROM beneficio.cuentas", nativeQuery = true)
    Integer findMaxNoCuenta();

    @Query(value = "SELECT " +
            "c.idcuenta as idCuenta, " +
            "c.nocuenta as noCuenta, " +
            "a.razonsocial as razonSocial, " +
            "c.pesototalesperado as pesoTotal, " +
            "c.fechacreacion as fechaEnvio, " +
            "(SELECT COUNT(*) FROM beneficio.detallecuenta dc WHERE dc.nocuenta = c.nocuenta AND dc.eliminado = false) as cantParcialidades, " +
            "cat.detallecatalogo as estadoNombre " +
            "FROM beneficio.cuentas c " +
            "LEFT JOIN beneficio.agricultores a ON TRIM(c.nitagricultor) = TRIM(a.nit) " + // También aplicamos TRIM en el JOIN
            "LEFT JOIN beneficio.catalogos cat ON c.estadopesaje = cat.id " +
            "WHERE c.eliminado = false", nativeQuery = true)
    List<CuentaDetalleDTO> findAllDetalles();
}