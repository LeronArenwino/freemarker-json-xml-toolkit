/*
 * This file is part of FreeMarker JSON Toolkit.
 *
 * FreeMarker JSON Toolkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FreeMarker JSON Toolkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeMarker JSON Toolkit. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino.editor;

import co.com.leronarenwino.FreemarkerProcessor;
import co.com.leronarenwino.TemplateValidator;
import co.com.leronarenwino.settings.Settings;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import utils.PropertiesManager;
import utils.SettingsSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

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
    private JPanel buttonPanel;
    private JPanel centerButtonsPanel;
    private Box leftTemplatePositionBox;
    private Box rightDataPositionBox;

    // Panel for validation
    private JPanel validationPanel;
    private JButton validateFieldsButton;
    private JLabel validationResultLabel;

    // Components for template input
    private RSyntaxTextArea templateInputTextArea;
    private JScrollPane templateInputScrollPane;

    // Components for data input
    private RSyntaxTextArea dataInputTextArea;
    private JScrollPane dataInputScrollPane;

    // Components for expected fields
    private RSyntaxTextArea expectedFieldsTextArea;
    private JScrollPane expectedFieldsScrollPane;

    // Components for output/result area
    private RSyntaxTextArea outputJsonTextArea;
    private JScrollPane outputJsonScrollPane;

    // Caret position labels
    private JLabel templatePositionLabel;
    private JLabel dataPositionLabel;
    private JLabel outputPositionLabel;

    // Buttons for actions
    private JButton processTemplateButton;
    private JButton formatJsonButton;
    private JButton clearOutputButton;

    private final TemplateValidator templateValidator = new TemplateValidator(new FreemarkerProcessor());


    public TemplateEditor() {

        SettingsSingleton.setSettingsFromProperties(
                PropertiesManager.loadProperties(
                        "freemarker.properties",
                        SettingsSingleton.defaultFreemarkerProperties()
                )
        );

        // Initialize components
        initComponents();

        // Set components
        setComponents();

        // Add components
        addComponents();

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
        leftTemplatePositionBox = Box.createHorizontalBox();
        rightDataPositionBox = Box.createHorizontalBox();

        // Validation and button panels
        validationPanel = new JPanel();
        buttonPanel = new JPanel(new BorderLayout(5, 5));
        centerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Template input
        templateInputTextArea = new RSyntaxTextArea(10, 40);
        templateInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        templateInputTextArea.setCodeFoldingEnabled(true);
        templateInputScrollPane = new RTextScrollPane(templateInputTextArea, true);

        // Data input
        dataInputTextArea = new RSyntaxTextArea(12, 40);
        dataInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        dataInputTextArea.setCodeFoldingEnabled(true);
        dataInputScrollPane = new RTextScrollPane(dataInputTextArea, true);

        // Right panel options
        // (You can add more fields as needed)
        // Example for validation area
        expectedFieldsTextArea = new RSyntaxTextArea(6, 40);
        expectedFieldsTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        expectedFieldsScrollPane = new RTextScrollPane(expectedFieldsTextArea, true);

        // Output/result area
        outputJsonTextArea = new RSyntaxTextArea(8, 80);
        outputJsonTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        outputJsonTextArea.setCodeFoldingEnabled(false);
        outputJsonScrollPane = new RTextScrollPane(outputJsonTextArea, true);

        // Validation result label
        validationResultLabel = new JLabel("Validation result will appear here.");

        // Buttons
        processTemplateButton = new JButton("Evaluate Template");
        clearOutputButton = new JButton("Clear Output");
        formatJsonButton = new JButton("Format JSON");
        validateFieldsButton = new JButton("Validate Output Fields");

        // Caret position labels
        templatePositionLabel = new JLabel("Line: 1  Column: 1");
        dataPositionLabel = new JLabel("Line: 1  Column: 1");
        outputPositionLabel = new JLabel("Line: 1  Column: 1");

    }

    public void setComponents() {

        // Main setup
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left and right panels setup
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Template input text area setup
        templateInputTextArea.setLineWrap(false);
        templateInputTextArea.setWrapStyleWord(false);
        templateInputScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Template"
                )
        );

        // Data input text area setup
        dataInputTextArea.setLineWrap(false);
        dataInputTextArea.setWrapStyleWord(false);
        dataInputScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Data Model"
                )
        );

        // Expected fields text area setup
        expectedFieldsTextArea.setLineWrap(true);
        expectedFieldsTextArea.setWrapStyleWord(true);
        expectedFieldsScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Expected fields"
                )
        );

        // Output JSON text area setup
        outputJsonTextArea.setEditable(false);
        outputJsonTextArea.setLineWrap(true);
        outputJsonTextArea.setWrapStyleWord(true);
        outputJsonScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Rendered Result"
                )
        );

        // Buttons setup
        validationResultLabel.setForeground(Color.GRAY);
        validationResultLabel.setVerticalAlignment(SwingConstants.CENTER);
        validationResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        validationResultLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Validation panel setup
        validationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Set default configuration to JFrame
        setTitle("FreeMarker JSON Toolkit (Apache FreeMarker 2.3.34)");
        setMinimumSize(new Dimension(600, 480));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        // Left column addition
        leftPanel.add(dataInputScrollPane);
        leftTemplatePositionBox.add(Box.createHorizontalGlue());
        leftTemplatePositionBox.add(dataPositionLabel);
        leftPanel.add(leftTemplatePositionBox);

        // Right column addition
        rightPanel.add(templateInputScrollPane);
        rightDataPositionBox.add(Box.createHorizontalGlue());
        rightDataPositionBox.add(templatePositionLabel);
        rightPanel.add(rightDataPositionBox);
        rightPanel.add(expectedFieldsScrollPane);

        // Columns addition
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);

        // Validation panel addition
        validationPanel.add(validationResultLabel);
        validationPanel.add(Box.createHorizontalStrut(10));
        validationPanel.add(validateFieldsButton);

        // Bottom panel addition
        bottomPanel.add(validationPanel, BorderLayout.NORTH);
        bottomPanel.add(outputJsonScrollPane, BorderLayout.CENTER);

        // Button panel addition actions
        openSettingsItem.addActionListener(e -> {
            Settings settingsDialog = new Settings(this);
            settingsDialog.setVisible(true);
        });
        formatJsonButton.addActionListener(e -> formatJsonOutput());
        processTemplateButton.addActionListener(e -> processTemplateOutput());
        clearOutputButton.addActionListener(e -> outputJsonTextArea.setText(""));
        validateFieldsButton.addActionListener(e -> validateOutputFields());

        // Center buttons panel addition
        centerButtonsPanel.add(processTemplateButton);
        centerButtonsPanel.add(formatJsonButton);
        centerButtonsPanel.add(clearOutputButton);

        // Button panel addition
        buttonPanel.add(centerButtonsPanel, BorderLayout.CENTER);
        buttonPanel.add(outputPositionLabel, BorderLayout.EAST);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add caret listeners to update position labels
        templateInputTextArea.addCaretListener(e -> updateCaretPosition(templateInputTextArea, templatePositionLabel));
        dataInputTextArea.addCaretListener(e -> updateCaretPosition(dataInputTextArea, dataPositionLabel));
        outputJsonTextArea.addCaretListener(e -> updateCaretPosition(outputJsonTextArea, outputPositionLabel));

    }


    private void processTemplateOutput() {
        String templateContent = templateInputTextArea.getText();
        try {
            Map<String, Object> dataModel = getDataModelFromInput();
            String output = templateValidator.processTemplate(templateContent, dataModel);
            outputJsonTextArea.setText(output);
        } catch (Exception ex) {
            outputJsonTextArea.setText("Error processing template: " + ex.getMessage());
        }
    }

    private void validateOutputFields() {
        String output = outputJsonTextArea.getText();
        if (output.contains("\\\"")) {
            output = output.replace("\\\"", "\"");
        }
        String expectedFieldsText = expectedFieldsTextArea.getText();
        if (expectedFieldsText.trim().isEmpty()) {
            validationResultLabel.setText("No expected fields specified.");
            validationResultLabel.setForeground(Color.GRAY);
            return;
        }
        String[] expectedFields = expectedFieldsText.split("\\s*,\\s*|\\s+");
        try {
            java.util.List<String> missing = TemplateValidator.validateFieldsPresent(output, expectedFields);
            if (missing.isEmpty()) {
                validationResultLabel.setText("All expected fields are present.");
                validationResultLabel.setForeground(new java.awt.Color(0, 128, 0));
            } else {
                validationResultLabel.setText("Missing fields: " + String.join(", ", missing));
                validationResultLabel.setForeground(java.awt.Color.RED);
            }
        } catch (Exception e) {
            validationResultLabel.setText("Invalid JSON output.");
            validationResultLabel.setForeground(java.awt.Color.RED);
        }
    }

    private Map<String, Object> getDataModelFromInput() throws Exception {
        String json = dataInputTextArea.getText().trim();
        return TemplateValidator.parseJsonToDataModel(json.isEmpty() ? "{}" : json);
    }

    private void formatJsonOutput() {
        String text = outputJsonTextArea.getText();
        if (text.trim().isEmpty()) return;
        try {
            String pretty = TemplateValidator.formatJson(text);
            outputJsonTextArea.setText(pretty);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid JSON: " + ex.getMessage(), "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCaretPosition(RSyntaxTextArea textArea, JLabel label) {
        int caretPos = textArea.getCaretPosition();
        try {
            int line = textArea.getLineOfOffset(caretPos) + 1;
            int column = caretPos - textArea.getLineStartOffset(line - 1) + 1;
            label.setText("Line: " + line + "  Column: " + column);
        } catch (Exception ex) {
            label.setText("Line: ?, Column: ?");
        }
    }


}
