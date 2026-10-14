package com.todocodeacademy.travelmate.ai.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CatalogChangeListener {

    private static final Logger log =
            LoggerFactory.getLogger(CatalogChangeListener.class);

    private final CatalogIndexService indexService;

    public CatalogChangeListener(CatalogIndexService indexService) {
        this.indexService = indexService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCatalogChanged(CatalogChangedEvent event) {
        for (Long paqueteId : event.paqueteIds().stream().distinct().toList()) {
            try {
                indexService.upsertPackage(paqueteId);

            } catch (RuntimeException exception) {
                log.error(
                        "El cambio en MySQL fue confirmado, pero no se pudo "
                                + "sincronizar el paquete {}. "
                                + "Reparar mediante rebuild. Tipo de error: {}",
                        paqueteId,
                        exception.getClass().getSimpleName()
                );
            }
        }
    }
}