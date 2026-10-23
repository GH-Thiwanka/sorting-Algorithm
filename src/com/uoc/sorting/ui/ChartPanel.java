package com.uoc.sorting.ui;

import com.uoc.sorting.model.SortResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom 2D Graphics panel that renders a high-definition bar chart
 * comparing sorting algorithm execution times.
 *
 * Part of Member 3's contribution: Visual Performance Reporting.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class ChartPanel extends JPanel {

    private List<SortResult> results = new ArrayList<>();
    private String chartTitle = "Algorithm Execution Time Comparison";

    public ChartPanel() {
        setBackground(UITheme.PANEL_BG);
        setPreferredSize(new Dimension(650, 320));
    }

    public void setResults(List<SortResult> results, String title) {
        this.results = results != null ? new ArrayList<>(results) : new ArrayList<>();
        this.chartTitle = title != null ? title : "Algorithm Execution Time Comparison";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int width = getWidth();
        int height = getHeight();

        // Background
        g2.setColor(UITheme.PANEL_BG);
        g2.fillRect(0, 0, width, height);

        if (results == null || results.isEmpty()) {
            drawEmptyState(g2, width, height);
            g2.dispose();
            return;
        }

        // Draw Title
        g2.setFont(UITheme.FONT_HEADER);
        g2.setColor(UITheme.TEXT_PRIMARY);
        g2.drawString(chartTitle, 25, 30);

        g2.setFont(UITheme.FONT_SUBTITLE);
        g2.setColor(UITheme.TEXT_MUTED);
        g2.drawString("Execution time measured using System.nanoTime() | Lower is better", 25, 48);

        // Find max execution time for relative bar scaling
        long maxNanos = 1;
        long minNanos = Long.MAX_VALUE;
        for (SortResult r : results) {
            if (r.getExecutionTimeNanos() > maxNanos) maxNanos = r.getExecutionTimeNanos();
            if (r.getExecutionTimeNanos() < minNanos) minNanos = r.getExecutionTimeNanos();
        }

        int count = results.size();
        int topMargin = 70;
        int bottomMargin = 30;
        int availableHeight = height - topMargin - bottomMargin;
        int barHeight = Math.min(38, Math.max(24, availableHeight / (count * 2)));
        int rowSpacing = availableHeight / Math.max(1, count);

        int labelX = 25;
        int labelWidth = 140;
        int barStartX = labelX + labelWidth + 10;
        int rightMargin = 220;
        int maxBarWidth = Math.max(100, width - barStartX - rightMargin);

        // Draw axis guide line
        g2.setColor(UITheme.BORDER_COLOR);
        g2.drawLine(barStartX, topMargin - 10, barStartX, height - bottomMargin);

        for (int i = 0; i < count; i++) {
            SortResult res = results.get(i);
            int y = topMargin + (i * rowSpacing) + (rowSpacing - barHeight) / 2;

            // Algorithm Name and Rank Badge
            g2.setFont(UITheme.FONT_BODY_BOLD);
            if (res.isBest()) {
                g2.setColor(UITheme.SUCCESS_DARK);
                g2.drawString("★ " + res.getAlgorithmName(), labelX, y + barHeight / 2 + 5);
            } else {
                g2.setColor(UITheme.TEXT_PRIMARY);
                g2.drawString(res.getAlgorithmName(), labelX, y + barHeight / 2 + 5);
            }

            // Calculate scaled bar length (using smooth scaling with min visible width of 15px)
            double ratio = (double) res.getExecutionTimeNanos() / (double) maxNanos;
            int barWidth = (int) Math.round(ratio * maxBarWidth);
            if (barWidth < 18) {
                barWidth = 18;
            }

            // Draw Bar Gradient
            Color startColor;
            Color endColor;
            if (res.isBest()) {
                startColor = new Color(16, 185, 129); // Vibrant emerald
                endColor = new Color(5, 150, 105);
            } else if (res.getRank() == 2) {
                startColor = new Color(59, 130, 246); // Blue
                endColor = new Color(29, 78, 216);
            } else if (res.getRank() == results.size()) {
                startColor = new Color(239, 68, 68);  // Rose/Red for slowest
                endColor = new Color(185, 28, 28);
            } else {
                startColor = new Color(100, 116, 139); // Slate
                endColor = new Color(71, 85, 105);
            }

            GradientPaint gradient = new GradientPaint(barStartX, y, startColor, barStartX + barWidth, y + barHeight, endColor);
            g2.setPaint(gradient);
            g2.fillRoundRect(barStartX, y, barWidth, barHeight, 8, 8);

            // Draw border
            g2.setColor(endColor.darker());
            g2.drawRoundRect(barStartX, y, barWidth, barHeight, 8, 8);

            // Draw Time text to the right of the bar
            g2.setFont(UITheme.FONT_BODY_BOLD);
            g2.setColor(UITheme.TEXT_PRIMARY);
            String timeText = res.getFormattedTime();
            int textX = barStartX + barWidth + 12;
            g2.drawString(timeText, textX, y + barHeight / 2 + 5);

            // If Best Performer, add trophy indicator
            if (res.isBest()) {
                g2.setFont(UITheme.FONT_BODY_BOLD);
                g2.setColor(UITheme.SUCCESS_DARK);
                int badgeX = textX + g2.getFontMetrics().stringWidth(timeText) + 12;
                g2.drawString("[BEST]", badgeX, y + barHeight / 2 + 5);
            }
        }

        g2.dispose();
    }

    private void drawEmptyState(Graphics2D g2, int width, int height) {
        g2.setColor(UITheme.TEXT_MUTED);
        g2.setFont(UITheme.FONT_HEADER);
        String msg = "No benchmark data available.";
        int msgWidth = g2.getFontMetrics().stringWidth(msg);
        g2.drawString(msg, (width - msgWidth) / 2, height / 2 - 10);

        g2.setFont(UITheme.FONT_BODY);
        String sub = "Upload a CSV dataset and click 'Run Performance Benchmark' to view comparison charts.";
        int subWidth = g2.getFontMetrics().stringWidth(sub);
        g2.drawString(sub, (width - subWidth) / 2, height / 2 + 15);
    }
}
