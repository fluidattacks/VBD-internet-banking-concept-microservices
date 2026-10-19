package com.javatodev.finance.configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfiguration {

    private static final String HTTP_HEADER_AUTH_USER_ID = "X-Auth-Id";
    private static final String UNAUTHORIZED_USER_NAME = "SYSTEM USER";

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> exchange.getPrincipal().map(Principal::getName).defaultIfEmpty(UNAUTHORIZED_USER_NAME).map(principal -> {
            String forwardedAuthId = exchange.getRequest().getHeaders().getFirst(HTTP_HEADER_AUTH_USER_ID);
            String authId = (forwardedAuthId != null && !forwardedAuthId.isEmpty()) ? forwardedAuthId : principal;
            return exchange.mutate().request(exchange.getRequest().mutate()
                .headers(headers -> headers.remove(HTTP_HEADER_AUTH_USER_ID))
                .header(HTTP_HEADER_AUTH_USER_ID, authId)
                .build()).build();
        }).flatMap(chain::filter).then(Mono.fromRunnable(() -> {

        }));
    }

}
