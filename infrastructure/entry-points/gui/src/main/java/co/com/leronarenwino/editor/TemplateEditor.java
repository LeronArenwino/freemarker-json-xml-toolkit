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

import co.com.leronarenwino.FreemarkerProcessor;
import co.com.leronarenwino.TemplateValidator;
import co.com.leronarenwino.settings.Settings;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import utils.SettingsSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static co.com.leronarenwino.TemplateValidator.formatFreemarkerTemplateCombined;
import static co.com.leronarenwino.editor.TemplateUtils.*;
import static co.com.leronarenwino.settings.Settings.PROPERTIES_FILE;
import static utils.PropertiesManager.loadProperties;
import static utils.SettingsSingleton.defaultAppProperties;
import static utils.SettingsSingleton.setSettingsFromProperties;

public class TemplateEditor extends JFrame {

    // Main container panel
    private JPanel mainPanel;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem exitItem;
    private JMenuItem openSettingsItem;

    // Panels for layout
    private JPanel columnsPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;

    // Component for template input
    private TemplatePanel templatePanel;

    // Components for data input
    private DataPanel dataPanel;

    // Components for expected fields
    private ExpectedFieldsPanel expectedFieldsPanel;

    // Components for output/result area
    private OutputPanel outputPanel;

    // Last formatted output and data input
    private String lastFormattedResultOutput;
    private String lastFormattedDataInput;
    private String lastValidDataInput;

    private RSyntaxTextArea[] textAreas;

    private final TemplateValidator templateValidator = new TemplateValidator(new FreemarkerProcessor());


