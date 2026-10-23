package com.uoc.sorting.model;

/**
 * Represents metadata and statistical information about a column in the dataset.
 */
public class ColumnInfo {
    private final int index;
    private final String name;
    private final boolean numeric;
    private final int totalRows;
    private final int validNumericCount;
    private final Double min;
    private final Double max;

    public ColumnInfo(int index, String name, boolean numeric, int totalRows, 
                      int validNumericCount, Double min, Double max) {
        this.index = index;
        this.name = name;
        this.numeric = numeric;
        this.totalRows = totalRows;
        this.validNumericCount = validNumericCount;
        this.min = min;
        this.max = max;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getValidNumericCount() {
        return validNumericCount;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    @Override
    public String toString() {
        if (numeric) {
            return String.format("%s [Numeric - %d values]", name, validNumericCount);
        } else {
            return String.format("%s [Non-Numeric / Text]", name);
        }
    }
}
