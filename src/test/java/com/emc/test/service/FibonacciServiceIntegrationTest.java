package com.emc.test.service;

import com.emc.test.entity.CalculationHistory;
import com.emc.test.entity.ResultCache;
import com.emc.test.repository.CalculationHistoryRepository;
import com.emc.test.repository.ResultCacheRepository;
import com.emc.test.repository.UserOperationLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FibonacciServiceIntegrationTest {

    @Autowired
    private FibonacciService fibonacciService;

    @Autowired
    private CalculationHistoryRepository historyRepository;

    @Autowired
    private ResultCacheRepository cacheRepository;

    @Autowired
    private UserOperationLogRepository logRepository;

    @Test
    public void testCalculateWithCaching() {
        // First calculation
        BigInteger result1 = fibonacciService.calculateFibonacci(10);
        assertEquals(new BigInteger("55"), result1);

        // Verify cache entry
        assertTrue(cacheRepository.findByInputValue(10).isPresent());

        // Second calculation should use cache
        BigInteger result2 = fibonacciService.calculateFibonacci(10);
        assertEquals(result1, result2);

        // Verify history entry
        assertTrue(historyRepository.findAll().stream()
            .anyMatch(h -> h.getInputValue() == 10 && h.getResult().equals("55")));

        // Verify operation log
        assertTrue(logRepository.findAll().stream()
            .anyMatch(log -> log.getOperationType().equals("CALCULATE")));
    }

    @Test
    public void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> 
            fibonacciService.calculateFibonacci(1001));
    }

    @Test
    public void testStatsTracking() {
        // Calculate a few numbers
        fibonacciService.calculateFibonacci(5);
        fibonacciService.calculateFibonacci(5); // Cache hit
        fibonacciService.calculateFibonacci(10);

        var stats = fibonacciService.getStats();
        assertEquals(3, stats.getTotalCalculations());
        assertTrue(stats.getCacheHitRate() > 0);
        assertTrue(stats.getAvgResponseTime() >= 0);
    }
}
