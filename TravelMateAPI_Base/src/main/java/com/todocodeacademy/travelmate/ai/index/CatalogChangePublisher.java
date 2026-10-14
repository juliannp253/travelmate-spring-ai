package com.todocodeacademy.travelmate.ai.index;

import com.todocodeacademy.travelmate.repository.PaqueteTuristicoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.MANDATORY)
public class CatalogChangePublisher {

    private final PaqueteTuristicoRepository repository;
    private final ApplicationEventPublisher events;

    public CatalogChangePublisher(
            PaqueteTuristicoRepository repository,
            ApplicationEventPublisher events) {

        this.repository = repository;
        this.events = events;
    }

    public void paquete(Long paqueteId) {
        publish(List.of(paqueteId));
    }

    public void destino(Long destinoId) {
        publish(repository.findIdsByDestinoId(destinoId));
    }

    public void hotel(Long hotelId) {
        publish(repository.findIdsByHotelId(hotelId));
    }

    public void actividad(Long actividadId) {
        publish(repository.findIdsByActividadId(actividadId));
    }

    private void publish(List<Long> paqueteIds) {
        if (!paqueteIds.isEmpty()) {
            events.publishEvent(new CatalogChangedEvent(paqueteIds));
        }
    }
}