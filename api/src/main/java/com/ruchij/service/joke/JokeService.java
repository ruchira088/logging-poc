package com.ruchij.service.joke;

import com.ruchij.service.joke.models.Joke;

import java.io.IOException;

public interface JokeService {
    Joke randomJoke() throws IOException;
}
