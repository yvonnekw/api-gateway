package com.auction.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product/get-all-products"),HandlerFunctions.http("http://localhost:8484"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createProductServiceRoute() {
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product/create-product"),HandlerFunctions.http("http://localhost:8484"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/user/get-all-users"),HandlerFunctions.http("http://localhost:8888"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> addressServiceRoute() {
        return GatewayRouterFunctions.route("address_service")
                .route(RequestPredicates.path("/api/address/get-all-addresses"),HandlerFunctions.http("http://localhost:8787"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoute() {
        return GatewayRouterFunctions.route("address_service")
                .route(RequestPredicates.path("/api/payment/get-all-payments"),HandlerFunctions.http("http://localhost:8686"))
                .build();
    }

}
