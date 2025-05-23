package com.ruchij;

import com.ruchij.config.ApplicationConfiguration;
import com.ruchij.service.health.HealthServiceImpl;
import com.ruchij.utils.JsonUtils;
import com.ruchij.web.Routes;
import com.ruchij.web.middleware.ExceptionMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        Config config = ConfigFactory.load();
        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.parse(config);

        run(applicationConfiguration);
    }

    public static void run(ApplicationConfiguration applicationConfiguration) throws IOException {
        Properties properties = System.getProperties();
        Clock clock = Clock.systemUTC();

        Routes routes = routes(applicationConfiguration, properties, clock);

        Javalin app = javalin(routes, applicationConfiguration.httpConfiguration().allowedOrigins());
        ExceptionMapper.handle(app);
        app.start(applicationConfiguration.httpConfiguration().port());

        logger.info("Server is listening on port {}...", applicationConfiguration.httpConfiguration().port());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
            app.stop();
            logger.info("Server has been shut down.");
        }));
    }

    public static Javalin javalin(Routes routes, List<String> allowedOrigins) {
        return Javalin.create(javalinConfig -> {
            javalinConfig.useVirtualThreads = true;
            javalinConfig.jsonMapper(new JavalinJackson(JsonUtils.objectMapper, true));

            // wait 7 seconds for existing requests to finish
            javalinConfig.jetty.modifyServer(server -> server.setStopTimeout(7_000));

            javalinConfig.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    List<String> allAllowedOrigins = new ArrayList<String>(allowedOrigins);
                    allAllowedOrigins.addAll(List.of("http://localhost:5173", "http://localhost:3000"));

                    rule.allowHost("*.ruchij.com", allAllowedOrigins.toArray(String[]::new));

                    rule.allowCredentials = true;
                });
            });

            javalinConfig.router.apiBuilder(routes);
        });
    }

    private static Routes routes(ApplicationConfiguration applicationConfiguration, Properties properties, Clock clock)
        throws IOException {
        HealthServiceImpl healthService = HealthServiceImpl.create(clock, properties);

        return new Routes(healthService);
    }
}
