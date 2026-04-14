package com.example.beneficio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.beneficio.model.Estado;
import com.example.beneficio.repository.EstadoRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    public List<Estado> findAllByCountry(Long countryId) {
        return estadoRepository.findAll().stream()
                .filter(estado -> Objects.equals(estado.getPais().getId(), countryId))
                .collect(Collectors.toList());
    }
}

