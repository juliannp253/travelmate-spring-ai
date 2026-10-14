package com.todocodeacademy.travelmate.service.impl;
import com.todocodeacademy.travelmate.service.interfaces.IHotelService;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;

import com.todocodeacademy.travelmate.dto.HotelRequest;
import com.todocodeacademy.travelmate.exception.RecursoNoEncontradoException;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.Hotel;
import com.todocodeacademy.travelmate.repository.HotelRepository;
import org.springframework.stereotype.Service;
import com.todocodeacademy.travelmate.ai.index.CatalogChangePublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final IDestinoService destinoService;
    private final CatalogChangePublisher catalogChanges;

    public HotelService(
            HotelRepository hotelRepository,
            IDestinoService destinoService,
            CatalogChangePublisher catalogChanges
    ) {
        this.hotelRepository = hotelRepository;
        this.destinoService = destinoService;
        this.catalogChanges = catalogChanges;
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
    @Transactional
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

        Hotel guardado = hotelRepository.save(hotel);
        catalogChanges.hotel(guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
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

        Hotel guardado = hotelRepository.save(hotel);
        catalogChanges.hotel(guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Hotel hotel = buscarPorId(id);

        catalogChanges.hotel(id);
        hotelRepository.delete(hotel);
    }
}
