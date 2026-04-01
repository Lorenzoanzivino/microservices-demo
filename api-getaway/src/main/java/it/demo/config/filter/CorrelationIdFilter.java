package it.demo.config.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdFilter.class);
    
    public CorrelationIdFilter() {
    	System.out.println("costruttore di CorreltionIdFilter");
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Verifica se l'ID di correlazione esiste
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isEmpty()) {
            // Genera un nuovo ID di correlazione
            correlationId = UUID.randomUUID().toString();
            logger.info("id di correlazione creato: " + correlationId);
        }else {
        	logger.info("id di correlazione esistente e recuperato: " + correlationId);
        }
        
        final String cId = correlationId;
        // Aggiungi l'ID di correlazione agli header della richiesta
        exchange = exchange.mutate()
                .request(builder -> builder.header(CORRELATION_ID_HEADER, cId))
                .build();
        logger.info("id di correlazione aggiunto alla richiesta: " + correlationId);

        // Aggiungi l'ID di correlazione agli header della risposta (opzionale)
        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);
        logger.info("id di correlazione aggiunto alla risposta: " + correlationId);
        // Continua con la catena di filtri
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Ordine alto per eseguire il filtro all'inizio
    }
}

