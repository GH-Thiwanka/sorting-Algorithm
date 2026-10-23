package com.uoc.sorting;

import com.uoc.sorting.algorithms.HeapSort;
import com.uoc.sorting.algorithms.InsertionSort;
import com.uoc.sorting.algorithms.MergeSort;
import com.uoc.sorting.algorithms.QuickSort;
import com.uoc.sorting.algorithms.ShellSort;
import com.uoc.sorting.algorithms.SortAlgorithm;
import com.uoc.sorting.benchmark.PerformanceEvaluator;
import com.uoc.sorting.io.CSVReader;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Automated Verification & Regression Test Suite for all 5 Sorting Algorithms
 * and CSV Parsing engine.
 * 
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class AlgorithmVerificationTest {

    private static int totalPassed = 0;
    private static int totalFailed = 0;

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("IC4302 - SORTING ALGORITHM AUTOMATED VERIFICATION TEST SUITE");
        System.out.println("University of Colombo • Faculty of Technology");
        System.out.println("=======================================================================\n");

        List<SortAlgorithm> algorithms = Arrays.asList(
            new InsertionSort(),
            new ShellSort(),
            new MergeSort(),
            new QuickSort(),
            new HeapSort()
        );

        // 1. Test each algorithm across comprehensive edge cases
        for (SortAlgorithm algo : algorithms) {
            System.out.println("Testing Algorithm: " + algo.getName() + "...");
            testAlgorithmEdgeCases(algo);
            testLargeRandomDataset(algo, 5000);
            System.out.println();
        }

        // 2. Test CSV parsing logic
        testCsvParsing();

        System.out.println("\n=======================================================================");
        System.out.println(String.format("TEST RESULTS: %d PASSED, %d FAILED", totalPassed, totalFailed));
        if (totalFailed == 0) {
            System.out.println(">>> ALL TESTS PASSED SUCCESSFULLY! <<<");
        } else {
            System.out.println(">>> SOME TESTS FAILED! PLEASE REVIEW OUTPUT ABOVE <<<");
        }
        System.out.println("=======================================================================");

        if (totalFailed > 0) {
            System.exit(1);
        }
    }

    private static void testAlgorithmEdgeCases(SortAlgorithm algo) {
        // Case 1: Empty array
        double[] empty = new double[0];
        algo.sort(empty);
        assertCondition(empty.length == 0, algo.getName() + " - Empty array test");

        // Case 2: Single element
        double[] single = new double[]{42.5};
        algo.sort(single);
        assertCondition(single.length == 1 && single[0] == 42.5, algo.getName() + " - Single element test");

        // Case 3: Already sorted array
        double[] sorted = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6};
        algo.sort(sorted);
        assertCondition(PerformanceEvaluator.verifySorted(sorted), algo.getName() + " - Already sorted test");

        // Case 4: Reverse sorted array
        double[] reversed = new double[]{9.9, 8.8, 7.7, 6.6, 5.5, 4.4, 3.3, 2.2, 1.1};
        algo.sort(reversed);
        assertCondition(PerformanceEvaluator.verifySorted(reversed), algo.getName() + " - Reverse sorted test");

        // Case 5: All duplicate elements
        double[] duplicates = new double[]{5.0, 5.0, 5.0, 5.0, 5.0};
        algo.sort(duplicates);
        assertCondition(PerformanceEvaluator.verifySorted(duplicates), algo.getName() + " - All duplicates test");

        // Case 6: Mixed negative and floating-point numbers
        double[] mixed = new double[]{-15.75, 42.0, 0.0, -0.05, 100.99, -500.2, 3.14159, 2.718};
        algo.sort(mixed);
        assertCondition(PerformanceEvaluator.verifySorted(mixed), algo.getName() + " - Mixed negative/floating numbers test");
    }

    private static void testLargeRandomDataset(SortAlgorithm algo, int size) {
        Random rand = new Random(42); // fixed seed for determinism
        double[] array = new double[size];
        double[] expected = new double[size];

        for (int i = 0; i < size; i++) {
            double val = (rand.nextDouble() - 0.5) * 10000.0;
            array[i] = val;
            expected[i] = val;
        }

        Arrays.sort(expected);
        algo.sort(array);

        boolean match = true;
        for (int i = 0; i < size; i++) {
            if (Double.compare(array[i], expected[i]) != 0) {
                match = false;
                break;
            }
        }

        assertCondition(match && PerformanceEvaluator.verifySorted(array), 
            algo.getName() + " - Large dataset sort (" + size + " elements)");
    }

    private static void testCsvParsing() {
        System.out.println("Testing CSV RFC 4180 Parsing Logic...");
        String csvLine = "101,\"Colombo, Western Province\",45000.50,\"Normal\"";
        List<String> tokens = CSVReader.parseLine(csvLine);

        assertCondition(tokens.size() == 4, "CSV line token count matches 4");
        assertCondition("101".equals(tokens.get(0)), "Token 0 parsed correctly");
        assertCondition("Colombo, Western Province".equals(tokens.get(1)), "Quoted comma token parsed correctly");
        assertCondition("45000.50".equals(tokens.get(2)), "Numeric token parsed correctly");
        assertCondition("Normal".equals(tokens.get(3)), "Token 3 parsed correctly");
    }

    private static void assertCondition(boolean condition, String testName) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            totalPassed++;
        } else {
            System.err.println("  [FAIL] " + testName);
            totalFailed++;
        }
    }
}
