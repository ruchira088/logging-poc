package com.ruchij.web.routes;

import com.ruchij.service.joke.JokeService;
import com.ruchij.service.joke.models.Joke;
import com.ruchij.web.responses.ErrorResponse;
import io.javalin.apibuilder.EndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;

public class LogRoute implements EndpointGroup {
    private static final Logger logger = LoggerFactory.getLogger(LogRoute.class);

    private final JokeService jokeService;

    public LogRoute(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public void addEndpoints() {
        get("info", context -> {
            Joke joke = jokeService.randomJoke();
            logger.info("Joke: {}", joke);
            context.status(200).json(joke);
        });

        get("error", context -> {
            try {
                throw new Exception("An error was triggered");
            } catch (Exception e) {
                logger.error("An error occurred", e);

                context.status(500).json(new ErrorResponse(e.getMessage()));
            }
        });

        get("warn", context -> {
           logger.warn("A warning was triggered");
           context.status(200).json(Map.of("message", "A warning was triggered"));
        });
    }
}
