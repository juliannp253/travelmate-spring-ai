package com.todocodeacademy.travelmate.controller;

import com.todocodeacademy.travelmate.dto.PaqueteRequest;
import com.todocodeacademy.travelmate.model.PaqueteTuristico;
import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import com.todocodeacademy.travelmate.service.interfaces.IPaqueteTuristicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/paquetes")
public class PaqueteTuristicoController {

    private final IPaqueteTuristicoService paqueteService;

    public PaqueteTuristicoController(
            IPaqueteTuristicoService paqueteService
    ) {
        this.paqueteService = paqueteService;
    }

    @GetMapping
    public ResponseEntity<List<PaqueteTuristico>> listar() {
        return ResponseEntity.ok(paqueteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteTuristico> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(paqueteService.buscarPorId(id));
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<PaqueteTuristico>> buscarPorDestino(
            @PathVariable Long destinoId
    ) {
        return ResponseEntity.ok(
                paqueteService.buscarPorDestino(destinoId)
        );
    }

    @GetMapping("/precio-maximo")
    public ResponseEntity<List<PaqueteTuristico>> buscarPorPrecioMaximo(
            @RequestParam BigDecimal valor
    ) {
        return ResponseEntity.ok(
                paqueteService.buscarPorPrecioMaximo(valor)
        );
    }

    @GetMapping("/duracion")
    public ResponseEntity<List<PaqueteTuristico>> buscarPorDuracion(
            @RequestParam Integer minimo,
            @RequestParam Integer maximo
    ) {
        return ResponseEntity.ok(
                paqueteService.buscarPorDuracion(minimo, maximo)
        );
    }

    @GetMapping("/transporte/{tipo}")
    public ResponseEntity<List<PaqueteTuristico>> buscarPorTransporte(
            @PathVariable TipoTransporte tipo
    ) {
        return ResponseEntity.ok(
                paqueteService.buscarPorTransporte(tipo)
        );
    }

    @PostMapping
    public ResponseEntity<PaqueteTuristico> crear(
            @Valid @RequestBody PaqueteRequest request
    ) {
        PaqueteTuristico creado = paqueteService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/paquetes/" + creado.getId()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaqueteTuristico> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PaqueteRequest request
    ) {
        return ResponseEntity.ok(
                paqueteService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        paqueteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
