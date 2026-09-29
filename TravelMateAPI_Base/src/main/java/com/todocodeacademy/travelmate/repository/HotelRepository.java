package com.todocodeacademy.travelmate.repository;

import com.todocodeacademy.travelmate.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByDestinoId(Long destinoId);

    List<Hotel> findByEstrellasGreaterThanEqual(Integer estrellas);

    List<Hotel> findByAptoNinosTrue();
}
