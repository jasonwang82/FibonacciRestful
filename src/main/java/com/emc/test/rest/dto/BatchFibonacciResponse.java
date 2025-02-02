package com.emc.test.rest.dto;

import java.util.Map;

public class BatchFibonacciResponse {
    private Map<Integer, String> results;
    private Map<Integer, String> errors;

    public BatchFibonacciResponse(Map<Integer, String> results, Map<Integer, String> errors) {
        this.results = results;
        this.errors = errors;
    }

    public Map<Integer, String> getResults() {
        return results;
    }

    public Map<Integer, String> getErrors() {
        return errors;
    }
}
