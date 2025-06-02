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

package co.com.leronarenwino.editor;

import javax.swing.*;
import java.awt.*;

public class TemplateEditor extends JFrame {

    private JPanel containerPanel;
    private JMenuBar containerJMenuBar;
    private JMenu fileJMenu;
    private JMenu editJMenu;
    private JMenuItem settingsJMenuItem;

    public TemplateEditor() {

        // Initialize the components
        initComponents();

        // Add the components to the frame
        addComponents();

        // Make the window visible
        setVisible(true);

    }

    public void initComponents() {

        // Set default configuration to JFrame
        setTitle("FreeMarker Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Container panel configuration
        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(true);
        containerPanel.add(new JLabel("Welcome to FreeMarker Tool", SwingConstants.CENTER), BorderLayout.CENTER);

        // Menu components
        containerJMenuBar = new JMenuBar();
        fileJMenu = new JMenu("File");
        editJMenu = new JMenu("Edit");
        settingsJMenuItem = new JMenuItem("Settings");

    }

    public void addComponents() {

        // Add the menu items to the menu bar
        containerJMenuBar.add(fileJMenu);
        containerJMenuBar.add(editJMenu);
        editJMenu.add(settingsJMenuItem);
        setJMenuBar(containerJMenuBar);

        // Add the container panel to the frame
        setContentPane(containerPanel);

    }
}
