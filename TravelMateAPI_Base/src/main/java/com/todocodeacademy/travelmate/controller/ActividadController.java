package com.todocodeacademy.travelmate.controller;

import com.todocodeacademy.travelmate.dto.ActividadRequest;
import com.todocodeacademy.travelmate.model.Actividad;
import com.todocodeacademy.travelmate.model.enums.TipoActividad;
import com.todocodeacademy.travelmate.service.interfaces.IActividadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final IActividadService actividadService;

    public ActividadController(IActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping
    public ResponseEntity<List<Actividad>> listar() {
        return ResponseEntity.ok(actividadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.buscarPorId(id));
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<Actividad>> buscarPorDestino(
            @PathVariable Long destinoId
    ) {
        return ResponseEntity.ok(
                actividadService.buscarPorDestino(destinoId)
        );
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Actividad>> buscarPorTipo(
            @PathVariable TipoActividad tipo
    ) {
        return ResponseEntity.ok(actividadService.buscarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<Actividad> crear(
            @Valid @RequestBody ActividadRequest request
    ) {
        Actividad creada = actividadService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/actividades/" + creada.getId()))
                .body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActividadRequest request
    ) {
        return ResponseEntity.ok(
                actividadService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
