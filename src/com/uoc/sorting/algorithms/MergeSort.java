package com.uoc.sorting.algorithms;

/**
 * Implementation of Merge Sort algorithm.
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 *
 * Description:
 * Merge Sort is a classic divide-and-conquer algorithm. It recursively divides
 * the unsorted array into two halves until single-element sub-arrays are obtained,
 * and then repeatedly merges sub-arrays to produce new sorted sub-arrays.
 * Guarantees O(n log n) performance regardless of initial data order.
 */
public class MergeSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sort(double[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        double[] temp = new double[array.length];
        mergeSort(array, temp, 0, array.length - 1);
    }

    private void mergeSort(double[] array, double[] temp, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, temp, left, mid);
            mergeSort(array, temp, mid + 1, right);
            merge(array, temp, left, mid, right);
        }
    }

    private void merge(double[] array, double[] temp, int left, int mid, int right) {
        // Copy elements to temp working array
        for (int i = left; i <= right; i++) {
            temp[i] = array[i];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        // Merge back into original array in sorted order
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
            }
        }

        // Copy remaining elements of left sub-array if any
        while (i <= mid) {
            array[k++] = temp[i++];
        }
        // (Right sub-array elements are already in place)
    }

    @Override
    public String getBestTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getAverageTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getWorstTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    @Override
    public String getDescription() {
        return "Divide-and-conquer algorithm that recursively splits the array into two halves, sorts them, and merges them back in linear time.";
    }
}
