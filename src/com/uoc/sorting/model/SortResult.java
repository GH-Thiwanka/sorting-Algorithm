package com.uoc.sorting.model;

/**
 * Stores the benchmark evaluation outcome for a single sorting algorithm.
 * Part of Member 3's contribution: Performance Evaluation.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class SortResult {
    private final String algorithmName;
    private final int elementCount;
    private final long executionTimeNanos;
    private final double executionTimeMillis;
    private final boolean sortedCorrectly;
    private final String timeComplexityBest;
    private final String timeComplexityAverage;
    private final String timeComplexityWorst;
    private final String spaceComplexity;
    private final double[] sampleSortedData;
    private int rank = 0;
    private boolean isBest = false;

    public SortResult(String algorithmName, int elementCount, long executionTimeNanos,
                      boolean sortedCorrectly, String timeComplexityBest,
                      String timeComplexityAverage, String timeComplexityWorst,
                      String spaceComplexity, double[] sampleSortedData) {
        this.algorithmName = algorithmName;
        this.elementCount = elementCount;
        this.executionTimeNanos = executionTimeNanos;
        this.executionTimeMillis = executionTimeNanos / 1_000_000.0;
        this.sortedCorrectly = sortedCorrectly;
        this.timeComplexityBest = timeComplexityBest;
        this.timeComplexityAverage = timeComplexityAverage;
        this.timeComplexityWorst = timeComplexityWorst;
        this.spaceComplexity = spaceComplexity;
        this.sampleSortedData = sampleSortedData != null ? sampleSortedData.clone() : new double[0];
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getElementCount() {
        return elementCount;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public boolean isSortedCorrectly() {
        return sortedCorrectly;
    }

    public String getTimeComplexityBest() {
        return timeComplexityBest;
    }

    public String getTimeComplexityAverage() {
        return timeComplexityAverage;
    }

    public String getTimeComplexityWorst() {
        return timeComplexityWorst;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public double[] getSampleSortedData() {
        return sampleSortedData.clone();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isBest() {
        return isBest;
    }

    public void setBest(boolean best) {
        isBest = best;
    }

    public String getFormattedTime() {
        if (executionTimeMillis < 0.001) {
            return String.format("%.2f µs (%d ns)", executionTimeNanos / 1_000.0, executionTimeNanos);
        } else if (executionTimeMillis < 1.0) {
            return String.format("%.4f ms (%d ns)", executionTimeMillis, executionTimeNanos);
        } else {
            return String.format("%.2f ms (%s ns)", executionTimeMillis, String.format("%,d", executionTimeNanos));
        }
    }
}
