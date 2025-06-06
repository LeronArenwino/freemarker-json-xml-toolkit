/*
 * This file is part of Template Tool.
 *
 * Template Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Template Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Template Tool. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino.editor;

import co.com.leronarenwino.TemplateValidator;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TemplateEditor extends JFrame {

    // Main container panel
    private JPanel mainPanel;

    // Panels for layout
    private JPanel columnsPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;
    private JPanel buttonPanel;
    private JButton clearOutputButton;

    // Panel for validation
    private JPanel validationPanel;
    private JButton validateFieldsButton;
    private JLabel validationResultLabel;

    // Components for template input
    private JTextArea templateInputTextArea;
    private JScrollPane templateInputScrollPane;
    private JButton processTemplateButton;

    // Components for data input
    private JTextArea dataInputTextArea;
    private JScrollPane dataInputScrollPane;

    // Components for expected fields
    private JTextArea expectedFieldsTextArea;
    private JScrollPane expectedFieldsScrollPane;

    // Components for output/result area
    private JTextArea outputJsonTextArea;
    private JScrollPane outputJsonScrollPane;

    public TemplateEditor() {

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

        // Left, right, and options panels
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        bottomPanel = new JPanel(new BorderLayout(5, 5));

        validationPanel = new JPanel();
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Template input
        templateInputTextArea = new JTextArea(10, 40);
        templateInputScrollPane = new JScrollPane(templateInputTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Data input
        dataInputTextArea = new JTextArea(12, 40);
        dataInputScrollPane = new JScrollPane(dataInputTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Right panel options
        // (You can add more fields as needed)
        // Example for validation area
        expectedFieldsTextArea = new JTextArea(6, 40);
        expectedFieldsScrollPane = new JScrollPane(expectedFieldsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Output/result area
        outputJsonTextArea = new JTextArea(8, 80);
        outputJsonScrollPane = new JScrollPane(outputJsonTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Validation result label
        validationResultLabel = new JLabel("Validation result will appear here.");

        // Buttons
        processTemplateButton = new JButton("Evaluate Template");
        clearOutputButton = new JButton("Clear Output");
        validateFieldsButton = new JButton("Validate Output Fields");

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
        setTitle("Template Tool (Apache FreeMarker 2.3.34)");
        setMinimumSize(new Dimension(600, 480));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

    }

    public void addComponents() {

        // Left column addition
        leftPanel.add(templateInputScrollPane);

        // Right column addition
        rightPanel.add(dataInputScrollPane);
        rightPanel.add(Box.createVerticalStrut(10));
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
        processTemplateButton.addActionListener(e -> processTemplateOutput());
        clearOutputButton.addActionListener(e -> outputJsonTextArea.setText(""));
        validateFieldsButton.addActionListener(e -> validateOutputFields());

        // Button panel addition
        buttonPanel.add(processTemplateButton);
        buttonPanel.add(clearOutputButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

    }


    private void processTemplateOutput() {
        String templateContent = templateInputTextArea.getText();
        try {
            Map<String, Object> dataModel = getDataModelFromInput();
            String output = TemplateValidator.processTemplate(templateContent, dataModel);
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

}
