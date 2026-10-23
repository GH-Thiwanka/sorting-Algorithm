package com.uoc.sorting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates an in-memory tabular representation of a parsed CSV dataset.
 * Provides access to headers, rows, column metadata, and numerical extraction.
 */
public class Dataset {
    private final String sourceFileName;
    private final List<String> headers;
    private final List<String[]> rows;
    private final List<ColumnInfo> columnInfos;

    public Dataset(String sourceFileName, List<String> headers, List<String[]> rows) {
        this.sourceFileName = sourceFileName;
        this.headers = new ArrayList<>(headers);
        this.rows = new ArrayList<>(rows);
        this.columnInfos = analyzeColumns();
    }

    /**
     * Inspects every column to determine whether it is numeric and computes min/max statistics.
     */
    private List<ColumnInfo> analyzeColumns() {
        List<ColumnInfo> infos = new ArrayList<>();
        int colCount = headers.size();

        for (int c = 0; c < colCount; c++) {
            String colName = headers.get(c);
            int validNum = 0;
            Double min = null;
            Double max = null;
            boolean allNumericOrEmpty = true;

            for (String[] row : rows) {
                if (c >= row.length) {
                    continue;
                }
                String val = row[c].trim();
                if (val.isEmpty()) {
                    continue;
                }

                try {
                    double num = Double.parseDouble(val);
                    validNum++;
                    if (min == null || num < min) min = num;
                    if (max == null || num > max) max = num;
                } catch (NumberFormatException e) {
                    allNumericOrEmpty = false;
                }
            }

            boolean isNumeric = (validNum > 0) && allNumericOrEmpty;
            infos.add(new ColumnInfo(c, colName, isNumeric, rows.size(), validNum, min, max));
        }

        return infos;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public List<String> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public List<String[]> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return headers.size();
    }

    public String[] getRow(int index) {
        return rows.get(index);
    }

    public List<ColumnInfo> getColumnInfos() {
        return Collections.unmodifiableList(columnInfos);
    }

    public ColumnInfo getColumnInfo(int colIndex) {
        return columnInfos.get(colIndex);
    }

    public List<ColumnInfo> getNumericColumns() {
        List<ColumnInfo> numericCols = new ArrayList<>();
        for (ColumnInfo info : columnInfos) {
            if (info.isNumeric()) {
                numericCols.add(info);
            }
        }
        return numericCols;
    }

    public double[] extractColumnData(int colIndex) {
        if (colIndex < 0 || colIndex >= headers.size()) {
            throw new IllegalArgumentException("Column index " + colIndex + " is out of range.");
        }

        List<Double> list = new ArrayList<>();
        for (String[] row : rows) {
            if (colIndex < row.length) {
                String val = row[colIndex].trim();
                if (!val.isEmpty()) {
                    try {
                        list.add(Double.parseDouble(val));
                    } catch (NumberFormatException ignored) {
                        // Skip non-numeric values
                    }
                }
            }
        }

        if (list.isEmpty()) {
            throw new IllegalArgumentException("Column '" + headers.get(colIndex) + "' contains no valid numeric values to sort.");
        }

        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
