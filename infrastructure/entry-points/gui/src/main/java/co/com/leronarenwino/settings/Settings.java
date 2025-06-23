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

package co.com.leronarenwino.settings;

import utils.SettingsSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.function.BiFunction;

public class Settings extends JDialog {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    // Editor tab
    private JPanel editorPanel;
    private JComboBox<String> themeCombo;

    // FreeMarker tab
    private JPanel freemarkerPanel;
    private BiFunction<String, JComboBox<String>, JPanel> createOption;
    private JComboBox<String> localeCombo;
    private JComboBox<String> timeZoneCombo;

    private JPanel buttonPanel;
    private JButton closeButton;

    public Settings(JFrame parent) {
        super(parent, "Settings", true);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(parent);

        initComponents();
        setComponents();
        addComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 10));
        tabbedPane = new JTabbedPane();

        // Editor tab
        editorPanel = new JPanel();
        themeCombo = new JComboBox<>(new String[]{"Dark", "Light"});

        // FreeMarker tab
        createOption = getOptionPanelCreator();
        freemarkerPanel = new JPanel();
        localeCombo = new JComboBox<>(new String[]{"en_US", "es_CO", "fr_FR"});
        timeZoneCombo = new JComboBox<>(new String[]{"America/Los_Angeles", "UTC"});

        // Buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButton = new JButton("Close");
    }

    private void setComponents() {
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Editor panel layout
        editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
        editorPanel.add(createOption.apply("Theme:", themeCombo));

        // FreeMarker panel layout
        freemarkerPanel.setLayout(new BoxLayout(freemarkerPanel, BoxLayout.Y_AXIS));
        Properties defaults = SettingsSingleton.defaultFreemarkerProperties();
        localeCombo.setSelectedItem(defaults.getProperty(SettingsSingleton.FREEMARKER_LOCALE));
        timeZoneCombo.setSelectedItem(defaults.getProperty(SettingsSingleton.FREEMARKER_TIME_ZONE));
        freemarkerPanel.add(createOption.apply("Locale:", localeCombo));
        freemarkerPanel.add(Box.createVerticalStrut(5));
        freemarkerPanel.add(createOption.apply("Time zone:", timeZoneCombo));
    }

    private void addComponents() {
        tabbedPane.addTab("Editor", editorPanel);
        tabbedPane.addTab("FreeMarker", freemarkerPanel);

        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Theme change listener (example)
        themeCombo.addActionListener(e -> {
            String selected = (String) themeCombo.getSelectedItem();
            // Save theme to settings and trigger UI update
            SettingsSingleton.setTheme(selected);
            // You should implement a method to repaint the main UI with the new theme
        });
    }

    private static BiFunction<String, JComboBox<String>, JPanel> getOptionPanelCreator() {
        Font compactFont = new Font("SansSerif", Font.PLAIN, 11);
        return (labelText, comboBox) -> {
            JLabel label = new JLabel(labelText);
            label.setMaximumSize(new Dimension(100, 20));
            label.setFont(compactFont);
            comboBox.setFont(compactFont);
            comboBox.setMaximumSize(new Dimension(150, 20));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(label);
            panel.add(Box.createHorizontalStrut(10));
            panel.add(comboBox);
            return panel;
        };
    }
}