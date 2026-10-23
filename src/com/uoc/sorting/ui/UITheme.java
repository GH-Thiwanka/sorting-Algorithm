package com.uoc.sorting.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Styling, colors, typography, and clean button components.
 */
public class UITheme {

    // Modern professional palette
    public static final Color PRIMARY_DARK = new Color(24, 32, 47);       // Slate dark header
    public static final Color PRIMARY_NAVY = new Color(51, 65, 85);       // Slate 700
    public static final Color ACCENT_BLUE = new Color(37, 99, 235);       // Blue 600
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129);    // Emerald 500
    public static final Color SUCCESS_DARK = new Color(5, 150, 105);      // Emerald 600
    public static final Color SUCCESS_BG = new Color(240, 253, 244);      // Soft emerald
    public static final Color BG_LIGHT = new Color(248, 250, 252);        // Slate 50
    public static final Color PANEL_BG = new Color(255, 255, 255);        // Pure White
    public static final Color BORDER_COLOR = new Color(226, 232, 240);    // Slate 200
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);      // Slate 500
    public static final Color HEADER_TEXT = new Color(248, 250, 252);     // Slate 50

    // Typography (system fonts)
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 12);

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, false),
            new EmptyBorder(10, 14, 10, 14)
        );
    }

    /**
     * Creates a modern flat button with zero black borders and smooth hover feedback.
     */
    public static JButton createStyledButton(String text, Color baseColor, Color textColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isEnabled()) {
                            isHovered = true;
                            repaint();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (!isEnabled()) {
                    g2.setColor(new Color(203, 213, 225));
                } else if (isHovered) {
                    g2.setColor(getHoverColor(baseColor));
                } else {
                    g2.setColor(baseColor);
                }

                // Smooth rounded flat background with NO black border
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                // Draw text
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(isEnabled() ? textColor : new Color(148, 163, 184));
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(FONT_BODY_BOLD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static Color getHoverColor(Color color) {
        int r = Math.max(0, color.getRed() - 20);
        int g = Math.max(0, color.getGreen() - 20);
        int b = Math.max(0, color.getBlue() - 20);
        return new Color(r, g, b);
    }

    public static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep default
        }
    }
}
