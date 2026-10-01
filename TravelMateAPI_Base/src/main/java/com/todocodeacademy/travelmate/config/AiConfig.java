package com.todocodeacademy.travelmate.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration(proxyBeanMethods = false)
public class AiConfig {

    @Bean
    public PgVectorStore pgVectorStore(
            @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate,
            EmbeddingModel embeddingModel,
            @Value("${travelmate.ai.vectorstore.dimensions}") int dimensions,
            @Value("${travelmate.ai.vectorstore.index-type}")
            PgVectorStore.PgIndexType indexType,
            @Value("${travelmate.ai.vectorstore.initialize-schema}")
            boolean initializeSchema,
            @Value("${travelmate.ai.vectorstore.remove-existing-vector-store-table}")
            boolean removeExistingTable,
            @Value("${travelmate.ai.vectorstore.schema-name}") String schemaName,
            @Value("${travelmate.ai.vectorstore.table-name}") String tableName) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(dimensions)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(indexType)
                .initializeSchema(initializeSchema)
                .removeExistingVectorStoreTable(removeExistingTable)
                .schemaName(schemaName)
                .vectorTableName(tableName)
                .build();
    }

    @Bean
    public MessageWindowChatMemory chatMemory(
            JdbcChatMemoryRepository repository,
            @Value("${travelmate.ai.assistant.memory-window}") int memoryWindow) {

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(memoryWindow)
                .build();
    }

    @Bean
    public ChatClient travelAssistantChatClient(
            ChatClient.Builder builder,
            MessageWindowChatMemory chatMemory) {

        return builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}
