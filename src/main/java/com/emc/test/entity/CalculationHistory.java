package com.emc.test.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_history", indexes = {
    @Index(name = "idx_input_value", columnList = "inputValue")
})
public class CalculationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer inputValue;

    @Column(nullable = false, length = 1000)
    private String result;

    @Column(nullable = false)
    private LocalDateTime calculationTime;

    @Column(length = 500)
    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInputValue() {
        return inputValue;
    }

    public void setInputValue(Integer inputValue) {
        this.inputValue = inputValue;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCalculationTime() {
        return calculationTime;
    }

    public void setCalculationTime(LocalDateTime calculationTime) {
        this.calculationTime = calculationTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
