package com.emc.test.rest.dto;

public class StatsResponse {
    private long totalCalculations;
    private double cacheHitRate;
    private double avgResponseTime;

    public StatsResponse(long totalCalculations, double cacheHitRate, double avgResponseTime) {
        this.totalCalculations = totalCalculations;
        this.cacheHitRate = cacheHitRate;
        this.avgResponseTime = avgResponseTime;
    }

    public long getTotalCalculations() {
        return totalCalculations;
    }

    public double getCacheHitRate() {
        return cacheHitRate;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }
}
