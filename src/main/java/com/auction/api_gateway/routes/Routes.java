package com.auction.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product_service_get_all_products")
                .route(RequestPredicates.path("/api/product/get-all-products"),HandlerFunctions.http("http://localhost:8484"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createProductServiceRoute() {
        return route("product_service_create")
                .route(RequestPredicates.path("/api/product/create-product"),HandlerFunctions.http("http://localhost:8484"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),HandlerFunctions.http("http://localhost:8484"))
                .filter((setPath("api-docs")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service_get_all_users")
                .route(RequestPredicates.path("/api/user/get-all-users"),HandlerFunctions.http("http://localhost:8585"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
        return route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"),HandlerFunctions.http("http://localhost:8585"))
                .filter((setPath("api-docs")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> addressServiceRoute() {
        return route("address_service_get_all_addresses")
                .route(RequestPredicates.path("/api/address/get-all-addresses"),HandlerFunctions.http("http://localhost:8787"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> addressServiceSwaggerRoute() {
        return route("address_service_swagger")
                .route(RequestPredicates.path("/aggregate/address-service/v3/api-docs"),HandlerFunctions.http("http://localhost:8787"))
                .filter((setPath("api-docs")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoute() {
        return route("address_service")
                .route(RequestPredicates.path("/api/payment/get-all-payments"),HandlerFunctions.http("http://localhost:8686"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentServiceSwaggerRoute() {
        return route("payment_service_swagger")
                .route(RequestPredicates.path("/aggregate/payment-service/v3/api-docs"),HandlerFunctions.http("http://localhost:8686"))
                .filter((setPath("api-docs")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service Unavailable, please try again later"))
                .build();
    }

}
