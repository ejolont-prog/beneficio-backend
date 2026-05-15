package com.example.beneficio.service;

import com.example.beneficio.dto.TransporteRequestDTO;
import com.example.beneficio.dto.UpdateEstadoDTO;
import com.example.beneficio.model.Transporte;
import com.example.beneficio.repository.TransporteBeneficioRepository;
import com.example.beneficio.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate; // <--- AÑADIDO
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Service
public class TransporteBeneficioService {

    @Autowired
    private TransporteBeneficioRepository repository;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // <--- AÑADIDO

    @Transactional
    public void actualizarEstadoSincronizado(UpdateEstadoDTO dto, String token) {
        // 1. Local (Beneficio)
        Transporte t = repository.findById(dto.getIdtransporte())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        t.setEstado(dto.getNuevoEstadoIdBeneficio());
        t.setObservaciones(dto.getObservaciones());
        t.setModificadopor(userSecurityService.getCurrentUserId().intValue());
        t.setFechamodificacion(LocalDateTime.now());
        repository.save(t);

        // --- NOTIFICAR VIA WEBSOCKET (Beneficio) ---
        Map<String, Object> payload = new HashMap<>();
        payload.put("idtransporte", t.getIdtransporte());
        payload.put("estado", dto.getNombreEstado());
        payload.put("observaciones", dto.getObservaciones());
        messagingTemplate.convertAndSend("/topic/actualizacion-transporte-beneficio", payload);

        // 2. Sincronización
        try {
            String urlAgricultor = "http://localhost:8081/api/transportes/sincronizar-estado";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            Map<String, Object> body = new HashMap<>();
            body.put("placa", dto.getPlaca());
            body.put("nombreEstado", dto.getNombreEstado());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(urlAgricultor, HttpMethod.PUT, entity, String.class);
        } catch (Exception e) {
            System.err.println("Error en Agricultor: " + e.getMessage());
        }
    }

    @Transactional
    public Transporte procesarRegistro(TransporteRequestDTO dto) {
        if (repository.existsByPlaca(dto.getPlaca())) {
            throw new RuntimeException("Ya existe un transporte registrado con la placa " + dto.getPlaca());
        }

        Transporte t = new Transporte();
        t.setPlaca(dto.getPlaca());
        t.setTipoplaca(dto.getNombreTipoPlaca());
        t.setMarca(dto.getNombreMarca());
        t.setLinea(dto.getNombreLinea());
        t.setColor(dto.getNombreColor());

        t.setModelo(dto.getIdModelo().toString());
        t.setNitAgricultor(dto.getNitAgricultor());
        t.setEstado(7);
        t.setDisponible(true);

        Transporte guardado = repository.save(t);

        // --- NOTIFICAR NUEVO REGISTRO ---
        messagingTemplate.convertAndSend("/topic/actualizacion-transporte-beneficio", guardado);

        return guardado;
    }
}