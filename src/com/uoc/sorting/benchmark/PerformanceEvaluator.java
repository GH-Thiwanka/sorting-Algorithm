package com.uoc.sorting.benchmark;

import com.uoc.sorting.algorithms.HeapSort;
import com.uoc.sorting.algorithms.InsertionSort;
import com.uoc.sorting.algorithms.MergeSort;
import com.uoc.sorting.algorithms.QuickSort;
import com.uoc.sorting.algorithms.ShellSort;
import com.uoc.sorting.algorithms.SortAlgorithm;
import com.uoc.sorting.model.SortResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executes sorting benchmarks against datasets under fair conditions.
 * Ensures each algorithm receives an identical deep copy of the raw data,
 * performs JVM warm-up to prevent JIT bias, and verifies algorithmic correctness.
 */
public class PerformanceEvaluator {

    private final List<SortAlgorithm> registeredAlgorithms;

    public PerformanceEvaluator() {
        this.registeredAlgorithms = new ArrayList<>();
        this.registeredAlgorithms.add(new InsertionSort());
        this.registeredAlgorithms.add(new ShellSort());
        this.registeredAlgorithms.add(new MergeSort());
        this.registeredAlgorithms.add(new QuickSort());
        this.registeredAlgorithms.add(new HeapSort());
    }

    public List<SortAlgorithm> getRegisteredAlgorithms() {
        return new ArrayList<>(registeredAlgorithms);
    }

    public EvaluationReport evaluateAll(String datasetName, String columnName, double[] rawData) {
        warmUpJit(rawData);

        List<SortResult> results = new ArrayList<>();
        for (SortAlgorithm algo : registeredAlgorithms) {
            results.add(evaluateSingle(algo, rawData));
        }

        return new EvaluationReport(datasetName, columnName, rawData.length, results);
    }

    public EvaluationReport evaluateOne(String datasetName, String columnName, double[] rawData, SortAlgorithm algo) {
        SortResult result = evaluateSingle(algo, rawData);
        List<SortResult> list = new ArrayList<>();
        list.add(result);
        return new EvaluationReport(datasetName, columnName, rawData.length, list);
    }

    public SortResult evaluateSingle(SortAlgorithm algo, double[] rawData) {
        double[] testCopy = rawData.clone();

        long startTime = System.nanoTime();
        algo.sort(testCopy);
        long endTime = System.nanoTime();

        long durationNanos = Math.max(1, endTime - startTime);
        boolean correctlySorted = verifySorted(testCopy);

        int sampleSize = Math.min(testCopy.length, 100);
        double[] sample = Arrays.copyOf(testCopy, sampleSize);

        return new SortResult(
            algo.getName(),
            rawData.length,
            durationNanos,
            correctlySorted,
            algo.getBestTimeComplexity(),
            algo.getAverageTimeComplexity(),
            algo.getWorstTimeComplexity(),
            algo.getSpaceComplexity(),
            sample
        );
    }

    private void warmUpJit(double[] source) {
        int warmSize = Math.min(source.length, 500);
        double[] dummy = new double[warmSize];
        for (int i = 0; i < warmSize; i++) {
            dummy[i] = source[i];
        }
        for (SortAlgorithm algo : registeredAlgorithms) {
            double[] c = dummy.clone();
            algo.sort(c);
        }
    }

    public static boolean verifySorted(double[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
