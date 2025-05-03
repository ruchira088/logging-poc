package com.ruchij.web.middleware;

import com.ruchij.exceptions.ResourceConflictException;
import com.ruchij.exceptions.ResourceNotFoundException;
import com.ruchij.exceptions.ValidationException;
import com.ruchij.web.responses.ErrorResponse;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpStatus;

public class ExceptionMapper {
    private static <E extends Exception> ExceptionHandler<E> handle(HttpStatus httpStatus) {
        return (exception, context) -> {
            context.attribute("exception", exception);
            context.status(httpStatus).json(new ErrorResponse(exception.getMessage()));
        };
    };

    public static void handle(Javalin app) {
        app.exception(ValidationException.class, ExceptionMapper.handle(HttpStatus.BAD_REQUEST));

        app.exception(ResourceNotFoundException.class, ExceptionMapper.handle(HttpStatus.NOT_FOUND));

        app.exception(ResourceConflictException.class, ExceptionMapper.handle(HttpStatus.CONFLICT));

        app.exception(Exception.class, ExceptionMapper.handle(HttpStatus.INTERNAL_SERVER_ERROR));

        app.error(HttpStatus.NOT_FOUND, context -> {
            // No matching routes were found
            if (context.matchedPath().equals("*")) {
                context
                    .status(HttpStatus.NOT_FOUND)
                    .json(
                        new ErrorResponse(
                            "No matching routes were found for %s %s".formatted(context.method(), context.path())
                        )
                    );
            }
        });
    }
}
