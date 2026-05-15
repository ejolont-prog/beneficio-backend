package com.example.beneficio.service;

import com.example.beneficio.dto.TransportistaRequestDTO;
import com.example.beneficio.dto.UpdateEstadoTransportistaDTO;
import com.example.beneficio.model.Transportista;
import com.example.beneficio.repository.TransportistaBeneficioRepository;
import com.example.beneficio.security.UserSecurityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate; // <--- AÑADIDO
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransportistaBeneficioService {
    @Autowired
    private TransportistaBeneficioRepository repository;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // <--- AÑADIDO

    @Transactional
    public Transportista procesarRegistro(TransportistaRequestDTO dto) {
        if (repository.existsByCui(dto.getCui())) {
            throw new RuntimeException("Ya existe un transportista registrado con el CUI  " + dto.getCui());
        }

        Transportista t = new Transportista();
        t.setCui(dto.getCui());
        t.setNombrecompleto(dto.getNombreCompleto());
        t.setFechanacimiento(dto.getFechaNacimiento());
        t.setTipolicencia(dto.getNombreTipoLicencia());
        t.setFechavencimientolicencia(dto.getFechaVencimientoLicencia());
        t.setNitAgricultor(dto.getNitAgricultor());
        t.setEstado(7);
        t.setDisponible(true);

        Transportista guardado = repository.save(t);

        // --- NOTIFICAR NUEVO REGISTRO ---
        messagingTemplate.convertAndSend("/topic/actualizacion-transportista-beneficio", guardado);

        return guardado;
    }

    public List<Map<String, Object>> listarTodoDetalle() {
        return repository.listarTodoConDetalle();
    }

    @Transactional
    public void actualizarEstadoSincronizado(UpdateEstadoTransportistaDTO dto, String token) {
        Transportista t = repository.findById(dto.getIdtransportista())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        t.setEstado(dto.getNuevoEstadoIdBeneficio());
        t.setObservaciones(dto.getObservaciones());
        t.setModificadopor(userSecurityService.getCurrentUserId().intValue());
        t.setFechamodificacion(LocalDateTime.now());

        repository.save(t);

        // --- NOTIFICAR VIA WEBSOCKET (Beneficio) ---
        Map<String, Object> payload = new HashMap<>();
        payload.put("idtransportista", t.getIdtransportista());
        payload.put("estado", dto.getNombreEstado());
        payload.put("observaciones", dto.getObservaciones());
        messagingTemplate.convertAndSend("/topic/actualizacion-transportista-beneficio", payload);

        try {
            String urlAgricultor = "http://localhost:8081/api/transportistas/sincronizar-estado";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            Map<String, Object> body = new HashMap<>();
            body.put("cui", dto.getCui());
            body.put("nombreEstado", dto.getNombreEstado());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(urlAgricultor, HttpMethod.PUT, entity, String.class);
        } catch (Exception e) {
            System.err.println("Error sincronizando Agricultor: " + e.getMessage());
        }
    }
}