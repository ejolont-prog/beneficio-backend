package com.example.beneficio.rest;

import com.example.beneficio.dto.TransporteRequestDTO;
import com.example.beneficio.dto.UpdateEstadoDTO;
import com.example.beneficio.model.Transporte;
import com.example.beneficio.repository.TransporteBeneficioRepository;
import com.example.beneficio.service.TransporteBeneficioService; // Importar el servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4600", allowedHeaders = "*")
@RestController
@RequestMapping("/api/transportes-beneficio")
public class TransporteBeneficioREST {

    @Autowired
    private TransporteBeneficioService service; // Cambiado de REST a Service
    @Autowired
    private TransporteBeneficioRepository repository;

    @GetMapping("/todo-detalle")
    public ResponseEntity<List<Map<String, Object>>> listarTodo() {
        List<Map<String, Object>> lista = repository.listarTodoConDetalle();
        System.out.println("Datos encontrados: " + lista.size()); // Debug en consola de Java
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/cambiar-estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        return repository.findById(id).map(t -> {
            // Lógica para alternar: si es 7 (Activo) pasar a 8 (Inactivo) o viceversa
            Integer nuevoEstado = payload.get("estado");
            t.setEstado(nuevoEstado);
            repository.save(t);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/validar-y-crear")
    public ResponseEntity<?> validarYCrear(@RequestBody TransporteRequestDTO dto) {
        try {
            Transporte nuevo = service.procesarRegistro(dto);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) { // Usamos Exception genérica si no has creado BusinessException
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/actualizar-estado-completo")
    public ResponseEntity<?> actualizarEstado(@RequestBody UpdateEstadoDTO dto,
                                              @RequestHeader("Authorization") String token) {
        try {
            service.actualizarEstadoSincronizado(dto, token);
            return ResponseEntity.ok().body(Map.of("message", "Estado actualizado en ambos sistemas"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}