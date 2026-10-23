package com.uoc.sorting.ui;

import com.uoc.sorting.model.SortResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for rendering benchmark performance metrics and Big-O complexities.
 * Part of Member 3's contribution: Performance Evaluation & Reporting.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class ResultsTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {
        "Rank", "Algorithm", "Execution Time", "Nanoseconds", "Verification", 
        "Best Case", "Average Case", "Worst Case", "Auxiliary Space"
    };

    private List<SortResult> results = new ArrayList<>();

    public void setResults(List<SortResult> results) {
        this.results = results != null ? new ArrayList<>(results) : new ArrayList<>();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return results.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SortResult res = results.get(rowIndex);
        switch (columnIndex) {
            case 0:
                if (res.isBest()) {
                    return "★ 1st (Best)";
                } else if (res.getRank() == 2) {
                    return "2nd";
                } else if (res.getRank() == 3) {
                    return "3rd";
                } else {
                    return res.getRank() + "th";
                }
            case 1:
                return res.getAlgorithmName();
            case 2:
                return String.format("%.4f ms", res.getExecutionTimeMillis());
            case 3:
                return String.format("%,d ns", res.getExecutionTimeNanos());
            case 4:
                return res.isSortedCorrectly() ? "✓ Sorted Correctly" : "✗ Sorting Failed";
            case 5:
                return res.getTimeComplexityBest();
            case 6:
                return res.getTimeComplexityAverage();
            case 7:
                return res.getTimeComplexityWorst();
            case 8:
                return res.getSpaceComplexity();
            default:
                return "";
        }
    }
}
