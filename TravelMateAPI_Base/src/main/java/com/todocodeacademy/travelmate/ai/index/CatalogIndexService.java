package com.todocodeacademy.travelmate.ai.index;

import com.todocodeacademy.travelmate.repository.PaqueteTuristicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CatalogIndexService {

    private static final Logger log =
            LoggerFactory.getLogger(CatalogIndexService.class);

    private final PaqueteTuristicoRepository repository;
    private final CatalogDocumentMapper mapper;
    private final PgVectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate postgresTransaction;
    private final String embeddingModelName;
    private final int dimensions;
    private final String table;

    public CatalogIndexService(
            PaqueteTuristicoRepository repository,
            CatalogDocumentMapper mapper,
            PgVectorStore vectorStore,
            EmbeddingModel embeddingModel,
            @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate,
            @Value("${spring.ai.openai.embedding.model}")
            String embeddingModelName,
            @Value("${travelmate.ai.vectorstore.dimensions}") int dimensions,
            @Value("${travelmate.ai.vectorstore.schema-name}") String schemaName,
            @Value("${travelmate.ai.vectorstore.table-name}") String tableName) {

        this.repository = repository;
        this.mapper = mapper;
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingModelName = embeddingModelName;
        this.dimensions = dimensions;

        this.table = sqlIdentifier(schemaName)
                + "." + sqlIdentifier(tableName);

        this.postgresTransaction = new TransactionTemplate(
                new DataSourceTransactionManager(
                        Objects.requireNonNull(jdbcTemplate.getDataSource())
                )
        );
    }

    public synchronized int rebuild() {
        String etapa = "lectura del catálogo MySQL";

        try {
            List<Document> documents = repository.findAll().stream()
                    .map(mapper::toDocument)
                    .map(this::withEmbeddingModel)
                    .toList();

            etapa = "validación del modelo en OpenRouter";
            validateEmbeddingModel();

            etapa = "reconstrucción en PostgreSQL y generación de embeddings";

            postgresTransaction.executeWithoutResult(status -> {
                jdbcTemplate.update("DELETE FROM " + table);

                if (!documents.isEmpty()) {
                    vectorStore.add(documents);
                }
            });

            log.info("Catálogo reconstruido: {} documentos", documents.size());
            return documents.size();

        } catch (RuntimeException exception) {
            throw failure("rebuild", etapa, exception);
        }
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            readOnly = true
    )
    public synchronized boolean upsertPackage(Long paqueteId) {
        Assert.notNull(paqueteId, "El ID del paquete es obligatorio");

        String etapa = "lectura del paquete en MySQL";

        try {
            var paquete = repository.findById(paqueteId);

            if (paquete.isEmpty()) {
                deletePackage(paqueteId);
                return false;
            }

            Document document =
                    withEmbeddingModel(mapper.toDocument(paquete.get()));

            etapa = "consulta del documento en PostgreSQL";
            ensureCompatibleIndex();

            List<String> previousContents = jdbcTemplate.query(
                    "SELECT content FROM " + table + " WHERE id = ?::uuid",
                    (resultSet, rowNumber) -> resultSet.getString("content"),
                    document.getId()
            );

            if (!previousContents.isEmpty()
                    && Objects.equals(previousContents.getFirst(),
                    document.getText())) {
                log.info("Paquete {} sin cambios; no se generan embeddings",
                        paqueteId);
                return false;
            }

            etapa = "validación del modelo en OpenRouter";
            validateEmbeddingModel();

            etapa = "actualización en PostgreSQL y generación de embeddings";

            postgresTransaction.executeWithoutResult(status -> {
                vectorStore.delete(List.of(document.getId()));
                vectorStore.add(List.of(document));
            });

            log.info("Documento actualizado para el paquete {}", paqueteId);
            return true;

        } catch (RuntimeException exception) {
            throw failure("upsertPackage", etapa, exception);
        }
    }

    public synchronized void deletePackage(Long paqueteId) {
        Assert.notNull(paqueteId, "El ID del paquete es obligatorio");

        try {
            vectorStore.delete(List.of(mapper.documentId(paqueteId)));
            log.info("Documento eliminado para el paquete {}", paqueteId);

        } catch (RuntimeException exception) {
            throw failure(
                    "deletePackage",
                    "eliminación en PostgreSQL",
                    exception
            );
        }
    }

    private Document withEmbeddingModel(Document document) {
        document.getMetadata().put("embeddingModel", embeddingModelName);
        return document;
    }

    private void ensureCompatibleIndex() {
        Long incompatibleDocuments = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + table
                        + " WHERE (metadata ->> 'embeddingModel')"
                        + " IS DISTINCT FROM ?",
                Long.class,
                embeddingModelName
        );

        if (incompatibleDocuments != null && incompatibleDocuments > 0) {
            throw new IllegalStateException(
                    "El índice corresponde a otro modelo o no tiene "
                            + "su identificación. Ejecuta rebuild."
            );
        }
    }

    private void validateEmbeddingModel() {
        if (!embeddingModelName.endsWith(":free")) {
            throw new IllegalStateException(
                    "El modelo de embeddings debe usar una ruta :free."
            );
        }

        float[] probe = embeddingModel.embed(
                "Comprobación de embeddings del catálogo TravelMate"
        );

        if (probe == null || probe.length != dimensions) {
            throw new IllegalStateException(
                    "La dimensión del modelo no coincide con "
                            + dimensions + "."
            );
        }

        for (float value : probe) {
            if (!Float.isFinite(value)) {
                throw new IllegalStateException(
                        "El modelo devolvió un vector inválido."
                );
            }
        }
    }

    private IllegalStateException failure(
            String operation,
            String stage,
            RuntimeException exception) {

        log.error(
                "Falló {} durante {}. Tipo de error: {}",
                operation,
                stage,
                exception.getClass().getSimpleName()
        );

        return new IllegalStateException(
                "No se pudo completar la indexación: " + stage + ".",
                exception
        );
    }

    private static String sqlIdentifier(String value) {
        Assert.isTrue(
                value != null && value.matches("[A-Za-z_][A-Za-z0-9_]*"),
                "Nombre de esquema o tabla inválido"
        );

        return "\"" + value + "\"";
    }
}

