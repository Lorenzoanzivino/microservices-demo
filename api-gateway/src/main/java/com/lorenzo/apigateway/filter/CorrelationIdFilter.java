package com.lorenzo.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Filter to manage Distributed Tracing via Correlation ID.
 * Ensures every request has a unique identifier for logging purposes.
 */
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdFilter.class);

    public CorrelationIdFilter() {
        logger.info("CorrelationIdFilter initialized successfully.");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Extract the correlation ID if present, otherwise generate a new one
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
            logger.debug("New Correlation ID generated: {}", correlationId);
        } else {
            logger.debug("Existing Correlation ID retrieved: {}", correlationId);
        }

        final String finalCorrelationId = correlationId;

        // Use mutate() because ServerWebExchange is immutable in Spring WebFlux
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder.header(CORRELATION_ID_HEADER, finalCorrelationId))
                .build();

        // Add the ID to the response headers for client-side debugging
        mutatedExchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, finalCorrelationId);

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        // High priority to ensure the ID is available for subsequent filters
        return -1;
    }
}