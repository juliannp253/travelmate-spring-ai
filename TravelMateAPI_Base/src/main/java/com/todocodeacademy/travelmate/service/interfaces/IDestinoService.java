package com.todocodeacademy.travelmate.service.interfaces;

import com.todocodeacademy.travelmate.dto.DestinoRequest;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.enums.TipoDestino;

import java.util.List;

public interface IDestinoService {

    List<Destino> listar();

    Destino buscarPorId(Long id);

    List<Destino> buscarPorPais(String pais);

    List<Destino> buscarPorTipo(TipoDestino tipo);

    List<Destino> listarTranquilos();

    List<Destino> listarFamiliares();

    List<Destino> listarParaParejas();

    Destino crear(DestinoRequest request);

    Destino actualizar(Long id, DestinoRequest request);

    void eliminar(Long id);
}
