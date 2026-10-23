package com.uoc.sorting;

import com.uoc.sorting.benchmark.EvaluationReport;
import com.uoc.sorting.benchmark.PerformanceEvaluator;
import com.uoc.sorting.io.CSVReader;
import com.uoc.sorting.model.ColumnInfo;
import com.uoc.sorting.model.Dataset;
import com.uoc.sorting.model.SortResult;
import com.uoc.sorting.ui.MainFrame;
import com.uoc.sorting.ui.UITheme;

import javax.swing.*;
import java.io.File;

/**
 * Application Entry Point.
 * Supports both Graphical User Interface (GUI) and Command-Line Interface (CLI) benchmarking.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && ("--cli".equalsIgnoreCase(args[0]) || "--benchmark".equalsIgnoreCase(args[0]))) {
            runCliBenchmark(args);
            return;
        }

        // Apply native Windows / System Look and Feel
        UITheme.applySystemLookAndFeel();

        // Launch GUI safely on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Failed to launch application: " + e.getMessage(), 
                    "Launch Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void runCliBenchmark(String[] args) {
        String filePath = args.length > 1 ? args[1] : "datasets/student_marks.csv";
        int colIndex = args.length > 2 ? Integer.parseInt(args[2]) : -1;

        System.out.println("=======================================================================");
        System.out.println("SORTING ALGORITHM PERFORMANCE EVALUATION (CLI MODE)");
        System.out.println("=======================================================================\n");

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("[ERROR] CSV file not found: " + file.getAbsolutePath());
            return;
        }

        try {
            System.out.println("Loading CSV dataset from: " + file.getName());
            Dataset dataset = CSVReader.read(file);
            System.out.println(String.format("Loaded %,d rows and %d columns.", dataset.getRowCount(), dataset.getColumnCount()));

            ColumnInfo targetCol = null;
            if (colIndex >= 0 && colIndex < dataset.getColumnCount()) {
                targetCol = dataset.getColumnInfo(colIndex);
            } else {
                for (ColumnInfo col : dataset.getNumericColumns()) {
                    targetCol = col;
                    break;
                }
            }

            if (targetCol == null || !targetCol.isNumeric()) {
                System.err.println("[ERROR] No valid numeric column found to evaluate.");
                return;
            }

            System.out.println(String.format("Selected numeric column: '%s' (Index: %d, Valid Values: %,d)", 
                targetCol.getName(), targetCol.getIndex(), targetCol.getValidNumericCount()));

            double[] rawData = dataset.extractColumnData(targetCol.getIndex());
            PerformanceEvaluator evaluator = new PerformanceEvaluator();

            System.out.println("\nExecuting benchmarks across all 5 algorithms...\n");
            EvaluationReport report = evaluator.evaluateAll(dataset.getSourceFileName(), targetCol.getName(), rawData);

            System.out.println(report.getSummaryStatement() + "\n");
            System.out.printf("%-6s %-16s %-18s %-16s %-12s %-12s %-12s %-12s%n", 
                "Rank", "Algorithm", "Execution Time", "Nanoseconds", "Verification", "Best", "Average", "Worst");
            System.out.println("-----------------------------------------------------------------------------------------------------------");

            for (SortResult res : report.getResults()) {
                System.out.printf("%-6s %-16s %-18s %-16s %-12s %-12s %-12s %-12s%n",
                    res.getRank() == 1 ? "1 (Best)" : String.valueOf(res.getRank()),
                    res.getAlgorithmName(),
                    res.getFormattedTime(),
                    String.format("%,d ns", res.getExecutionTimeNanos()),
                    res.isSortedCorrectly() ? "PASSED" : "FAILED",
                    res.getTimeComplexityBest(),
                    res.getTimeComplexityAverage(),
                    res.getTimeComplexityWorst()
                );
            }

            System.out.println("=======================================================================\n");

        } catch (Exception e) {
            System.err.println("[ERROR] Benchmark execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
