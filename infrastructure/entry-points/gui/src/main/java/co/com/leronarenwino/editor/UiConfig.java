/*
 * This file is part of FreeMarker JSON/XML Toolkit.
 *
 * FreeMarker JSON/XML Toolkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FreeMarker JSON/XML Toolkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeMarker JSON/XML Toolkit. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class UiConfig {

    // Dark mode colors
    public static final Color DARK_BG = new Color(40, 44, 52);
    public static final Color DARK_FG = new Color(187, 187, 187);
    public static final Color BUTTON_BG = new Color(60, 63, 65);
    public static final Color BUTTON_FG = new Color(200, 200, 200);
    public static final Color SCROLLBAR_TRACK = new Color(60, 63, 65);

    // Light mode colors
    public static final Color LIGHT_BG = new Color(245, 245, 245);
    public static final Color LIGHT_FG = new Color(30, 30, 30);
    public static final Color LIGHT_BUTTON_BG = new Color(230, 230, 230);
    public static final Color LIGHT_BUTTON_FG = new Color(30, 30, 30);
    public static final Color LIGHT_SCROLLBAR_TRACK = new Color(220, 220, 220);

    // Store current colors for use in painting
    public static Color CURRENT_BG = DARK_BG;
    public static Color CURRENT_FG = DARK_FG;
    public static Color CURRENT_BUTTON_BG = BUTTON_BG;
    public static Color CURRENT_BUTTON_FG = BUTTON_FG;
    public static Color CURRENT_SCROLLBAR_TRACK = SCROLLBAR_TRACK;

    // Method to apply theme colors
    public static void applyTheme(String theme) {
        if ("Light".equalsIgnoreCase(theme)) {
            setCurrentColors(LIGHT_BG, LIGHT_FG, LIGHT_BUTTON_BG, LIGHT_BUTTON_FG, LIGHT_SCROLLBAR_TRACK);
        } else {
            setCurrentColors(DARK_BG, DARK_FG, BUTTON_BG, BUTTON_FG, SCROLLBAR_TRACK);
        }
    }

    private static void setCurrentColors(Color bg, Color fg, Color btnBg, Color btnFg, Color scrollTrack) {
        CURRENT_BG = bg;
        CURRENT_FG = fg;
        CURRENT_BUTTON_BG = btnBg;
        CURRENT_BUTTON_FG = btnFg;
        CURRENT_SCROLLBAR_TRACK = scrollTrack;
    }

    public static void setPanelColors(Color bg, JPanel... panels) {
        for (JPanel panel : panels) {
            panel.setBackground(bg);
        }
    }

    public static void setTextAreaColors(Color bg, Color fg, RSyntaxTextArea... areas) {
        for (RSyntaxTextArea area : areas) {
            area.setBackground(bg);
            area.setForeground(fg);
        }
    }

    public static void setLabelColors(Color fg, JLabel... labels) {
        for (JLabel label : labels) {
            label.setForeground(fg);
        }
    }

    public static void styleButtons(Color bg, Color fg, JButton... buttons) {
        for (JButton button : buttons) {
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setBackground(bg);
            button.setForeground(fg);
            button.setFocusPainted(false);
        }
    }

    public static void setScrollBarColors(Color trackColor, RTextScrollPane... scrollPanes) {
        for (RTextScrollPane scrollPane : scrollPanes) {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
            if (vertical != null) {
                vertical.setBackground(trackColor);
            }
            if (horizontal != null) {
                horizontal.setBackground(trackColor);
            }
        }
    }

    public static void customizeRTextScrollPanes(String[] titles, RTextScrollPane... scrollPanes) {
        for (int i = 0; i < scrollPanes.length && i < titles.length; i++) {
            customizeRTextScrollPane(scrollPanes[i], titles[i]);
        }
    }

    public static void customizeRTextScrollPane(RTextScrollPane scrollPane, String title) {
        scrollPane.getViewport().setBackground(CURRENT_BG);
        scrollPane.setBackground(CURRENT_BG);
        scrollPane.setForeground(CURRENT_FG);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(CURRENT_SCROLLBAR_TRACK),
                        title, 0, 0, null, CURRENT_FG
                )
        );

        org.fife.ui.rtextarea.Gutter gutter = scrollPane.getGutter();
        gutter.setBackground(CURRENT_BG);
        gutter.setBorderColor(CURRENT_SCROLLBAR_TRACK);
        gutter.setLineNumberColor(CURRENT_FG);
        gutter.setLineNumberFont(new Font("Consolas", Font.PLAIN, 12));
    }

    public static void setCaretColors(Color color, RSyntaxTextArea... textAreas) {
        for (RSyntaxTextArea area : textAreas) {
            area.setCaretColor(color);
        }
    }

}