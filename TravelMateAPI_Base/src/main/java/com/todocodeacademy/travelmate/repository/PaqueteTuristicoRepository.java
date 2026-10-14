package com.todocodeacademy.travelmate.repository;

import com.todocodeacademy.travelmate.model.PaqueteTuristico;
import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaqueteTuristicoRepository extends JpaRepository<PaqueteTuristico, Long> {

    List<PaqueteTuristico> findByDestinoId(Long destinoId);

    List<PaqueteTuristico> findByPrecioPorPersonaLessThanEqual(BigDecimal precio);

    List<PaqueteTuristico> findByCantidadDiasBetween(Integer minimo, Integer maximo);

    List<PaqueteTuristico> findByTipoTransporte(TipoTransporte tipoTransporte);

    @Query("""
        select p.id
        from PaqueteTuristico p
        where p.destino.id = :destinoId
        """)
    List<Long> findIdsByDestinoId(@Param("destinoId") Long destinoId);

    @Query("""
        select p.id
        from PaqueteTuristico p
        where p.hotel.id = :hotelId
        """)
    List<Long> findIdsByHotelId(@Param("hotelId") Long hotelId);

    @Query("""
        select distinct p.id
        from PaqueteTuristico p
        join p.actividades a
        where a.id = :actividadId
        """)
    List<Long> findIdsByActividadId(@Param("actividadId") Long actividadId);
}
