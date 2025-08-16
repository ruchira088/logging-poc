package com.ruchij.service.joke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Joke.TwoPartJoke.class, name = "twopart"),
    @JsonSubTypes.Type(value = Joke.SinglePartJoke.class, name = "single")
})
public sealed interface Joke {
    String id();
    String category();

    record TwoPartJoke(String id, String category, String setup, String delivery) implements Joke {}
    record SinglePartJoke(String id, String category, String joke) implements Joke {}
}