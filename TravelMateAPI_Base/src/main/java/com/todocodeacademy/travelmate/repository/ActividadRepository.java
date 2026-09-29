package com.todocodeacademy.travelmate.repository;

import com.todocodeacademy.travelmate.model.Actividad;
import com.todocodeacademy.travelmate.model.enums.TipoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByDestinoId(Long destinoId);

    List<Actividad> findByTipo(TipoActividad tipo);

    List<Actividad> findByAptaNinosTrue();
}
