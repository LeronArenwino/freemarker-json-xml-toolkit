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

package co.com.leronarenwino.utils;

import javax.swing.*;
import java.awt.*;

public class ButtonStyleUtil {

    public enum ButtonStyle {
        PRIMARY, SUCCESS, DANGER, SECONDARY
    }

    public static void applyFlatLafButtonStyles() {
        // Estilos PRIMARY
        UIManager.put("Button.primary.background", new Color(0, 123, 255));
        UIManager.put("Button.primary.foreground", Color.WHITE);
        UIManager.put("Button.primary.hoverBackground", new Color(0, 86, 179));
        UIManager.put("Button.primary.pressedBackground", new Color(0, 86, 179));
        UIManager.put("Button.primary.borderColor", new Color(0, 123, 255));

        // Estilos SUCCESS
        UIManager.put("Button.success.background", new Color(40, 167, 69));
        UIManager.put("Button.success.foreground", Color.WHITE);
        UIManager.put("Button.success.hoverBackground", new Color(33, 136, 56));
        UIManager.put("Button.success.pressedBackground", new Color(33, 136, 56));
        UIManager.put("Button.success.borderColor", new Color(40, 167, 69));

        // Estilos DANGER
        UIManager.put("Button.danger.background", new Color(220, 53, 69));
        UIManager.put("Button.danger.foreground", Color.WHITE);
        UIManager.put("Button.danger.hoverBackground", new Color(200, 35, 51));
        UIManager.put("Button.danger.pressedBackground", new Color(200, 35, 51));
        UIManager.put("Button.danger.borderColor", new Color(220, 53, 69));

        // Estilos SECONDARY
        UIManager.put("Button.secondary.background", new Color(108, 117, 125));
        UIManager.put("Button.secondary.foreground", Color.WHITE);
        UIManager.put("Button.secondary.hoverBackground", new Color(90, 98, 104));
        UIManager.put("Button.secondary.pressedBackground", new Color(90, 98, 104));
        UIManager.put("Button.secondary.borderColor", new Color(108, 117, 125));
    }

    public static JButton createStyledButton(String text, String tooltip, ButtonStyle style) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);

        switch (style) {
            case PRIMARY:
                button.putClientProperty("FlatLaf.style",
                        "background: $Button.primary.background; " +
                                "foreground: $Button.primary.foreground; " +
                                "hoverBackground: $Button.primary.hoverBackground; " +
                                "pressedBackground: $Button.primary.pressedBackground; " +
                                "borderColor: $Button.primary.borderColor");
                break;
            case SUCCESS:
                button.putClientProperty("FlatLaf.style",
                        "background: $Button.success.background; " +
                                "foreground: $Button.success.foreground; " +
                                "hoverBackground: $Button.success.hoverBackground; " +
                                "pressedBackground: $Button.success.pressedBackground; " +
                                "borderColor: $Button.success.borderColor");
                break;
            case DANGER:
                button.putClientProperty("FlatLaf.style",
                        "background: $Button.danger.background; " +
                                "foreground: $Button.danger.foreground; " +
                                "hoverBackground: $Button.danger.hoverBackground; " +
                                "pressedBackground: $Button.danger.pressedBackground; " +
                                "borderColor: $Button.danger.borderColor");
                break;
            case SECONDARY:
                button.putClientProperty("FlatLaf.style",
                        "background: $Button.secondary.background; " +
                                "foreground: $Button.secondary.foreground; " +
                                "hoverBackground: $Button.secondary.hoverBackground; " +
                                "pressedBackground: $Button.secondary.pressedBackground; " +
                                "borderColor: $Button.secondary.borderColor");
                break;
        }
        return button;
    }
}