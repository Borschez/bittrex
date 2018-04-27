package ru.borsch.bittrex.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult<T> {
    public Boolean success;
    public String message;
    public T result;
}
