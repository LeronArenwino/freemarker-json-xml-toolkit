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
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import utils.PropertiesManager;
import utils.SettingsSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static co.com.leronarenwino.editor.TemplateUtils.*;
import static co.com.leronarenwino.settings.Settings.PROPERTIES_FILE;
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
    private JPanel buttonPanel;
    private JPanel centerButtonsPanel;
    private Box rightDataPositionBox;

    // Panel for validation
    private JPanel validationPanel;
    private JButton validateFieldsButton;
    private JLabel validationResultLabel;

    private JPanel dataBottomPanel;
    private JButton validateDataModelButton;

    // Components for template input
    private RSyntaxTextArea templateInputTextArea;
    private RTextScrollPane templateInputScrollPane;

    // Components for data input
    private RSyntaxTextArea dataInputTextArea;
    private RTextScrollPane dataInputScrollPane;

    // Components for expected fields
    private RSyntaxTextArea expectedFieldsTextArea;
    private RTextScrollPane expectedFieldsScrollPane;

    // Components for output/result area
    private RSyntaxTextArea outputJsonTextArea;
    private RTextScrollPane outputJsonScrollPane;

    // Caret position labels
    private JLabel templatePositionLabel;
    private JLabel dataPositionLabel;
    private JLabel outputPositionLabel;

    // Buttons for actions
    private JButton processTemplateButton;
    private JButton formatJsonButton;
    private JButton clearOutputButton;

    // Last formatted output and data input
    private String lastFormattedResultOutput;
    private String lastFormattedDataInput;
    private String lastValidDataInput;

    private JPanel[] panels;
    private RSyntaxTextArea[] textAreas;
    private JLabel[] labels;
    private JButton[] buttons;
    private RTextScrollPane[] scrollPanes;
    private String[] scrollPaneTitles;

    private final TemplateValidator templateValidator = new TemplateValidator(new FreemarkerProcessor());


    public TemplateEditor() {

        // Set default properties from file or create new ones
        setSettingsFromProperties(
                PropertiesManager.loadProperties(
                        PROPERTIES_FILE,
                        defaultAppProperties()
                )
        );

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
        rightDataPositionBox = Box.createHorizontalBox();

        // Validation and button panels
        validationPanel = new JPanel();
        buttonPanel = new JPanel(new BorderLayout(5, 5));
        centerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dataBottomPanel = new JPanel();

        // Template input
        templateInputTextArea = new RSyntaxTextArea(10, 40);
        templateInputScrollPane = new RTextScrollPane(templateInputTextArea, true);

        // Data input
        dataInputTextArea = new RSyntaxTextArea(12, 40);
        dataInputScrollPane = new RTextScrollPane(dataInputTextArea, true);

        // Expected fields input
        expectedFieldsTextArea = new RSyntaxTextArea(1, 40);
        expectedFieldsScrollPane = new RTextScrollPane(expectedFieldsTextArea, false);

        // Output/result area
        outputJsonTextArea = new RSyntaxTextArea(12, 80);
        outputJsonScrollPane = new RTextScrollPane(outputJsonTextArea, true);

        // Validation result label
        validationResultLabel = new JLabel("Validation result will appear here.");

        // Buttons
        processTemplateButton = new JButton("Evaluate Template");
        clearOutputButton = new JButton("Clear Output");
        formatJsonButton = new JButton("Format to JSON");
        validateFieldsButton = new JButton("Validate Output Fields");
        validateDataModelButton = new JButton("Format to JSON");

        // Caret position labels
        templatePositionLabel = new JLabel("Line: 1  Column: 1");
        dataPositionLabel = new JLabel("Line: 1  Column: 1");
        outputPositionLabel = new JLabel("Line: 1  Column: 1");

        // Initialize arrays for easy access
        panels = new JPanel[]{mainPanel, columnsPanel, leftPanel, rightPanel, bottomPanel,
                validationPanel, buttonPanel, centerButtonsPanel, dataBottomPanel};
        textAreas = new RSyntaxTextArea[]{templateInputTextArea, dataInputTextArea, expectedFieldsTextArea, outputJsonTextArea};
        labels = new JLabel[]{validationResultLabel, templatePositionLabel, dataPositionLabel, outputPositionLabel};
        buttons = new JButton[]{processTemplateButton, formatJsonButton, clearOutputButton, validateFieldsButton, validateDataModelButton};
        scrollPanes = new RTextScrollPane[]{templateInputScrollPane, dataInputScrollPane, expectedFieldsScrollPane, outputJsonScrollPane};
        scrollPaneTitles = new String[]{"Template", "Data Model", "Expected fields", "Rendered Result"};

    }

    public void setComponents() {

        // Main setup
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left and right panels setup
        leftPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Template input text area setup
        templateInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        templateInputTextArea.setCodeFoldingEnabled(true);
        templateInputTextArea.setLineWrap(false);
        templateInputTextArea.setWrapStyleWord(false);
        templateInputTextArea.setHighlightCurrentLine(false);
        templateInputScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Template"
                )
        );

        // Data input text area setup
        dataInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        dataInputTextArea.setCodeFoldingEnabled(true);
        dataInputTextArea.setLineWrap(false);
        dataInputTextArea.setWrapStyleWord(false);
        dataInputTextArea.setHighlightCurrentLine(false);
        dataInputScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Data Model"
                )
        );

        // Expected fields text area setup
        expectedFieldsTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        expectedFieldsTextArea.setLineWrap(false);
        expectedFieldsTextArea.setCodeFoldingEnabled(true);
        expectedFieldsTextArea.setLineWrap(false);
        expectedFieldsTextArea.setWrapStyleWord(false);
        expectedFieldsTextArea.setHighlightCurrentLine(false);
        expectedFieldsScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1, true),
                        "Expected fields"
                )
        );
        expectedFieldsScrollPane.setFoldIndicatorEnabled(false);
        expectedFieldsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        expectedFieldsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Output JSON text area setup
        outputJsonTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        outputJsonTextArea.setCodeFoldingEnabled(false);
        outputJsonTextArea.setEditable(false);
        outputJsonTextArea.setLineWrap(true);
        outputJsonTextArea.setWrapStyleWord(true);
        outputJsonTextArea.setHighlightCurrentLine(false);
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

        // Validation panel setup
        validationPanel.setLayout(new BoxLayout(validationPanel, BoxLayout.X_AXIS));
        dataBottomPanel.setLayout(new BoxLayout(dataBottomPanel, BoxLayout.X_AXIS));

        // Set default configuration to JFrame
        setTitle("FreeMarker JSON/XML Toolkit (Apache FreeMarker 2.3.34)");
        setMinimumSize(new Dimension(600, 480));
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        // Data bottom panel addition
        dataBottomPanel.add(validateDataModelButton);
        dataBottomPanel.add(Box.createHorizontalGlue());
        dataBottomPanel.add(dataPositionLabel);

        // Left column addition
        leftPanel.add(dataInputScrollPane, BorderLayout.CENTER);
        leftPanel.add(dataBottomPanel, BorderLayout.SOUTH);

        // Right column addition
        rightPanel.add(templateInputScrollPane);
        rightDataPositionBox.add(Box.createHorizontalGlue());
        rightDataPositionBox.add(templatePositionLabel);
        rightPanel.add(rightDataPositionBox);

        // Columns addition
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);

        // Validation panel addition
        validationPanel.add(expectedFieldsScrollPane);
        validationPanel.add(Box.createHorizontalStrut(10));
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

        // Button actions
        validateDataModelButton.addActionListener(e -> formatDataInputJson());
        formatJsonButton.addActionListener(e -> formatJsonOutput());
        processTemplateButton.addActionListener(e -> processTemplateOutput());
        clearOutputButton.addActionListener(e -> outputJsonTextArea.setText(""));
        validateFieldsButton.addActionListener(e -> validateOutputFields());

        // Center buttons panel addition
        centerButtonsPanel.add(processTemplateButton);
        centerButtonsPanel.add(formatJsonButton);
        centerButtonsPanel.add(clearOutputButton);

        // Button panel addition
        buttonPanel.add(centerButtonsPanel, BorderLayout.WEST);
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

    public void paintComponents() {

        // Apply theme and styles
        UiConfig.applyTheme(SettingsSingleton.getTheme());
        UiConfig.setPanelColors(UiConfig.CURRENT_BG, panels);
        UiConfig.setTextAreaColors(UiConfig.CURRENT_BG, UiConfig.CURRENT_FG, textAreas);
        UiConfig.setLabelColors(UiConfig.CURRENT_FG, labels);
        UiConfig.styleButtons(UiConfig.CURRENT_BUTTON_BG, UiConfig.CURRENT_BUTTON_FG, buttons);
        UiConfig.customizeRTextScrollPanes(scrollPaneTitles, scrollPanes);
        UiConfig.setCaretColors(UiConfig.CURRENT_FG, textAreas);
        UiConfig.setScrollBarColors(UiConfig.CURRENT_SCROLLBAR_TRACK, scrollPanes);

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
            java.util.List<String> missing = validateFields(output, expectedFields);
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
        return parseDataModel(json);
    }

    private void formatJsonOutput() {
        TemplateUtils.formatJsonIfNeeded(outputJsonTextArea, lastFormattedResultOutput, formatted -> lastFormattedResultOutput = formatted);
    }

    private void formatDataInputJson() {
        formatJsonSafely(
                this,
                dataInputTextArea,
                lastFormattedDataInput,
                lastValidDataInput,
                formatted -> {
                    lastFormattedDataInput = formatted;
                    lastValidDataInput = formatted;
                }
        );
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
