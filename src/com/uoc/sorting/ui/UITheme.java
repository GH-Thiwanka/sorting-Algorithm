package com.uoc.sorting.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Aesthetic styling, colors, and layout constants for the Swing User Interface.
 * Part of Member 3's contribution: User Interface and Experience.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class UITheme {

    // Modern professional palette
    public static final Color PRIMARY_DARK = new Color(15, 23, 42);       // Slate 900
    public static final Color PRIMARY_NAVY = new Color(30, 41, 59);       // Slate 800
    public static final Color ACCENT_BLUE = new Color(37, 99, 235);       // Blue 600
    public static final Color ACCENT_HOVER = new Color(29, 78, 216);      // Blue 700
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129);    // Emerald 500
    public static final Color SUCCESS_DARK = new Color(5, 150, 105);      // Emerald 600
    public static final Color SUCCESS_BG = new Color(236, 253, 245);      // Emerald 50
    public static final Color WARNING_ORANGE = new Color(245, 158, 11);   // Amber 500
    public static final Color BG_LIGHT = new Color(248, 250, 252);        // Slate 50
    public static final Color PANEL_BG = new Color(255, 255, 255);        // Pure White
    public static final Color BORDER_COLOR = new Color(226, 232, 240);    // Slate 200
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);      // Slate 500
    public static final Color HEADER_TEXT = new Color(241, 245, 249);     // Slate 100

    // Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_STAT_VAL = new Font("Segoe UI", Font.BOLD, 22);

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        );
    }

    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY_BOLD);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void applySystemLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Default fallback
            }
        }
    }
}
