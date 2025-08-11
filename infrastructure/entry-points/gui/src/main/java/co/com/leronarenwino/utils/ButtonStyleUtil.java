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

    /**
     * Configura los estilos globales de FlatLaf para botones semánticos
     */
    public static void applyFlatLafButtonStyles() {
        // Botones de peligro (destructivos)
        UIManager.put("Button[danger].background", new Color(220, 53, 69));
        UIManager.put("Button[danger].foreground", Color.WHITE);
        UIManager.put("Button[danger].hoverBackground", new Color(200, 35, 51));
        UIManager.put("Button[danger].pressedBackground", new Color(180, 25, 41));

        // Botones de éxito
        UIManager.put("Button[success].background", new Color(40, 167, 69));
        UIManager.put("Button[success].foreground", Color.WHITE);
        UIManager.put("Button[success].hoverBackground", new Color(34, 142, 58));
        UIManager.put("Button[success].pressedBackground", new Color(28, 117, 48));

        // Botones primarios
        UIManager.put("Button[primary].background", new Color(0, 123, 255));
        UIManager.put("Button[primary].foreground", Color.WHITE);
        UIManager.put("Button[primary].hoverBackground", new Color(0, 86, 179));
        UIManager.put("Button[primary].pressedBackground", new Color(0, 69, 144));
    }

    /**
     * Crea un botón con estilo semántico usando FlatLaf
     */
    public static JButton createStyledButton(String text, String tooltip, ButtonStyle style) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        applyStyle(button, style);
        return button;
    }

    /**
     * Aplica un estilo a un botón existente
     */
    public static void applyStyle(JButton button, ButtonStyle style) {
        switch (style) {
            case PRIMARY -> button.putClientProperty("JButton.styleClass", "primary");
            case SUCCESS -> button.putClientProperty("JButton.styleClass", "success");
            case DANGER -> button.putClientProperty("JButton.styleClass", "danger");
            case SECONDARY -> button.putClientProperty("JButton.buttonType", "borderless");
        }
    }
}