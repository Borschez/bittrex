package ru.borsch.bittrex.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResults<T> {
    public Boolean success;
    public String message;
    public T[] result;
}
