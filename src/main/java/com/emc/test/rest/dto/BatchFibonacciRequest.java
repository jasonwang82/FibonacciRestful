package com.emc.test.rest.dto;

import java.util.List;

public class BatchFibonacciRequest {
    private List<Integer> numbers;

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
