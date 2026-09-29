package com.todocodeacademy.travelmate.service.interfaces;

import com.todocodeacademy.travelmate.dto.HotelRequest;
import com.todocodeacademy.travelmate.model.Hotel;

import java.util.List;

public interface IHotelService {

    List<Hotel> listar();

    Hotel buscarPorId(Long id);

    List<Hotel> buscarPorDestino(Long destinoId);

    List<Hotel> buscarPorEstrellas(Integer estrellas);

    Hotel crear(HotelRequest request);

    Hotel actualizar(Long id, HotelRequest request);

    void eliminar(Long id);
}
