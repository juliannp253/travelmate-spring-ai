package com.todocodeacademy.travelmate.controller;

import com.todocodeacademy.travelmate.dto.DestinoRequest;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.enums.TipoDestino;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    private final IDestinoService destinoService;

    public DestinoController(IDestinoService destinoService) {
        this.destinoService = destinoService;
    }

    @GetMapping
    public ResponseEntity<List<Destino>> listar() {
        return ResponseEntity.ok(destinoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(destinoService.buscarPorId(id));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Destino>> buscarPorPais(
            @PathVariable String pais
    ) {
        return ResponseEntity.ok(destinoService.buscarPorPais(pais));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Destino>> buscarPorTipo(
            @PathVariable TipoDestino tipo
    ) {
        return ResponseEntity.ok(destinoService.buscarPorTipo(tipo));
    }

    @GetMapping("/tranquilos")
    public ResponseEntity<List<Destino>> listarTranquilos() {
        return ResponseEntity.ok(destinoService.listarTranquilos());
    }

    @GetMapping("/familiares")
    public ResponseEntity<List<Destino>> listarFamiliares() {
        return ResponseEntity.ok(destinoService.listarFamiliares());
    }

    @GetMapping("/parejas")
    public ResponseEntity<List<Destino>> listarParaParejas() {
        return ResponseEntity.ok(destinoService.listarParaParejas());
    }

    @PostMapping
    public ResponseEntity<Destino> crear(
            @Valid @RequestBody DestinoRequest request
    ) {
        Destino creado = destinoService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/destinos/" + creado.getId()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DestinoRequest request
    ) {
        return ResponseEntity.ok(destinoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        destinoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
