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
    private JPanel optionsPanel;
    private BiFunction<String, JComboBox<String>, JPanel> createOption;

    private JPanel buttonPanel;
    private JButton closeButton;

    private JComboBox<String> localeCombo;
    private JComboBox<String> timeZoneCombo;

    public Settings(JFrame parent) {
        super(parent, "FreeMarker Options", true);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        initComponents();

        setComponents();

        addComponents();

    }

    private void initComponents() {

        // Initializing panels and buttons
        mainPanel = new JPanel();
        createOption = getOptionPanelCreator();
        optionsPanel = new JPanel();
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButton = new JButton("Close");

        // Initializing combo boxes with options
        localeCombo = new JComboBox<>(new String[]{"en_US", "es_CO", "fr_FR"});
        timeZoneCombo = new JComboBox<>(new String[]{"America/Los_Angeles", "UTC"});

    }

    private void setComponents() {

        // Setting layout and borders for the main panel and options panel
        mainPanel.setLayout(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        setContentPane(mainPanel);

    }

    private void addComponents() {

        // Adding options to the options panel
        Properties defaults = SettingsSingleton.defaultFreemarkerProperties();

        // Setting default values for combo boxes from the defaults properties
        localeCombo.setSelectedItem(defaults.getProperty(SettingsSingleton.FREEMARKER_LOCALE));
        timeZoneCombo.setSelectedItem(defaults.getProperty(SettingsSingleton.FREEMARKER_TIME_ZONE));

        // Creating and adding option panels to the options panel
        optionsPanel.add(createOption.apply("Locale:", localeCombo));
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(createOption.apply("Time zone:", timeZoneCombo));

        // Adding the close button to the button panel
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Adding the options panel and button panel to the main panel
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    }

    private static BiFunction<String, JComboBox<String>, JPanel> getOptionPanelCreator() {
        Font compactFont = new Font("SansSerif", Font.PLAIN, 11);

        // Función auxiliar para crear sub paneles compactos de opción
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
