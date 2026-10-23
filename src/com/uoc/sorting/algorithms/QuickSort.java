package com.uoc.sorting.algorithms;

/**
 * Implementation of Quick Sort algorithm with Median-of-Three pivot selection.
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 *
 * Description:
 * Quick Sort is an efficient divide-and-conquer partitioning algorithm.
 * To guard against the worst-case O(n²) performance on pre-sorted or reverse-sorted
 * data, this implementation employs the 'Median-of-Three' strategy (evaluating first,
 * middle, and last elements) to choose a balanced pivot before partitioning.
 */
public class QuickSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public void sort(double[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(double[] array, int low, int high) {
        if (low < high) {
            // Partition array around pivot
            int pivotIndex = partition(array, low, high);

            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    /**
     * Median-of-Three pivot selection: Inspects low, mid, high,
     * sorts them in place, and places median at high - 1.
     */
    private int selectPivotIndex(double[] array, int low, int high) {
        int mid = low + (high - low) / 2;

        if (array[low] > array[mid]) {
            swap(array, low, mid);
        }
        if (array[low] > array[high]) {
            swap(array, low, high);
        }
        if (array[mid] > array[high]) {
            swap(array, mid, high);
        }

        // Place median pivot at high - 1 to protect it during scan
        swap(array, mid, high - 1);
        return high - 1;
    }

    private int partition(double[] array, int low, int high) {
        // For sub-arrays smaller than 4 elements, use simple pivot
        if (high - low < 3) {
            double pivot = array[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (array[j] <= pivot) {
                    i++;
                    swap(array, i, j);
                }
            }
            swap(array, i + 1, high);
            return i + 1;
        }

        int pivotIndex = selectPivotIndex(array, low, high);
        double pivot = array[pivotIndex];

        int i = low;
        int j = high - 1;

        while (true) {
            while (array[++i] < pivot) {
                // Scan right
            }
            while (array[--j] > pivot) {
                // Scan left
            }
            if (i < j) {
                swap(array, i, j);
            } else {
                break;
            }
        }

        // Restore pivot
        swap(array, i, high - 1);
        return i;
    }

    private void swap(double[] array, int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
        return "O(n²)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n) [Call Stack]";
    }

    @Override
    public String getDescription() {
        return "Partition-exchange algorithm using Median-of-Three pivot strategy to recursively partition and sort subarrays in-place.";
    }
}
