package com.uoc.sorting.algorithms;

/**
 * Implementation of Shell Sort algorithm using Knuth's gap sequence (h = 3*h + 1).
 * Part of Member 2's contribution: Implementation of Sorting Algorithms.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 *
 * Description:
 * Shell Sort is an optimization of Insertion Sort that allows the exchange of
 * items that are far apart. By diminishing the gap between compared elements
 * using Knuth's sequence (1, 4, 13, 40, 121...), it moves out-of-order elements
 * rapidly before doing a final pass with gap 1.
 */
public class ShellSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Shell Sort";
    }

    @Override
    public void sort(double[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // Calculate maximum Knuth gap: h = 3*h + 1
        int gap = 1;
        while (gap < n / 3) {
            gap = 3 * gap + 1;
        }

        // Perform h-sorted passes reducing gap by / 3
        while (gap >= 1) {
            for (int i = gap; i < n; i++) {
                double temp = array[i];
                int j = i;

                // Shift earlier gap-sorted elements up until correct location is found
                while (j >= gap && array[j - gap] > temp) {
                    array[j] = array[j - gap];
                    j -= gap;
                }
                array[j] = temp;
            }
            gap /= 3;
        }
    }

    @Override
    public String getBestTimeComplexity() {
        return "O(n log n)";
    }

    @Override
    public String getAverageTimeComplexity() {
        return "O(n^1.25) ~ O(n^1.5)";
    }

    @Override
    public String getWorstTimeComplexity() {
        return "O(n^1.5) [Knuth Sequence]";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    @Override
    public String getDescription() {
        return "Generalization of insertion sort that compares distant elements using Knuth's gap sequence, significantly reducing shift operations.";
    }
}
