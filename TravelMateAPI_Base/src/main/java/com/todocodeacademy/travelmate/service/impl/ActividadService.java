package com.todocodeacademy.travelmate.service.impl;
import com.todocodeacademy.travelmate.service.interfaces.IActividadService;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;

import com.todocodeacademy.travelmate.dto.ActividadRequest;
import com.todocodeacademy.travelmate.exception.RecursoNoEncontradoException;
import com.todocodeacademy.travelmate.model.Actividad;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.enums.TipoActividad;
import com.todocodeacademy.travelmate.repository.ActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService implements IActividadService {

    private final ActividadRepository actividadRepository;
    private final IDestinoService destinoService;

    public ActividadService(
            ActividadRepository actividadRepository,
            IDestinoService destinoService
    ) {
        this.actividadRepository = actividadRepository;
        this.destinoService = destinoService;
    }

    @Override
    public List<Actividad> listar() {
        return actividadRepository.findAll();
    }

    @Override
    public Actividad buscarPorId(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la actividad con ID " + id
                ));
    }

    @Override
    public List<Actividad> buscarPorDestino(Long destinoId) {
        return actividadRepository.findByDestinoId(destinoId);
    }

    @Override
    public List<Actividad> buscarPorTipo(TipoActividad tipo) {
        return actividadRepository.findByTipo(tipo);
    }

    @Override
    public Actividad crear(ActividadRequest request) {
        Destino destino = destinoService.buscarPorId(request.destinoId());

        Actividad actividad = new Actividad();
        actividad.setNombre(request.nombre());
        actividad.setTipo(request.tipo());
        actividad.setDescripcion(request.descripcion());
        actividad.setDuracionHoras(request.duracionHoras());
        actividad.setPrecio(request.precio());
        actividad.setAptaNinos(request.aptaNinos());
        actividad.setDestino(destino);

        return actividadRepository.save(actividad);
    }

    @Override
    public Actividad actualizar(Long id, ActividadRequest request) {
        Actividad actividad = buscarPorId(id);
        Destino destino = destinoService.buscarPorId(request.destinoId());

        actividad.setNombre(request.nombre());
        actividad.setTipo(request.tipo());
        actividad.setDescripcion(request.descripcion());
        actividad.setDuracionHoras(request.duracionHoras());
        actividad.setPrecio(request.precio());
        actividad.setAptaNinos(request.aptaNinos());
        actividad.setDestino(destino);

        return actividadRepository.save(actividad);
    }

    @Override
    public void eliminar(Long id) {
        actividadRepository.delete(buscarPorId(id));
    }
}
