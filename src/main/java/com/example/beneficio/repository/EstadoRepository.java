package com.example.beneficio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.example.beneficio.model.Estado;
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{

}

