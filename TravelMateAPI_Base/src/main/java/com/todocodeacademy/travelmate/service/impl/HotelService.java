package com.todocodeacademy.travelmate.service.impl;
import com.todocodeacademy.travelmate.service.interfaces.IHotelService;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;

import com.todocodeacademy.travelmate.dto.HotelRequest;
import com.todocodeacademy.travelmate.exception.RecursoNoEncontradoException;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.Hotel;
import com.todocodeacademy.travelmate.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final IDestinoService destinoService;

    public HotelService(
            HotelRepository hotelRepository,
            IDestinoService destinoService
    ) {
        this.hotelRepository = hotelRepository;
        this.destinoService = destinoService;
    }

    @Override
    public List<Hotel> listar() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel buscarPorId(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el hotel con ID " + id
                ));
    }

    @Override
    public List<Hotel> buscarPorDestino(Long destinoId) {
        return hotelRepository.findByDestinoId(destinoId);
    }

    @Override
    public List<Hotel> buscarPorEstrellas(Integer estrellas) {
        return hotelRepository.findByEstrellasGreaterThanEqual(estrellas);
    }

    @Override
    public Hotel crear(HotelRequest request) {
        Destino destino = destinoService.buscarPorId(request.destinoId());

        Hotel hotel = new Hotel();
        hotel.setNombre(request.nombre());
        hotel.setEstrellas(request.estrellas());
        hotel.setPrecioNoche(request.precioNoche());
        hotel.setDesayunoIncluido(request.desayunoIncluido());
        hotel.setPileta(request.pileta());
        hotel.setSpa(request.spa());
        hotel.setAptoNinos(request.aptoNinos());
        hotel.setDestino(destino);

        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel actualizar(Long id, HotelRequest request) {
        Hotel hotel = buscarPorId(id);
        Destino destino = destinoService.buscarPorId(request.destinoId());

        hotel.setNombre(request.nombre());
        hotel.setEstrellas(request.estrellas());
        hotel.setPrecioNoche(request.precioNoche());
        hotel.setDesayunoIncluido(request.desayunoIncluido());
        hotel.setPileta(request.pileta());
        hotel.setSpa(request.spa());
        hotel.setAptoNinos(request.aptoNinos());
        hotel.setDestino(destino);

        return hotelRepository.save(hotel);
    }

    @Override
    public void eliminar(Long id) {
        hotelRepository.delete(buscarPorId(id));
    }
}
