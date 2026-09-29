package com.todocodeacademy.travelmate.service.impl;
import com.todocodeacademy.travelmate.service.interfaces.IPaqueteTuristicoService;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;
import com.todocodeacademy.travelmate.service.interfaces.IHotelService;
import com.todocodeacademy.travelmate.service.interfaces.IActividadService;

import com.todocodeacademy.travelmate.dto.PaqueteRequest;
import com.todocodeacademy.travelmate.exception.RecursoNoEncontradoException;
import com.todocodeacademy.travelmate.model.*;
import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import com.todocodeacademy.travelmate.repository.PaqueteTuristicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaqueteTuristicoService implements IPaqueteTuristicoService {

    private final PaqueteTuristicoRepository paqueteRepository;
    private final IDestinoService destinoService;
    private final IHotelService hotelService;
    private final IActividadService actividadService;

    public PaqueteTuristicoService(
            PaqueteTuristicoRepository paqueteRepository,
            IDestinoService destinoService,
            IHotelService hotelService,
            IActividadService actividadService
    ) {
        this.paqueteRepository = paqueteRepository;
        this.destinoService = destinoService;
        this.hotelService = hotelService;
        this.actividadService = actividadService;
    }

    @Override
    public List<PaqueteTuristico> listar() {
        return paqueteRepository.findAll();
    }

    @Override
    public PaqueteTuristico buscarPorId(Long id) {
        return paqueteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el paquete con ID " + id
                ));
    }

    @Override
    public List<PaqueteTuristico> buscarPorDestino(Long destinoId) {
        return paqueteRepository.findByDestinoId(destinoId);
    }

    @Override
    public List<PaqueteTuristico> buscarPorPrecioMaximo(BigDecimal precio) {
        return paqueteRepository.findByPrecioPorPersonaLessThanEqual(precio);
    }

    @Override
    public List<PaqueteTuristico> buscarPorDuracion(
            Integer minimo,
            Integer maximo
    ) {
        return paqueteRepository.findByCantidadDiasBetween(minimo, maximo);
    }

    @Override
    public List<PaqueteTuristico> buscarPorTransporte(
            TipoTransporte tipoTransporte
    ) {
        return paqueteRepository.findByTipoTransporte(tipoTransporte);
    }

    @Override
    public PaqueteTuristico crear(PaqueteRequest request) {
        PaqueteTuristico paquete = new PaqueteTuristico();
        copiarDatos(paquete, request);
        return paqueteRepository.save(paquete);
    }

    @Override
    public PaqueteTuristico actualizar(Long id, PaqueteRequest request) {
        PaqueteTuristico paquete = buscarPorId(id);
        copiarDatos(paquete, request);
        return paqueteRepository.save(paquete);
    }

    @Override
    public void eliminar(Long id) {
        paqueteRepository.delete(buscarPorId(id));
    }

    private void copiarDatos(
            PaqueteTuristico paquete,
            PaqueteRequest request
    ) {
        Destino destino = destinoService.buscarPorId(request.destinoId());
        Hotel hotel = hotelService.buscarPorId(request.hotelId());

        List<Actividad> actividades = request.actividadIds() == null
                ? List.of()
                : request.actividadIds()
                        .stream()
                        .map(actividadService::buscarPorId)
                        .toList();

        paquete.setNombre(request.nombre());
        paquete.setCantidadDias(request.cantidadDias());
        paquete.setPrecioPorPersona(request.precioPorPersona());
        paquete.setIncluyeTraslado(request.incluyeTraslado());
        paquete.setIncluyeDesayuno(request.incluyeDesayuno());
        paquete.setIncluyeExcursiones(request.incluyeExcursiones());
        paquete.setTipoTransporte(request.tipoTransporte());
        paquete.setDescripcion(request.descripcion());
        paquete.setDestino(destino);
        paquete.setHotel(hotel);
        paquete.setActividades(actividades);
    }
}
