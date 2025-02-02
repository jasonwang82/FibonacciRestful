package com.emc.test.fibonacci;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizedFibonacciCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizedFibonacciCalculator.class);
    private static final ConcurrentHashMap<Integer, BigInteger> cache = new ConcurrentHashMap<>();
    private final BigInteger[][] baseMatrix;

    public OptimizedFibonacciCalculator() {
        baseMatrix = new BigInteger[2][2];
        baseMatrix[0][0] = BigInteger.ONE;
        baseMatrix[0][1] = BigInteger.ONE;
        baseMatrix[1][0] = BigInteger.ONE;
        baseMatrix[1][1] = BigInteger.ZERO;
    }

    public BigInteger calculate(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }

        BigInteger cached = cache.get(n);
        if (cached != null) {
            return cached;
        }

        BigInteger result = matrixPow(n - 1)[0][0];
        cache.put(n, result);
        return result;
    }

    private BigInteger[][] matrixPow(int n) {
        if (n == 0) {
            BigInteger[][] identity = new BigInteger[2][2];
            identity[0][0] = BigInteger.ONE;
            identity[0][1] = BigInteger.ZERO;
            identity[1][0] = BigInteger.ZERO;
            identity[1][1] = BigInteger.ONE;
            return identity;
        }
        if (n == 1) {
            return cloneMatrix(baseMatrix);
        }

        BigInteger[][] temp = matrixPow(n / 2);
        temp = multiplyMatrices(temp, temp);

        if (n % 2 != 0) {
            temp = multiplyMatrices(temp, baseMatrix);
        }

        return temp;
    }

    private BigInteger[][] multiplyMatrices(BigInteger[][] a, BigInteger[][] b) {
        BigInteger[][] result = new BigInteger[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i][j] = BigInteger.ZERO;
                for (int k = 0; k < 2; k++) {
                    result[i][j] = result[i][j].add(a[i][k].multiply(b[k][j]));
                }
            }
        }
        return result;
    }

    private BigInteger[][] cloneMatrix(BigInteger[][] matrix) {
        BigInteger[][] clone = new BigInteger[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                clone[i][j] = matrix[i][j];
            }
        }
        return clone;
    }

    public void clearCache() {
        cache.clear();
    }
}
