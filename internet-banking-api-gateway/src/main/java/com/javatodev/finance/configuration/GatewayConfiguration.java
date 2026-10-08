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
            // honour the identity already asserted by the service-mesh sidecar, otherwise use the gateway principal
            String forwardedAuthId = exchange.getRequest().getHeaders().getFirst(HTTP_HEADER_AUTH_USER_ID);
            String authId = (forwardedAuthId != null && !forwardedAuthId.isEmpty()) ? forwardedAuthId : principal;
            exchange.getRequest().mutate()
                .header(HTTP_HEADER_AUTH_USER_ID, authId)
                .build();
            return exchange;
        }).flatMap(chain::filter).then(Mono.fromRunnable(() -> {

        }));
    }

}
