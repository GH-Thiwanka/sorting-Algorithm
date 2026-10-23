package com.uoc.sorting.ui;

import com.uoc.sorting.algorithms.SortAlgorithm;
import com.uoc.sorting.benchmark.EvaluationReport;
import com.uoc.sorting.benchmark.PerformanceEvaluator;
import com.uoc.sorting.io.CSVParserException;
import com.uoc.sorting.io.CSVReader;
import com.uoc.sorting.model.ColumnInfo;
import com.uoc.sorting.model.Dataset;
import com.uoc.sorting.model.SortResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Main Graphical User Interface for the Sorting Algorithm Performance Evaluator.
 */
public class MainFrame extends JFrame {

    private final PerformanceEvaluator evaluator;
    private Dataset currentDataset;
    private EvaluationReport currentReport;

    // UI Components
    private JLabel lblFileName;
    private JLabel lblRowCount;
    private JLabel lblColCount;
    private JLabel lblNumericColsCount;
    private JComboBox<ColumnItem> comboColumns;
    private JComboBox<String> comboAlgorithms;
    private JButton btnUpload;
    private JButton btnLoadSample;
    private JButton btnRun;
    private JButton btnExport;

    // Result Presentation
    private JPanel panelBestPerformer;
    private JLabel lblBestPerformerText;
    private JLabel lblBestPerformerSpeedup;
    private ChartPanel chartPanel;
    private JTable tableResults;
    private ResultsTableModel resultsTableModel;

    // Tabs
    private JTabbedPane tabbedPane;
    private JTable tableDatasetPreview;
    private JTextArea txtSortedPreview;
    private JLabel lblStatus;

    public MainFrame() {
        this.evaluator = new PerformanceEvaluator();
        initUI();
    }

    private void initUI() {
        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(1200, 800);
        setMinimumSize(new Dimension(980, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root container
        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(UITheme.BG_LIGHT);

        // 1. Top Header Banner
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Content (Controls + Tabs)
        JPanel centerContainer = new JPanel(new BorderLayout(10, 10));
        centerContainer.setBorder(new EmptyBorder(12, 16, 12, 16));
        centerContainer.setBackground(UITheme.BG_LIGHT);

        centerContainer.add(createControlToolbar(), BorderLayout.NORTH);
        centerContainer.add(createMainTabs(), BorderLayout.CENTER);

        rootPanel.add(centerContainer, BorderLayout.CENTER);

        // 3. Bottom Status Bar
        rootPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_DARK);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 2, 2));
        titleBlock.setOpaque(false);

