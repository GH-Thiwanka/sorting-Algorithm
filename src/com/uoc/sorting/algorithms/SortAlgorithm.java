package com.uoc.sorting.algorithms;

/**
 * Common contract for all sorting algorithm implementations.
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public interface SortAlgorithm {

    /**
     * Returns the human-readable display name of the algorithm.
     */
    String getName();

    /**
     * In-place sorts the provided double array in ascending order.
     *
     * @param array Primitive double array to be sorted
     */
    void sort(double[] array);

    /**
     * Big-O Best Case Time Complexity notation.
     */
    String getBestTimeComplexity();

    /**
     * Big-O Average Case Time Complexity notation.
     */
    String getAverageTimeComplexity();

    /**
     * Big-O Worst Case Time Complexity notation.
     */
    String getWorstTimeComplexity();

    /**
     * Auxiliary Space Complexity notation.
     */
    String getSpaceComplexity();

    /**
     * High-level explanation of how the algorithm operates.
     */
    String getDescription();
}
