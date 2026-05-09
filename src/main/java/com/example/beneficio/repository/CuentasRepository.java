package com.example.beneficio.repository;

import com.example.beneficio.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentasRepository extends JpaRepository<Cuenta, Long> {
    // Cambiamos la 'a' minúscula por 'A' mayúscula para que coincida con la variable de la entidad
    long countByNitAgricultor(String nit);
}