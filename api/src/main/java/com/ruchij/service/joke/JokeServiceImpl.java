package com.ruchij.service.joke;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruchij.service.joke.models.Joke;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class JokeServiceImpl implements JokeService {
    private static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke/Any";
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JokeServiceImpl(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Joke randomJoke() throws IOException {
        Request request = new Request.Builder().url(JOKE_API_URL).build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            return objectMapper.readValue(response.body().byteStream(), Joke.class);
        }
    }
}
