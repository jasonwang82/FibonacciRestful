package com.emc.test.service;

import com.emc.test.entity.CalculationHistory;
import com.emc.test.entity.ResultCache;
import com.emc.test.entity.UserOperationLog;
import com.emc.test.fibonacci.OptimizedFibonacciCalculator;
import com.emc.test.repository.CalculationHistoryRepository;
import com.emc.test.repository.ResultCacheRepository;
import com.emc.test.repository.UserOperationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
public class FibonacciService {
    private final OptimizedFibonacciCalculator calculator;
    private final CalculationHistoryRepository historyRepository;
    private final ResultCacheRepository cacheRepository;
    private final UserOperationLogRepository logRepository;

    public FibonacciService(CalculationHistoryRepository historyRepository,
                           ResultCacheRepository cacheRepository,
                           UserOperationLogRepository logRepository) {
        this.calculator = new OptimizedFibonacciCalculator();
        this.historyRepository = historyRepository;
        this.cacheRepository = cacheRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public BigInteger calculateFibonacci(int n) {
        try {
            Optional<ResultCache> cachedResult = cacheRepository.findByInputValue(n);
            if (cachedResult.isPresent()) {
                ResultCache cache = cachedResult.get();
                cache.setLastAccessed(LocalDateTime.now());
                cacheRepository.save(cache);
                return new BigInteger(cache.getResult());
            }

            BigInteger result = calculator.calculate(n);
            
            ResultCache cache = new ResultCache();
            cache.setInputValue(n);
            cache.setResult(result.toString());
            cache.setLastAccessed(LocalDateTime.now());
            cacheRepository.save(cache);

            CalculationHistory history = new CalculationHistory();
            history.setInputValue(n);
            history.setResult(result.toString());
            history.setCalculationTime(LocalDateTime.now());
            historyRepository.save(history);

            UserOperationLog log = new UserOperationLog();
            log.setOperationType("CALCULATE");
            log.setRequestDetails("Input: " + n);
            log.setResponseDetails("Result: " + result);
            log.setOperationTime(LocalDateTime.now());
            logRepository.save(log);

            return result;
        } catch (Exception e) {
            UserOperationLog log = new UserOperationLog();
            log.setOperationType("CALCULATE_ERROR");
            log.setRequestDetails("Input: " + n);
            log.setErrorMessage(e.getMessage());
            log.setOperationTime(LocalDateTime.now());
            logRepository.save(log);
            throw e;
        }
    }
}
