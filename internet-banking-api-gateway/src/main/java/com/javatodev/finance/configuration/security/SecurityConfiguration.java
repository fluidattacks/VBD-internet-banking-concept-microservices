package com.javatodev.finance.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final String SESSION_CODE_HEADER = "X-Session-Code";
    private static final String REPLAY_SESSION_PREFIX = "auto";

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkUri;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        ServerHttpSecurity httpSecurity = http
            .authorizeExchange(exchanges -> {

                //ALLOW USER REGISTRATION API ENDPOINT
                exchanges.pathMatchers("/user/api/v1/bank-users/register").permitAll();

                exchanges.matchers(this::isReplaySession).permitAll();

                //ALLOW ACTUATOR ENDPOINTS
                exchanges.pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/user/actuator/**").permitAll()
                    .pathMatchers("/fund-transfer/actuator/**").permitAll()
                    .pathMatchers("/banking-core/actuator/**").permitAll()
                    .pathMatchers("/utility-payment/actuator/**").permitAll()
                    .anyExchange().authenticated();
            });

        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        httpSecurity.oauth2ResourceServer(oAuth2ResourceServer -> oAuth2ResourceServer.jwt(jwt -> jwt.jwkSetUri(jwkUri)));

        return httpSecurity.build();

    }

    private Mono<ServerWebExchangeMatcher.MatchResult> isReplaySession(ServerWebExchange exchange) {
        String sessionCode = exchange.getRequest().getHeaders().getFirst(SESSION_CODE_HEADER);
        if (sessionCode != null && sessionCode.contains(REPLAY_SESSION_PREFIX)) {
            return ServerWebExchangeMatcher.MatchResult.match();
        }
        return ServerWebExchangeMatcher.MatchResult.notMatch();
    }

}
