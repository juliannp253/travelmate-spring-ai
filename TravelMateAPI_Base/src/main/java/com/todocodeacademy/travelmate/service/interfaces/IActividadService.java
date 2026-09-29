package com.todocodeacademy.travelmate.service.interfaces;

import com.todocodeacademy.travelmate.dto.ActividadRequest;
import com.todocodeacademy.travelmate.model.Actividad;
import com.todocodeacademy.travelmate.model.enums.TipoActividad;

import java.util.List;

public interface IActividadService {

    List<Actividad> listar();

    Actividad buscarPorId(Long id);

    List<Actividad> buscarPorDestino(Long destinoId);

    List<Actividad> buscarPorTipo(TipoActividad tipo);

    Actividad crear(ActividadRequest request);

    Actividad actualizar(Long id, ActividadRequest request);

    void eliminar(Long id);
}