        JLabel titleLabel = new JLabel("Sorting Algorithm Performance Evaluation Suite");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.HEADER_TEXT);

        JLabel subtitleLabel = new JLabel("Upload a CSV dataset, select a numeric column, and evaluate sorting algorithm execution times");
        subtitleLabel.setFont(UITheme.FONT_SUBTITLE);
        subtitleLabel.setForeground(new Color(148, 163, 184)); // Slate 400

        titleBlock.add(titleLabel);
        titleBlock.add(subtitleLabel);

        header.add(titleBlock, BorderLayout.WEST);

        JLabel badge = new JLabel("Performance Suite");
        badge.setFont(UITheme.FONT_BODY_BOLD);
        badge.setForeground(UITheme.SUCCESS_GREEN);
        header.add(badge, BorderLayout.EAST);

        return header;
    }

    private JPanel createControlToolbar() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setOpaque(false);

        // Dataset Information Card
        JPanel infoCard = new JPanel(new GridLayout(1, 4, 10, 0));
        infoCard.setBackground(UITheme.PANEL_BG);
        infoCard.setBorder(UITheme.createCardBorder());

        lblFileName = new JLabel("File: (None loaded)");
        lblFileName.setFont(UITheme.FONT_BODY_BOLD);

        lblRowCount = new JLabel("Rows: 0");
        lblRowCount.setFont(UITheme.FONT_BODY);

        lblColCount = new JLabel("Total Columns: 0");
        lblColCount.setFont(UITheme.FONT_BODY);

        lblNumericColsCount = new JLabel("Numeric Columns: 0");
        lblNumericColsCount.setFont(UITheme.FONT_BODY);
        lblNumericColsCount.setForeground(UITheme.ACCENT_BLUE);

        infoCard.add(lblFileName);
        infoCard.add(lblRowCount);
        infoCard.add(lblColCount);
        infoCard.add(lblNumericColsCount);

        // Action Toolbar
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actionRow.setBackground(UITheme.PANEL_BG);
        actionRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, false),
            new EmptyBorder(6, 10, 6, 10)
        ));

        btnUpload = UITheme.createStyledButton("Upload CSV File", UITheme.PRIMARY_NAVY, Color.WHITE);
        btnUpload.addActionListener(e -> chooseAndUploadFile());

        btnLoadSample = UITheme.createStyledButton("Load Sample Data", new Color(71, 85, 105), Color.WHITE);
        btnLoadSample.addActionListener(e -> promptLoadSampleDataset());

        JLabel lblCol = new JLabel("Sort Column:");
        lblCol.setFont(UITheme.FONT_BODY_BOLD);
        comboColumns = new JComboBox<>();
        comboColumns.setPreferredSize(new Dimension(220, 32));
        comboColumns.setFont(UITheme.FONT_BODY);

        JLabel lblAlgo = new JLabel("Algorithm:");
        lblAlgo.setFont(UITheme.FONT_BODY_BOLD);
        comboAlgorithms = new JComboBox<>();
        comboAlgorithms.setPreferredSize(new Dimension(200, 32));
        comboAlgorithms.setFont(UITheme.FONT_BODY);
        comboAlgorithms.addItem("Compare All (All 5 Algorithms)");
        for (SortAlgorithm algo : evaluator.getRegisteredAlgorithms()) {
            comboAlgorithms.addItem(algo.getName());
        }

        btnRun = UITheme.createStyledButton("Run Performance Benchmark", UITheme.SUCCESS_DARK, Color.WHITE);
        btnRun.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRun.addActionListener(e -> executeBenchmark());

        btnExport = UITheme.createStyledButton("Export Report", new Color(100, 116, 139), Color.WHITE);
        btnExport.setEnabled(false);
        btnExport.addActionListener(e -> exportBenchmarkReport());

        actionRow.add(btnUpload);
        actionRow.add(btnLoadSample);
        actionRow.add(Box.createHorizontalStrut(10));
        actionRow.add(lblCol);
        actionRow.add(comboColumns);
        actionRow.add(Box.createHorizontalStrut(5));
        actionRow.add(lblAlgo);
        actionRow.add(comboAlgorithms);
        actionRow.add(Box.createHorizontalStrut(10));
        actionRow.add(btnRun);
        actionRow.add(btnExport);

        container.add(infoCard, BorderLayout.NORTH);
        container.add(actionRow, BorderLayout.SOUTH);

        return container;
    }

    private JTabbedPane createMainTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_HEADER);

        // Tab 1: Benchmark Results & Visual Chart
        tabbedPane.addTab("Benchmark & Comparison", createBenchmarkTab());

        // Tab 2: Dataset Preview Table
        tabbedPane.addTab("Dataset Preview", createDatasetPreviewTab());

        // Tab 3: Sorted Column Values
        tabbedPane.addTab("Sorted Data Preview", createSortedDataTab());

        // Tab 4: Algorithm Analysis & Big-O Guide
        tabbedPane.addTab("Algorithm Reference", createAlgorithmReferenceTab());

        return tabbedPane;
    }

    private JPanel createBenchmarkTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Best Performer Banner
        panelBestPerformer = new JPanel(new BorderLayout(10, 5));
        panelBestPerformer.setBackground(UITheme.SUCCESS_BG);
        panelBestPerformer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.SUCCESS_DARK, 1, false),
            new EmptyBorder(12, 16, 12, 16)
        ));
        panelBestPerformer.setVisible(false);

        lblBestPerformerText = new JLabel("BEST PERFORMING ALGORITHM");
        lblBestPerformerText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBestPerformerText.setForeground(UITheme.SUCCESS_DARK);

        lblBestPerformerSpeedup = new JLabel("");
        lblBestPerformerSpeedup.setFont(UITheme.FONT_BODY);
        lblBestPerformerSpeedup.setForeground(UITheme.TEXT_PRIMARY);

        panelBestPerformer.add(lblBestPerformerText, BorderLayout.NORTH);
        panelBestPerformer.add(lblBestPerformerSpeedup, BorderLayout.CENTER);

        // 2. Chart Panel
        chartPanel = new ChartPanel();
        chartPanel.setBorder(UITheme.createCardBorder());

        // 3. Results Table
        resultsTableModel = new ResultsTableModel();
        tableResults = new JTable(resultsTableModel);
        tableResults.setRowHeight(28);
        tableResults.setFont(UITheme.FONT_BODY);
        tableResults.getTableHeader().setFont(UITheme.FONT_BODY_BOLD);
        tableResults.getTableHeader().setBackground(new Color(241, 245, 249));
        tableResults.setShowGrid(true);
        tableResults.setGridColor(UITheme.BORDER_COLOR);

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableResults.getColumnCount(); i++) {
            tableResults.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTable = new JScrollPane(tableResults);
        scrollTable.setPreferredSize(new Dimension(800, 170));
        scrollTable.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, false),
            "Benchmark Results Table & Theoretical Complexity Breakdown",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            UITheme.FONT_BODY_BOLD,
            UITheme.TEXT_PRIMARY
        ));

        // Center Split (Chart on top, Table on bottom)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, scrollTable);
        splitPane.setResizeWeight(0.65);
        splitPane.setBorder(null);

        panel.add(panelBestPerformer, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDatasetPreviewTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableDatasetPreview = new JTable();
        tableDatasetPreview.setRowHeight(24);
        tableDatasetPreview.setFont(UITheme.FONT_BODY);
        tableDatasetPreview.getTableHeader().setFont(UITheme.FONT_BODY_BOLD);
        tableDatasetPreview.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(tableDatasetPreview);
        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel lblNotice = new JLabel("Showing up to first 5,000 rows for preview efficiency. Select a numeric column in the toolbar above to run sorting.");
        lblNotice.setFont(UITheme.FONT_SUBTITLE);
        lblNotice.setForeground(UITheme.TEXT_MUTED);
        panel.add(lblNotice, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSortedDataTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));

        txtSortedPreview = new JTextArea();
        txtSortedPreview.setEditable(false);
        txtSortedPreview.setFont(UITheme.FONT_CODE);
        txtSortedPreview.setLineWrap(true);
        txtSortedPreview.setWrapStyleWord(true);
        txtSortedPreview.setText("Run a benchmark to preview the verified sorted column elements here.");

        JScrollPane scroll = new JScrollPane(txtSortedPreview);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, false),
            "Sorted Values Verification (Ascending Order)",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            UITheme.FONT_BODY_BOLD,
            UITheme.TEXT_PRIMARY
        ));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAlgorithmReferenceTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(buildAlgorithmReferenceHtml());
        editorPane.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(editorPane);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(241, 245, 249));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR),
            new EmptyBorder(6, 14, 6, 14)
        ));

        lblStatus = new JLabel("Ready. Please upload a .csv file or click 'Load Sample Data' to begin.");
        lblStatus.setFont(UITheme.FONT_SUBTITLE);
        lblStatus.setForeground(UITheme.TEXT_MUTED);

        statusPanel.add(lblStatus, BorderLayout.WEST);

        return statusPanel;
    }

    // ----------------------------------------------------
    // User Actions and File Handling
    // ----------------------------------------------------

    private void chooseAndUploadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Upload CSV Dataset");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Datasets (*.csv)", "csv"));

        File defaultDir = new File("datasets");
        if (defaultDir.exists()) {
            chooser.setCurrentDirectory(defaultDir);
        }

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            loadFile(chooser.getSelectedFile());
        }
    }

    private void promptLoadSampleDataset() {
        File datasetsDir = new File("datasets");
        if (!datasetsDir.exists() || !datasetsDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, 
                "Sample datasets directory not found at: " + datasetsDir.getAbsolutePath(),
                "Directory Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File[] files = datasetsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "No CSV sample files found in datasets directory.", 
                "No Samples", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] sampleNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            sampleNames[i] = files[i].getName();
        }

        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Choose a sample dataset to load:",
            "Load Sample Dataset",
            JOptionPane.QUESTION_MESSAGE,
            null,
            sampleNames,
            sampleNames[0]
        );

        if (selected != null) {
            for (File file : files) {
                if (file.getName().equals(selected)) {
                    loadFile(file);
                    break;
                }
            }
        }
    }

    private void loadFile(File file) {
        try {
            lblStatus.setText("Reading and parsing CSV file: " + file.getName() + "...");
            Dataset dataset = CSVReader.read(file);
            setDataset(dataset);
            lblStatus.setText("Successfully loaded " + dataset.getRowCount() + " rows from " + file.getName());
        } catch (CSVParserException e) {
            JOptionPane.showMessageDialog(this,
                "Failed to parse CSV file:\n" + e.getMessage(),
                "CSV Upload Error", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("CSV Upload failed: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "I/O error while reading file:\n" + e.getMessage(),
                "File Read Error", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("File read error: " + e.getMessage());
        }
    }

    private void setDataset(Dataset dataset) {
        this.currentDataset = dataset;

        // Update Info Labels
        lblFileName.setText("File: " + dataset.getSourceFileName());
        lblRowCount.setText(String.format("Rows: %,d", dataset.getRowCount()));
        lblColCount.setText("Columns: " + dataset.getColumnCount());

        List<ColumnInfo> numericCols = dataset.getNumericColumns();
        lblNumericColsCount.setText("Numeric Columns: " + numericCols.size());

        // Update Combo Columns
        comboColumns.removeAllItems();
        for (ColumnInfo col : dataset.getColumnInfos()) {
            comboColumns.addItem(new ColumnItem(col));
        }

        // Auto-select first numeric column
        if (!numericCols.isEmpty()) {
            for (int i = 0; i < comboColumns.getItemCount(); i++) {
                ColumnItem item = comboColumns.getItemAt(i);
                if (item.info.isNumeric()) {
                    comboColumns.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Update Dataset Preview Table
        tableDatasetPreview.setModel(new DataTableModel(dataset));

        // Reset previous benchmark results
        resultsTableModel.setResults(null);
        chartPanel.setResults(null, "Algorithm Execution Time Comparison");
        panelBestPerformer.setVisible(false);
        txtSortedPreview.setText("Dataset loaded. Select numeric column and click 'Run Performance Benchmark' to sort.");
        btnExport.setEnabled(false);

        // Switch to preview tab initially
        tabbedPane.setSelectedIndex(1);
    }

    private void executeBenchmark() {
        if (currentDataset == null) {
            JOptionPane.showMessageDialog(this, 
                "Please upload a .csv file first before running benchmark.",
                "No Dataset Uploaded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ColumnItem selectedItem = (ColumnItem) comboColumns.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a column to sort.",
                "No Column Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate numeric requirement
        if (!selectedItem.info.isNumeric()) {
            JOptionPane.showMessageDialog(this,
                "The selected column '" + selectedItem.info.getName() + "' contains text or non-numeric values.\n\n" +
                "Please select a numeric column to sort.",
                "Numeric Column Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double[] rawData = currentDataset.extractColumnData(selectedItem.info.getIndex());
            if (rawData.length == 0) {
                JOptionPane.showMessageDialog(this, "The selected column has no valid numbers to sort.", 
                    "Empty Column", JOptionPane.WARNING_MESSAGE);
                return;
            }

            lblStatus.setText("Executing sorting algorithms on " + rawData.length + " values...");

            String selectedAlgo = (String) comboAlgorithms.getSelectedItem();
            EvaluationReport report;

            if (selectedAlgo == null || selectedAlgo.startsWith("Compare All")) {
                report = evaluator.evaluateAll(currentDataset.getSourceFileName(), selectedItem.info.getName(), rawData);
            } else {
                SortAlgorithm targetAlgo = null;
                for (SortAlgorithm algo : evaluator.getRegisteredAlgorithms()) {
                    if (algo.getName().equals(selectedAlgo)) {
                        targetAlgo = algo;
                        break;
                    }
                }
                report = evaluator.evaluateOne(currentDataset.getSourceFileName(), selectedItem.info.getName(), rawData, targetAlgo);
            }

            this.currentReport = report;
            displayBenchmarkResults(report);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during benchmark execution: " + ex.getMessage(), 
                "Execution Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayBenchmarkResults(EvaluationReport report) {
        // 1. Update Table
        resultsTableModel.setResults(report.getResults());

        // 2. Update Visual Chart
        String title = String.format("Performance Comparison on Column '%s' (%,d Elements)", 
            report.getColumnName(), report.getTotalElements());
        chartPanel.setResults(report.getResults(), title);

        // 3. Highlight Best Performer
        SortResult best = report.getBestPerformer();
        if (best != null) {
            panelBestPerformer.setVisible(true);
            lblBestPerformerText.setText(String.format("BEST PERFORMING ALGORITHM: %s  —  Execution Time: %s",
                best.getAlgorithmName().toUpperCase(), best.getFormattedTime()));

            if (report.getResults().size() > 1 && report.getSlowestPerformer() != null) {
                lblBestPerformerSpeedup.setText(String.format(
                    "Demonstrated a %.1fx speedup over the slowest algorithm (%s at %s). Verified non-decreasing order.",
                    report.getSpeedupFactor(),
                    report.getSlowestPerformer().getAlgorithmName(),
                    report.getSlowestPerformer().getFormattedTime()
                ));
            } else {
                lblBestPerformerSpeedup.setText("Single algorithm evaluated. Sorting verified successfully.");
            }
        }

        // 4. Update Sorted Preview Tab
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append(String.format("SORTED DATA PREVIEW: %s | Column: %s | Total: %,d Elements\n", 
            report.getDatasetName(), report.getColumnName(), report.getTotalElements()));
        sb.append(String.format("Best Algorithm: %s (%s)\n", best != null ? best.getAlgorithmName() : "N/A", 
            best != null ? best.getFormattedTime() : "N/A"));
        sb.append("========================================================================\n\n");

        if (best != null && best.getSampleSortedData().length > 0) {
            double[] sample = best.getSampleSortedData();
            sb.append(String.format("First %d Sorted Values:\n", sample.length));
            for (int i = 0; i < sample.length; i++) {
                sb.append(String.format("[%d] %f\n", i + 1, sample[i]));
            }
            if (report.getTotalElements() > sample.length) {
                sb.append(String.format("\n... (%d more sorted values omitted from preview) ...\n", 
                    report.getTotalElements() - sample.length));
            }
        }
        txtSortedPreview.setText(sb.toString());
        txtSortedPreview.setCaretPosition(0);

        // Enable Export
        btnExport.setEnabled(true);

        // Switch to benchmark tab
        tabbedPane.setSelectedIndex(0);

        lblStatus.setText(String.format("Benchmark completed successfully: %s was fastest at %s", 
            best != null ? best.getAlgorithmName() : "", 
            best != null ? best.getFormattedTime() : ""));
    }

    private void exportBenchmarkReport() {
        if (currentReport == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("sorting_benchmark_report.txt"));
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File target = chooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(target)) {
                writer.write("=======================================================================\n");
                writer.write("SORTING ALGORITHM PERFORMANCE EVALUATION REPORT\n");
                writer.write("=======================================================================\n\n");
                writer.write("Dataset: " + currentReport.getDatasetName() + "\n");
                writer.write("Evaluated Column: " + currentReport.getColumnName() + "\n");
                writer.write("Total Elements: " + currentReport.getTotalElements() + "\n\n");
                writer.write(currentReport.getSummaryStatement() + "\n\n");

                writer.write(String.format("%-6s %-16s %-18s %-16s %-12s %-12s %-12s %-12s\n", 
                    "Rank", "Algorithm", "Execution Time", "Nanoseconds", "Verification", "Best", "Average", "Worst"));
                writer.write("-----------------------------------------------------------------------------------------------------------\n");

                for (SortResult res : currentReport.getResults()) {
                    writer.write(String.format("%-6s %-16s %-18s %-16s %-12s %-12s %-12s %-12s\n",
                        String.valueOf(res.getRank()),
                        res.getAlgorithmName(),
                        res.getFormattedTime(),
                        String.format("%,d ns", res.getExecutionTimeNanos()),
                        res.isSortedCorrectly() ? "PASSED" : "FAILED",
                        res.getTimeComplexityBest(),
                        res.getTimeComplexityAverage(),
                        res.getTimeComplexityWorst()
                    ));
                }

                JOptionPane.showMessageDialog(this, "Report saved successfully to:\n" + target.getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save report: " + ex.getMessage(),
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper item for JComboBox
    private static class ColumnItem {
        final ColumnInfo info;

        ColumnItem(ColumnInfo info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return info.toString();
        }
    }

    private String buildAlgorithmReferenceHtml() {
        return "<html><body style='font-family:sans-serif; padding:15px; color:#1e293b;'>" +
            "<h2 style='color:#0f172a;'>Sorting Algorithm Complexity and Mechanics</h2>" +
            "<p>The five sorting algorithms implemented in this evaluation application:</p>" +
            "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse; width:100%; border-color:#cbd5e1;'>" +
            "<tr style='background-color:#f1f5f9; text-align:left;'>" +
            "<th>Algorithm</th><th>Best Time</th><th>Average Time</th><th>Worst Time</th><th>Space</th><th>Stability</th><th>Core Concept</th>" +
            "</tr>" +
            "<tr>" +
            "<td><b>Insertion Sort</b></td><td>O(n)</td><td>O(n²)</td><td>O(n²)</td><td>O(1)</td><td>Stable</td>" +
            "<td>Iteratively shifts larger elements right to insert each key into its proper sorted prefix position.</td>" +
            "</tr>" +
            "<tr>" +
            "<td><b>Shell Sort</b></td><td>O(n log n)</td><td>O(n^1.25) ~ O(n^1.5)</td><td>O(n^1.5)</td><td>O(1)</td><td>Unstable</td>" +
            "<td>Generalization of insertion sort comparing elements separated by Knuth gap (h = 3h + 1).</td>" +
            "</tr>" +
            "<tr>" +
            "<td><b>Merge Sort</b></td><td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(n)</td><td>Stable</td>" +
            "<td>Divide-and-conquer strategy recursively dividing array into halves, sorting, and merging them.</td>" +
            "</tr>" +
            "<tr>" +
            "<td><b>Quick Sort</b></td><td>O(n log n)</td><td>O(n log n)</td><td>O(n²)</td><td>O(log n)</td><td>Unstable</td>" +
            "<td>Partition-exchange algorithm utilizing Median-of-Three pivot to avoid worst case on pre-sorted data.</td>" +
            "</tr>" +
            "<tr>" +
            "<td><b>Heap Sort</b></td><td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(1)</td><td>Unstable</td>" +
            "<td>Transforms array into binary max-heap and repeatedly extracts maximum element into place.</td>" +
            "</tr>" +
            "</table>" +
            "</body></html>";
    }
}
