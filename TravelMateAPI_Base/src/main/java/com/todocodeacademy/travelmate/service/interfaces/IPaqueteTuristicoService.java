package com.todocodeacademy.travelmate.service.interfaces;

import com.todocodeacademy.travelmate.dto.PaqueteRequest;
import com.todocodeacademy.travelmate.model.PaqueteTuristico;
import com.todocodeacademy.travelmate.model.enums.TipoTransporte;

import java.math.BigDecimal;
import java.util.List;

public interface IPaqueteTuristicoService {

    List<PaqueteTuristico> listar();

    PaqueteTuristico buscarPorId(Long id);

    List<PaqueteTuristico> buscarPorDestino(Long destinoId);

    List<PaqueteTuristico> buscarPorPrecioMaximo(BigDecimal precio);

    List<PaqueteTuristico> buscarPorDuracion(Integer minimo, Integer maximo);

    List<PaqueteTuristico> buscarPorTransporte(TipoTransporte tipoTransporte);

    PaqueteTuristico crear(PaqueteRequest request);

    PaqueteTuristico actualizar(Long id, PaqueteRequest request);

    void eliminar(Long id);
}
