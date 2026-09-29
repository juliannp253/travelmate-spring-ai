package com.todocodeacademy.travelmate.repository;

import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.enums.TipoDestino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinoRepository extends JpaRepository<Destino, Long> {

    List<Destino> findByPaisIgnoreCase(String pais);

    List<Destino> findByTipo(TipoDestino tipo);

    List<Destino> findByTranquiloTrue();

    List<Destino> findByAptoFamiliasTrue();

    List<Destino> findByAptoParejasTrue();
}
