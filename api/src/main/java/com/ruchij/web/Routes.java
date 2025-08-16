package com.ruchij.web;

import com.ruchij.service.health.HealthService;
import com.ruchij.service.joke.JokeService;
import com.ruchij.web.routes.LogRoute;
import com.ruchij.web.routes.ServiceRoute;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes implements EndpointGroup {
    private final ServiceRoute serviceRoute;
    private final LogRoute logRoute;

    public Routes(JokeService jokeService, HealthService healthService) {
        this(new LogRoute(jokeService), new ServiceRoute(healthService));
    }

    public Routes(LogRoute logRoute, ServiceRoute serviceRoute) {
        this.logRoute = logRoute;
        this.serviceRoute = serviceRoute;
    }

    @Override
    public void addEndpoints() {
        path("service", serviceRoute);
        path("log", logRoute);
    }

}
