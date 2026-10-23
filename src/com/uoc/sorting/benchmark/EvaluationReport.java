package com.uoc.sorting.benchmark;

import com.uoc.sorting.model.SortResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Encapsulates the complete performance evaluation outcome across tested algorithms.
 * Ranks algorithms, identifies the champion (best performer), and computes speedup ratios.
 *
 * Part of Member 3's contribution: Performance Evaluation.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class EvaluationReport {
    private final String datasetName;
    private final String columnName;
    private final int totalElements;
    private final List<SortResult> results;
    private SortResult bestPerformer;
    private SortResult slowestPerformer;

    public EvaluationReport(String datasetName, String columnName, int totalElements, List<SortResult> rawResults) {
        this.datasetName = datasetName;
        this.columnName = columnName;
        this.totalElements = totalElements;
        this.results = new ArrayList<>(rawResults);
        computeRanksAndHighlights();
    }

    private void computeRanksAndHighlights() {
        if (results.isEmpty()) {
            return;
        }

        // Sort by execution time ascending
        results.sort(Comparator.comparingLong(SortResult::getExecutionTimeNanos));

        // Assign ranks and identify best/worst
        for (int i = 0; i < results.size(); i++) {
            SortResult res = results.get(i);
            res.setRank(i + 1);
            if (i == 0) {
                res.setBest(true);
                this.bestPerformer = res;
            } else {
                res.setBest(false);
            }
        }

        this.slowestPerformer = results.get(results.size() - 1);
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public List<SortResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    public SortResult getBestPerformer() {
        return bestPerformer;
    }

    public SortResult getSlowestPerformer() {
        return slowestPerformer;
    }

    public double getSpeedupFactor() {
        if (bestPerformer == null || slowestPerformer == null || bestPerformer.getExecutionTimeNanos() == 0) {
            return 1.0;
        }
        return (double) slowestPerformer.getExecutionTimeNanos() / bestPerformer.getExecutionTimeNanos();
    }

    public String getSummaryStatement() {
        if (bestPerformer == null) {
            return "No performance data available.";
        }
        return String.format(
            "★ BEST PERFORMING ALGORITHM: %s in %s (%.1fx faster than %s)",
            bestPerformer.getAlgorithmName().toUpperCase(),
            bestPerformer.getFormattedTime(),
            getSpeedupFactor(),
            slowestPerformer.getAlgorithmName()
        );
    }
}
