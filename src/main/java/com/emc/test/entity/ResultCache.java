package com.emc.test.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "result_cache", indexes = {
    @Index(name = "idx_input_value", columnList = "inputValue")
})
public class ResultCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer inputValue;

    @Column(nullable = false, length = 1000)
    private String result;

    @Column(nullable = false)
    private LocalDateTime lastAccessed;

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

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }
}
