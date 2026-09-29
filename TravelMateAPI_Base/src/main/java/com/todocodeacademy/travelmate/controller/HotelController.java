package com.todocodeacademy.travelmate.controller;

import com.todocodeacademy.travelmate.dto.HotelRequest;
import com.todocodeacademy.travelmate.model.Hotel;
import com.todocodeacademy.travelmate.service.interfaces.IHotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hoteles")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> listar() {
        return ResponseEntity.ok(hotelService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.buscarPorId(id));
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<Hotel>> buscarPorDestino(
            @PathVariable Long destinoId
    ) {
        return ResponseEntity.ok(hotelService.buscarPorDestino(destinoId));
    }

    @GetMapping("/estrellas/{estrellas}")
    public ResponseEntity<List<Hotel>> buscarPorEstrellas(
            @PathVariable Integer estrellas
    ) {
        return ResponseEntity.ok(hotelService.buscarPorEstrellas(estrellas));
    }

    @PostMapping
    public ResponseEntity<Hotel> crear(
            @Valid @RequestBody HotelRequest request
    ) {
        Hotel creado = hotelService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/hoteles/" + creado.getId()))
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequest request
    ) {
        return ResponseEntity.ok(hotelService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        hotelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
