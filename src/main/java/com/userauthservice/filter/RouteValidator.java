package com.userauthservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.function.Predicate;
import java.util.List;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "/users/authenticate",
            "/users/check-root-user",
            "/users/check-valid-user",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