    public TemplateEditor() {
        // Disable FlatLaf custom window decorations globally
        System.setProperty("flatlaf.useWindowDecorations", "false");

        // Set default properties from file or create new ones
        setSettingsFromProperties(
                loadProperties(
                        PROPERTIES_FILE,
                        defaultAppProperties()
                )
        );

        String theme = SettingsSingleton.getTheme();
        try {
            switch (theme) {
                case "Flat Dark" -> com.formdev.flatlaf.FlatDarkLaf.setup();
                case "Flat Light" -> com.formdev.flatlaf.FlatLightLaf.setup();
                case "Flat IntelliJ" -> com.formdev.flatlaf.FlatIntelliJLaf.setup();
                case "Flat Darcula" -> com.formdev.flatlaf.FlatDarculaLaf.setup();
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            System.err.println("Could not apply FlatLaf theme");
        }

        // Initialize components
        initComponents();

        // Set components
        setComponents();

        // Add components
        addComponents();

        // Paint components
        paintComponents();

    }

    public void initComponents() {

        // Main panels
        mainPanel = new JPanel(new BorderLayout(10, 10));
        columnsPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Menu bar and menu items
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        exitItem = new JMenuItem("Exit");
        openSettingsItem = new JMenuItem("Settings...");

        // Left, right, and options panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        bottomPanel = new JPanel(new BorderLayout(5, 5));

        // Template input
        templatePanel = TemplatePanel.getInstance();

        // Data input
        dataPanel = DataPanel.getInstance();

        // Expected fields input
        expectedFieldsPanel = ExpectedFieldsPanel.getInstance();

        // Output/result area
        outputPanel = OutputPanel.getInstance();


        // Initialize arrays for easy access
        textAreas = new RSyntaxTextArea[]{templatePanel.getTextArea(), dataPanel.getTextArea(), expectedFieldsPanel.getTextArea(), outputPanel.getTextArea()};

    }

    public void setComponents() {

        // Main setup
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left and right panels setup
        leftPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setLayout(new BorderLayout(5, 5));

        // Set default configuration to JFrame
        setTitle("FreeMarker JSON/XML Toolkit (Apache FreeMarker 2.3.34)");
        setMinimumSize(new Dimension(600, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setJMenuBar(menuBar);
        setContentPane(mainPanel);

    }

    public void addComponents() {
        // Menu bar addition
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(openSettingsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Add columns panel components
        addLeftPanelComponents();
        addRightPanelComponents();

        // Columns addition
        addColumnsPanelComponents();

        // Expected fields panel with title
        JPanel expectedFieldsPanelContainer = new JPanel(new BorderLayout());
        expectedFieldsPanelContainer.add(expectedFieldsPanel, BorderLayout.CENTER);


        // Bottom panel addition
        bottomPanel.add(expectedFieldsPanel, BorderLayout.NORTH);
        bottomPanel.add(outputPanel, BorderLayout.CENTER);

        // Button panel addition actions
        openSettingsItem.addActionListener(e -> {
            Settings settingsDialog = new Settings(this);
            settingsDialog.setVisible(true);
        });

        // Button actions
        dataPanel.getValidateDataModelButton().addActionListener(e -> formatDataInputJson());
        templatePanel.getFormatTemplateButton().addActionListener(e -> formatTemplateInputArea());
        templatePanel.getSingleLineButton().addActionListener(e -> setTemplateToSingleLine());
        outputPanel.getProcessTemplateButton().addActionListener(e -> processTemplateOutput());
        outputPanel.getFormatJsonButton().addActionListener(e -> formatJsonOutput());
        outputPanel.getClearOutputButton().addActionListener(e -> outputPanel.getTextArea().setText(""));
        expectedFieldsPanel.getValidateFieldsButton().addActionListener(e -> validateOutputFields());

        // Add to main panel
        addMainPanelComponents();

    }

    // Groups and adds all main sections to the mainPanel
    private void addMainPanelComponents() {
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Groups and adds left-side components (template area)
    private void addLeftPanelComponents() {
        leftPanel.add(templatePanel, BorderLayout.CENTER);
    }

    // Groups and adds right-side components (data area)
    private void addRightPanelComponents() {
        rightPanel.add(dataPanel, BorderLayout.CENTER);
    }

    // Groups and adds components to columnsPanel
    private void addColumnsPanelComponents() {
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);
    }

    public void paintComponents() {

        // Apply theme and styles
        applyRSyntaxThemeToAllAreas(this);

    }

    private void applyRSyntaxThemeToAllAreas(Component parent) {
        String rsyntaxTheme = SettingsSingleton.getRSyntaxTheme();
        String themePath = "/themes/" + rsyntaxTheme;
        for (RSyntaxTextArea area : textAreas) {
            try {
                UiConfig.applyRSyntaxTheme(area, themePath, parent);
            } catch (Exception ignored) {

            }
        }
    }

    private void processTemplateOutput() {
        String templateContent = templatePanel.getTextArea().getText();
        try {
            Map<String, Object> dataModel = getDataModelFromInput();
            String output = templateValidator.processTemplate(templateContent, dataModel);
            outputPanel.getTextArea().setText(output);
        } catch (Exception ex) {
            outputPanel.getTextArea().setText("Error processing template: " + ex.getMessage());
        }
    }

    private void validateOutputFields() {
        String output = outputPanel.getTextArea().getText();
        if (output.contains("\\\"")) {
            output = output.replace("\\\"", "\"");
        }
        String expectedFieldsText = expectedFieldsPanel.getTextArea().getText();
        if (expectedFieldsText.trim().isEmpty()) {
            expectedFieldsPanel.getValidationResultLabel().setText("No expected fields specified.");
            expectedFieldsPanel.getValidationResultLabel().setForeground(Color.GRAY);
            return;
        }
        String[] expectedFields = expectedFieldsText.split("\\s*,\\s*|\\s+");
        try {
            java.util.List<String> missing = validateFields(output, expectedFields);
            if (missing.isEmpty()) {
                expectedFieldsPanel.getValidationResultLabel().setText("All expected fields are present.");
                expectedFieldsPanel.getValidationResultLabel().setForeground(new java.awt.Color(0, 128, 0));
            } else {
                expectedFieldsPanel.getValidationResultLabel().setText("Missing fields: " + String.join(", ", missing));
                expectedFieldsPanel.getValidationResultLabel().setForeground(java.awt.Color.RED);
            }
        } catch (Exception e) {
            expectedFieldsPanel.getValidationResultLabel().setText("Invalid JSON output.");
            expectedFieldsPanel.getValidationResultLabel().setForeground(java.awt.Color.RED);
        }
    }

    private Map<String, Object> getDataModelFromInput() throws Exception {
        String json = dataPanel.getTextArea().getText().trim();
        return parseDataModel(json);
    }

    private void formatJsonOutput() {
        TemplateUtils.formatJsonIfNeeded(outputPanel.getTextArea(), lastFormattedResultOutput, formatted -> lastFormattedResultOutput = formatted);
    }

    private void formatDataInputJson() {
        formatJsonSafely(
                this,
                dataPanel.getTextArea(),
                lastFormattedDataInput,
                lastValidDataInput,
                formatted -> {
                    lastFormattedDataInput = formatted;
                    lastValidDataInput = formatted;
                }
        );
    }

    private void formatTemplateInputArea() {
        String template = templatePanel.getTextArea().getText();
        String formatted = formatFreemarkerTemplateCombined(template);
        templatePanel.getTextArea().beginAtomicEdit();
        try {
            templatePanel.getTextArea().setText(formatted);
        } finally {
            templatePanel.getTextArea().endAtomicEdit();
        }
    }

    private void setTemplateToSingleLine() {
        String template = templatePanel.getTextArea().getText();
        String singleLine = TemplateValidator.toSingleLine(template);
        singleLine = singleLine.replaceAll("}>\\s+\\{", "}>{");
        templatePanel.getTextArea().setText(singleLine);
    }


}
