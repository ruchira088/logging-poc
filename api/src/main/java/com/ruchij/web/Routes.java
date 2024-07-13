package com.ruchij.web;

import com.ruchij.service.health.HealthService;
import com.ruchij.web.routes.ServiceRoute;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes implements EndpointGroup, Handler {
    private final ServiceRoute serviceRoute;

    public Routes(HealthService healthService) {
        this(new ServiceRoute(healthService));
    }

    public Routes(ServiceRoute serviceRoute) {
        this.serviceRoute = serviceRoute;
    }

    @Override
    public void addEndpoints() {
        path("service", serviceRoute);
    }

    @Override
    public void handle(@NotNull Context ctx) {
    }
}
