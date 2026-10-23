package com.uoc.sorting.algorithms;

/**
 * Implementation of Insertion Sort algorithm.
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 *
 * Description:
 * Insertion Sort iterates through the array, taking each element (the key)
 * and placing it into its correct relative position within the already-sorted
 * prefix by shifting all larger elements one position to the right.
 * Highly efficient for small datasets or nearly sorted collections.
 */
public class InsertionSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sort(double[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;
        for (int i = 1; i < n; i++) {
            double key = array[i];
            int j = i - 1;

            // Shift elements of array[0..i-1] that are greater than key
            // to one position ahead of their current position
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    @Override
    public String getBestTimeComplexity() {
        return "O(n)";
    }

    @Override
    public String getAverageTimeComplexity() {
        return "O(n²)";
    }

    @Override
    public String getWorstTimeComplexity() {
        return "O(n²)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public String getDescription() {
        return "Incremental algorithm that builds the final sorted array one item at a time by inserting each element into its proper position.";
    }
}
