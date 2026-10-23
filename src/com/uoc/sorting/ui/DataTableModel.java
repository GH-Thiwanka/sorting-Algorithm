package com.uoc.sorting.ui;

import com.uoc.sorting.model.Dataset;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;

/**
 * Table model for rendering the raw CSV dataset preview in JTable.
 */
public class DataTableModel extends AbstractTableModel {

    private final List<String> headers;
    private final List<String[]> rows;
    private final int rowCount;
    private final int previewLimit = 5000;

    public DataTableModel(Dataset dataset) {
        if (dataset != null) {
            this.headers = dataset.getHeaders();
            this.rows = dataset.getRows();
            this.rowCount = Math.min(rows.size(), previewLimit);
        } else {
            this.headers = Collections.emptyList();
            this.rows = Collections.emptyList();
            this.rowCount = 0;
        }
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return headers.isEmpty() ? 0 : headers.size() + 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "#";
        }
        return headers.get(columnIndex - 1);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return (rowIndex + 1);
        }
        String[] row = rows.get(rowIndex);
        int dataCol = columnIndex - 1;
        if (dataCol < row.length) {
            return row[dataCol];
        }
        return "";
    }
}
