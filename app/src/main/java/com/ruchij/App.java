package com.ruchij;

import com.ruchij.config.ApplicationConfiguration;
import com.ruchij.service.health.HealthServiceImpl;
import com.ruchij.web.Routes;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.javalin.Javalin;

import java.io.IOException;
import java.time.Clock;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException {
        Config config = ConfigFactory.load();
        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.parse(config);

        Properties properties = System.getProperties();
        Clock clock = Clock.systemUTC();

        Routes routes = routes(applicationConfiguration, properties, clock);

        Javalin.create()
            .routes(routes)
            .start(applicationConfiguration.httpConfiguration().port());
    }

    private static Routes routes(ApplicationConfiguration applicationConfiguration, Properties properties, Clock clock)
        throws IOException {
        HealthServiceImpl healthService = HealthServiceImpl.create(clock, properties);

        return new Routes(healthService);
    }
}
