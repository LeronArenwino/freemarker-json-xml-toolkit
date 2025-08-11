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

import utils.PropertiesManager;
import utils.SettingsSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static co.com.leronarenwino.config.FreemarkerConfigProvider.reloadConfiguration;
import static utils.PropertiesManager.loadProperties;
import static utils.SettingsSingleton.defaultAppProperties;

public class Settings extends JDialog {

    // Constants for property keys
    public static final String PROPERTIES_FILE = "config.properties";

    // Main panel and tabbed pane
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

    // Buttons
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton okButton;
    private JButton applyButton;

    // RSyntax panel for syntax highlighting themes
    private JPanel rsyntaxPanel;
    private JComboBox<String> rsyntaxThemeCombo;

    // Properties for storing settings
    private Properties props;

    private static final Map<String, String> THEME_DISPLAY_TO_FILE = Map.of(
            "Dark", "dark.xml",
            "Default", "default.xml",
            "Eclipse", "eclipse.xml",
            "IDEA", "idea.xml",
            "IDEA Dark", "idea-dark.xml",
            "Monokai", "monokai.xml",
            "Monokai Dark", "monokai-dark.xml",
            "VS", "vs.xml",
            "VS Dark", "vs-dark.xml"
    );

    public Settings(JFrame parent) {
        super(parent, "Settings", true);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(parent);

        initComponents();
        setComponents();
        addComponents();
        loadSettings();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 10));
        tabbedPane = new JTabbedPane();

        // Editor tab
        editorPanel = new JPanel();
        themeCombo = new JComboBox<>(new String[]{
                "Flat Light", "Flat Dark", "Flat IntelliJ", "Flat Darcula"
        });

        // FreeMarker tab
        createOption = getOptionPanelCreator();
        freemarkerPanel = new JPanel();
        localeCombo = new JComboBox<>(new String[]{"en_US", "es_CO", "fr_FR"});
        timeZoneCombo = new JComboBox<>(new String[]{"America/Los_Angeles", "UTC"});

        // Buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        applyButton = new JButton("Apply");

        // RSyntax panel for syntax highlighting themes
        rsyntaxPanel = new JPanel();
        java.util.List<String> sortedThemes = new java.util.ArrayList<>(THEME_DISPLAY_TO_FILE.keySet());
        java.util.Collections.sort(sortedThemes);
        rsyntaxThemeCombo = new JComboBox<>(sortedThemes.toArray(new String[0]));

        props = loadProperties(PROPERTIES_FILE, defaultAppProperties());
        if (props.isEmpty()) {
            props = defaultAppProperties();
        }

    }

    private void setComponents() {
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Editor panel layout
        editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
        editorPanel.add(createOption.apply("Theme:", themeCombo));

        // RSyntax panel layout
        rsyntaxPanel.setLayout(new BoxLayout(rsyntaxPanel, BoxLayout.Y_AXIS));
        rsyntaxPanel.add(createOption.apply("RSyntax Theme:", rsyntaxThemeCombo));

        // FreeMarker panel layout
        freemarkerPanel.setLayout(new BoxLayout(freemarkerPanel, BoxLayout.Y_AXIS));

        // Set default locale and time zone from properties
        localeCombo.setSelectedItem(props.getProperty(SettingsSingleton.FREEMARKER_LOCALE));
        timeZoneCombo.setSelectedItem(props.getProperty(SettingsSingleton.FREEMARKER_TIME_ZONE));
        freemarkerPanel.add(createOption.apply("Locale:", localeCombo));
        freemarkerPanel.add(Box.createVerticalStrut(5));
        freemarkerPanel.add(createOption.apply("Time zone:", timeZoneCombo));
    }

    private void addComponents() {
        tabbedPane.addTab("Editor", editorPanel);
        tabbedPane.addTab("Syntax Theme", rsyntaxPanel);
        tabbedPane.addTab("FreeMarker", freemarkerPanel);

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(applyButton);

        okButton.addActionListener(e -> {
            saveSettings();
            loadSettings();
            applyThemeToParent();
            reloadConfiguration();
            dispose();
        });
        applyButton.addActionListener(e -> {
            saveSettings();
            loadSettings();
            applyThemeToParent();
            reloadConfiguration();
        });

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set default theme
        themeCombo.addActionListener(e -> {
            String selected = (String) themeCombo.getSelectedItem();
            try {
                switch (Objects.requireNonNull(selected)) {
                    case "Flat Light" -> UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    case "Flat Dark" -> UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                    case "Flat IntelliJ" -> UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
                    case "Flat Darcula" -> UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
                }
                // Update UI for all windows
                SwingUtilities.updateComponentTreeUI(getParent());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to apply theme: " + ex.getMessage());
            }
        });

        // Set default RSyntax theme
        rsyntaxThemeCombo.addActionListener(e -> {
            String selectedDisplay = (String) rsyntaxThemeCombo.getSelectedItem();
            String fileName = THEME_DISPLAY_TO_FILE.get(selectedDisplay);
            if (fileName != null) {
                SettingsSingleton.setRSyntaxTheme(fileName);
                applyThemeToParent();
            }
        });
    }

    private void saveSettings() {
        Properties props = loadProperties(PROPERTIES_FILE, defaultAppProperties());
        props.setProperty(SettingsSingleton.FREEMARKER_LOCALE, (String) localeCombo.getSelectedItem());
        props.setProperty(SettingsSingleton.FREEMARKER_TIME_ZONE, (String) timeZoneCombo.getSelectedItem());
        props.setProperty(SettingsSingleton.APP_THEME, (String) themeCombo.getSelectedItem());
        String selectedDisplay = (String) rsyntaxThemeCombo.getSelectedItem();
        String fileName = THEME_DISPLAY_TO_FILE.get(selectedDisplay);
        props.setProperty(SettingsSingleton.RSYNTAX_THEME, fileName);
        PropertiesManager.saveProperties(PROPERTIES_FILE, props);
    }

    private void loadSettings() {
        Properties props = loadProperties(PROPERTIES_FILE, defaultAppProperties());

        if (props.isEmpty()) {
            props = defaultAppProperties();
        }

        localeCombo.setSelectedItem(props.getProperty(SettingsSingleton.FREEMARKER_LOCALE));
        timeZoneCombo.setSelectedItem(props.getProperty(SettingsSingleton.FREEMARKER_TIME_ZONE));
        themeCombo.setSelectedItem(props.getProperty(SettingsSingleton.APP_THEME));
        String fileName = props.getProperty(SettingsSingleton.RSYNTAX_THEME, "idea.xml");
        String displayName = THEME_FILE_TO_DISPLAY.getOrDefault(fileName, "IDEA");
        rsyntaxThemeCombo.setSelectedItem(displayName);
        SettingsSingleton.setRSyntaxTheme(fileName);
        SettingsSingleton.setSettingsFromProperties(props);

    }

    private static final Map<String, String> THEME_FILE_TO_DISPLAY = THEME_DISPLAY_TO_FILE.entrySet()
            .stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    private void applyThemeToParent() {
        if (getParent() instanceof co.com.leronarenwino.editor.TemplateEditor editor) {
            editor.paintComponents();
            editor.repaint();
        }
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