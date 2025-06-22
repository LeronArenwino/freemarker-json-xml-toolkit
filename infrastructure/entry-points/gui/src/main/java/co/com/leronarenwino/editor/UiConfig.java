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

    public static final Color DARK_BG = new Color(40, 44, 52);
    public static final Color DARK_FG = new Color(187, 187, 187);
    public static final Color BUTTON_BG = new Color(60, 63, 65);
    public static final Color BUTTON_FG = new Color(200, 200, 200);
    public static final Color SCROLLBAR_TRACK = new Color(60, 63, 65);

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

    public static void customizeRTextScrollPanes(String[] titles, RTextScrollPane... scrollPanes) {
        for (int i = 0; i < scrollPanes.length && i < titles.length; i++) {
            customizeRTextScrollPane(scrollPanes[i], titles[i]);
        }
    }

    public static void customizeRTextScrollPane(RTextScrollPane scrollPane, String title) {
        scrollPane.getViewport().setBackground(DARK_BG);
        scrollPane.setBackground(DARK_BG);
        scrollPane.setForeground(DARK_FG);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(SCROLLBAR_TRACK),
                        title, 0, 0, null, DARK_FG
                )
        );

        org.fife.ui.rtextarea.Gutter gutter = scrollPane.getGutter();
        gutter.setBackground(DARK_BG);
        gutter.setBorderColor(SCROLLBAR_TRACK);
        gutter.setLineNumberColor(DARK_FG);
        gutter.setLineNumberFont(new Font("Consolas", Font.PLAIN, 12));
    }

    public static void setCaretColors(Color color, RSyntaxTextArea... textAreas) {
        for (RSyntaxTextArea area : textAreas) {
            area.setCaretColor(color);
        }
    }

}