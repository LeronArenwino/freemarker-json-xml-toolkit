/*
 * This file is part of FreeMarker Tool.
 *
 * FreeMarker Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FreeMarker Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeMarker Tool. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Look and feel to mainWindow
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException |
                 IllegalAccessException _) {

        }

        // Create and display the form
        java.awt.EventQueue.invokeLater(() -> {

        });

    }
}