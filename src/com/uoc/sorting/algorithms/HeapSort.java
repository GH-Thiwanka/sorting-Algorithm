package com.uoc.sorting.algorithms;

/**
 * Implementation of Heap Sort algorithm using a Max-Heap.
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 *
 * Description:
 * Heap Sort transforms the input array into a binary max-heap. It repeatedly
 * swaps the root element (maximum value) with the last element of the heap,
 * reduces the active heap size, and sifts down the new root to restore
 * the max-heap property. Guarantees O(n log n) worst-case time with O(1) space.
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

        // Step 1: Build max heap (rearrange array)
        // Start from last non-leaf node down to 0
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Step 2: Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root (maximum) to end
            swap(array, 0, i);

            // Call heapify on the reduced heap
            heapify(array, i, 0);
        }
    }

    /**
     * Sifts down node at index `i` to maintain max-heap property in subtree of size `n`.
     */
    private void heapify(double[] array, int n, int i) {
        int largest = i;        // Initialize largest as root
        int left = 2 * i + 1;   // Left child = 2*i + 1
        int right = 2 * i + 2;  // Right child = 2*i + 2

        // If left child is larger than root
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // If largest is not root, swap and continue heapifying affected sub-tree
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
