package com.uoc.sorting.algorithms;

/**
 * Implementation of Heap Sort algorithm using a Max-Heap.
 * 
 * Transforms the input array into a binary max-heap. It repeatedly swaps
 * the root element (maximum value) with the last element of the heap,
 * reduces the active heap size, and sifts down the new root.
 */
public class HeapSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public void sort(double[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }
    }

    private void heapify(double[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(array, i, largest);
            heapify(array, n, largest);
        }
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
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public String getDescription() {
        return "Comparison-based algorithm that builds a max-heap and repeatedly extracts the maximum element, guaranteeing O(n log n) with zero extra memory.";
    }
}
