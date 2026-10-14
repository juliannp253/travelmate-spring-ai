package com.todocodeacademy.travelmate.ai.index;

import java.util.List;

public record CatalogChangedEvent(List<Long> paqueteIds) {

    public CatalogChangedEvent {
        paqueteIds = List.copyOf(paqueteIds);
    }
}