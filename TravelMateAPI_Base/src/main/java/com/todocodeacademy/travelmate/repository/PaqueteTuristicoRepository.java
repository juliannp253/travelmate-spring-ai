package com.todocodeacademy.travelmate.repository;

import com.todocodeacademy.travelmate.model.PaqueteTuristico;
import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PaqueteTuristicoRepository extends JpaRepository<PaqueteTuristico, Long> {

    List<PaqueteTuristico> findByDestinoId(Long destinoId);

    List<PaqueteTuristico> findByPrecioPorPersonaLessThanEqual(BigDecimal precio);

    List<PaqueteTuristico> findByCantidadDiasBetween(Integer minimo, Integer maximo);

    List<PaqueteTuristico> findByTipoTransporte(TipoTransporte tipoTransporte);
}
