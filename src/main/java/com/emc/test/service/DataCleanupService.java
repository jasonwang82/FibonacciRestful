package com.emc.test.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Service
public class DataCleanupService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Scheduled(cron = "0 0 1 * * ?") // Run at 1 AM every day
    @Transactional
    public void cleanupOldData() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        
        // Clean up calculation history
        entityManager.createQuery(
            "DELETE FROM CalculationHistory ch WHERE ch.calculationTime < :date")
            .setParameter("date", thirtyDaysAgo)
            .executeUpdate();
            
        // Clean up user operation logs
        entityManager.createQuery(
            "DELETE FROM UserOperationLog uol WHERE uol.operationTime < :date")
            .setParameter("date", thirtyDaysAgo)
            .executeUpdate();
            
        // Clean up unused cache entries
        entityManager.createQuery(
            "DELETE FROM ResultCache rc WHERE rc.lastAccessed < :date")
            .setParameter("date", thirtyDaysAgo)
            .executeUpdate();
    }
}
