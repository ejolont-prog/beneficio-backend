package com.example.beneficio.service;

import com.example.beneficio.dto.ParcialidadEntranteDTO;
import com.example.beneficio.model.DetalleCuenta;
import com.example.beneficio.repository.DetalleCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DetalleCuentaService {

    @Autowired
    private DetalleCuentaRepository detalleCuentaRepository;

    @Transactional
    public void procesarRecepcionParcialidad(ParcialidadEntranteDTO dto) {
        DetalleCuenta detalle = new DetalleCuenta();

        // Mapeo del JSON a la Tabla
        detalle.setNoparcialidad(dto.getIdParcialidad());
        detalle.setNocuenta(dto.getNoCuenta());
        detalle.setPlaca(dto.getTransporte());
        detalle.setCuitransportista(dto.getTransportista());
        detalle.setPesoestimado(dto.getPesoParcial());

        // Valores iniciales por defecto
        detalle.setEstado(1); // Supongamos que 1 es 'Registrado'
        detalle.setEstadopesaje(1);
        detalle.setEliminado(false);
        detalle.setFechacreacion(LocalDateTime.now());
        detalle.setCreadopor(1); // ID de sistema o usuario genérico

        detalleCuentaRepository.save(detalle);

        System.out.println("✅ Registro guardado en detallecuenta para la placa: " + detalle.getPlaca());
    }
}