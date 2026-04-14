package com.example.beneficio.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.example.beneficio.model.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long>{

}
