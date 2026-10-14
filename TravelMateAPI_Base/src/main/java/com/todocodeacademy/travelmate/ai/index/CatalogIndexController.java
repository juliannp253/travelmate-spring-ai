package com.todocodeacademy.travelmate.ai.index;

import com.todocodeacademy.travelmate.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Profile("dev")
@RequestMapping("/api/ai/index")
public class CatalogIndexController {

    private final CatalogIndexService indexService;
    private final byte[] adminSecret;

    public CatalogIndexController(
            CatalogIndexService indexService,
            @Value("${travelmate.ai.index.admin-secret}") String adminSecret) {

        Assert.hasText(
                adminSecret,
                "Debes configurar el secreto administrativo del índice"
        );

        this.indexService = indexService;
        this.adminSecret = adminSecret.getBytes(StandardCharsets.UTF_8);
    }

    @PostMapping("/rebuild")
    public ResponseEntity<?> rebuild(
            @RequestHeader(
                    name = "X-Admin-Secret",
                    required = false
            ) String suppliedSecret) {

        if (!authorized(suppliedSecret)) {
            return error(
                    HttpStatus.FORBIDDEN,
                    "ACCESO_DENEGADO",
                    "Se requiere un secreto administrativo válido."
            );
        }

        try {
            int documents = indexService.rebuild();

            return ResponseEntity.ok(
                    new RebuildResponse("COMPLETADO", documents)
            );

        } catch (RuntimeException exception) {
            return error(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "INDEXACION_NO_COMPLETADA",
                    "No se pudo reconstruir el índice. "
                            + "Revisa los registros del servidor."
            );
        }
    }

    private boolean authorized(String suppliedSecret) {
        return suppliedSecret != null
                && MessageDigest.isEqual(
                adminSecret,
                suppliedSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    private ResponseEntity<ErrorResponse> error(
            HttpStatus status,
            String code,
            String message) {

        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        code,
                        message,
                        Map.of()
                )
        );
    }

    public record RebuildResponse(
            String estado,
            int documentosIndexados
    ) {
    }
}