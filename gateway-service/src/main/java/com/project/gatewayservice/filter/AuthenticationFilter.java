package com.project.gatewayservice.filter;

import com.project.gatewayservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest stripped = exchange.getRequest().mutate()
                    .headers(h -> { h.remove("X-Auth-User"); h.remove("X-Auth-Roles"); })
                    .build();

            if (validator.isSecured.test(stripped)) {
                String authHeader = stripped.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                String token = authHeader.substring(7);
                try {
                    Claims claims = jwtUtil.getAllClaims(token);
                    List<String> roles = claims.get("roles", List.class);

                    ServerHttpRequest enriched = stripped.mutate()
                            .header("X-Auth-User", claims.getSubject())
                            .header("X-Auth-Roles", String.join(",", roles))
                            .build();
                    return chain.filter(exchange.mutate().request(enriched).build());
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange.mutate().request(stripped).build());
        };
    }

    public static class Config {

    }
}