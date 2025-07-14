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
import utils.SettingsSingleton;
import co.com.leronarenwino.utils.FindReplacePanel;

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
    private JPanel buttonPanel;
    private JPanel centerButtonsPanel;

    // Panel for validation
    private JPanel validationPanel;
    private JButton validateFieldsButton;
    private JLabel validationResultLabel;

    private JPanel dataBottomPanel;
    private JButton validateDataModelButton;
    private JPanel templateBottomPanel;
    private JButton validateTemplateModelButton;
    private JButton singleLineTemplateButton;

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

    private RSyntaxTextArea[] textAreas;

    // Find/replace panels per area
    private FindReplacePanel templateFindReplacePanel;
    private FindReplacePanel dataFindReplacePanel;
    private FindReplacePanel expectedFieldsFindReplacePanel;
    private FindReplacePanel outputFindReplacePanel;

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

        // Validation and button panels
        validationPanel = new JPanel();
        buttonPanel = new JPanel(new BorderLayout(5, 5));
        centerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dataBottomPanel = new JPanel();
        templateBottomPanel = new JPanel();

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
        validateTemplateModelButton = new JButton("Format Template");
        singleLineTemplateButton = new JButton("Single Line");

        // Caret position labels
        templatePositionLabel = new JLabel("Line: 1  Column: 1");
        dataPositionLabel = new JLabel("Line: 1  Column: 1");
        outputPositionLabel = new JLabel("Line: 1  Column: 1");

        // Initialize arrays for easy access
        textAreas = new RSyntaxTextArea[]{templateInputTextArea, dataInputTextArea, expectedFieldsTextArea, outputJsonTextArea};

        // Initialize find/replace bars (hidden by default)
        templateFindReplacePanel = new FindReplacePanel(templateInputTextArea);
        dataFindReplacePanel = new FindReplacePanel(dataInputTextArea);
        expectedFieldsFindReplacePanel = new FindReplacePanel(expectedFieldsTextArea);
        outputFindReplacePanel = new FindReplacePanel(outputJsonTextArea);

    }

    public void setComponents() {

        // Main setup
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left and right panels setup
        leftPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setLayout(new BorderLayout(5, 5));

        // Template input text area setup
        templateInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        templateInputTextArea.setCodeFoldingEnabled(true);
        templateInputTextArea.setLineWrap(false);
        templateInputTextArea.setWrapStyleWord(false);
        templateInputTextArea.setHighlightCurrentLine(false);
        templateInputScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Data input text area setup
        dataInputTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        dataInputTextArea.setCodeFoldingEnabled(true);
        dataInputTextArea.setLineWrap(false);
        dataInputTextArea.setWrapStyleWord(false);
        dataInputTextArea.setHighlightCurrentLine(false);
        dataInputScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Expected fields text area setup
        expectedFieldsTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        expectedFieldsTextArea.setLineWrap(false);
        expectedFieldsTextArea.setCodeFoldingEnabled(true);
        expectedFieldsTextArea.setLineWrap(false);
        expectedFieldsTextArea.setWrapStyleWord(false);
        expectedFieldsTextArea.setHighlightCurrentLine(false);
        expectedFieldsScrollPane.setBorder(BorderFactory.createEmptyBorder());
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
        outputJsonScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Buttons setup
        validationResultLabel.setForeground(Color.GRAY);
        validationResultLabel.setVerticalAlignment(SwingConstants.CENTER);
        validationResultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Validation panel setup
        validationPanel.setLayout(new BoxLayout(validationPanel, BoxLayout.X_AXIS));
        dataBottomPanel.setLayout(new BoxLayout(dataBottomPanel, BoxLayout.X_AXIS));
        templateBottomPanel.setLayout(new BoxLayout(templateBottomPanel, BoxLayout.X_AXIS));

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

        // Data bottom panel addition
        dataBottomPanel.add(validateDataModelButton);
        dataBottomPanel.add(Box.createHorizontalGlue());
        dataBottomPanel.add(dataPositionLabel);

        // Template bottom panel addition
        templateBottomPanel.add(validateTemplateModelButton);
        templateBottomPanel.add(Box.createHorizontalStrut(10));
        templateBottomPanel.add(singleLineTemplateButton);
        templateBottomPanel.add(Box.createHorizontalGlue());
        templateBottomPanel.add(templatePositionLabel);

        // Use utility method to create input panels
        JPanel dataInputPanel = createInputPanelWithTitle("Data Model", dataFindReplacePanel, dataInputScrollPane, dataBottomPanel);
        JPanel templateInputPanel = createInputPanelWithTitle("Template", templateFindReplacePanel, templateInputScrollPane, templateBottomPanel);

        rightPanel.add(dataInputPanel, BorderLayout.CENTER);
        leftPanel.add(templateInputPanel, BorderLayout.CENTER);

        // Columns addition
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);

        // Expected fields panel with title
        JPanel expectedFieldsPanel = new JPanel(new BorderLayout());
        JLabel expectedFieldsTitle = createSectionTitleLabel("Expected fields");
        expectedFieldsPanel.add(expectedFieldsTitle, BorderLayout.NORTH);

        JPanel expectedFieldsCenter = new JPanel(new BorderLayout());
        expectedFieldsCenter.add(expectedFieldsFindReplacePanel, BorderLayout.NORTH);
        expectedFieldsCenter.add(expectedFieldsScrollPane, BorderLayout.CENTER);
        expectedFieldsPanel.add(expectedFieldsCenter, BorderLayout.CENTER);

        // Validation panel addition
        validationPanel.setLayout(new BorderLayout(5, 5));
        validationPanel.add(expectedFieldsPanel, BorderLayout.CENTER);
        JPanel validationRightPanel = new JPanel();
        validationRightPanel.setLayout(new BoxLayout(validationRightPanel, BoxLayout.X_AXIS));
        validationRightPanel.add(Box.createHorizontalStrut(10));
        validationRightPanel.add(validationResultLabel);
        validationRightPanel.add(Box.createHorizontalStrut(10));
        validationRightPanel.add(validateFieldsButton);
        validationPanel.add(validationRightPanel, BorderLayout.EAST);

        // Output panel with title
        JPanel outputPanel = new JPanel(new BorderLayout());
        JLabel outputTitle = createSectionTitleLabel("Rendered Result");
        outputPanel.add(outputTitle, BorderLayout.NORTH);

        JPanel outputCenter = new JPanel(new BorderLayout());
        outputCenter.add(outputFindReplacePanel, BorderLayout.NORTH);
        outputCenter.add(outputJsonScrollPane, BorderLayout.CENTER);
        outputPanel.add(outputCenter, BorderLayout.CENTER);

        // Bottom panel addition
        bottomPanel.add(validationPanel, BorderLayout.NORTH);
        bottomPanel.add(outputPanel, BorderLayout.CENTER);

        // Button panel addition actions
        openSettingsItem.addActionListener(e -> {
            Settings settingsDialog = new Settings(this);
            settingsDialog.setVisible(true);
        });

        // Button actions
        validateDataModelButton.addActionListener(e -> formatDataInputJson());
        validateTemplateModelButton.addActionListener(e -> formatTemplateInputArea());
        singleLineTemplateButton.addActionListener(e -> setTemplateToSingleLine());
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

        // Keyboard shortcuts for showing/hiding find/replace bar
        addFindReplaceKeyBindings(templateInputTextArea, templateFindReplacePanel);
        addFindReplaceKeyBindings(dataInputTextArea, dataFindReplacePanel);
        addFindReplaceKeyBindings(expectedFieldsTextArea, expectedFieldsFindReplacePanel);
        addFindReplaceKeyBindings(outputJsonTextArea, outputFindReplacePanel);

        // Add to main panel
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add caret listeners to update position labels
        templateInputTextArea.addCaretListener(e -> updateCaretPosition(templateInputTextArea, templatePositionLabel));
        dataInputTextArea.addCaretListener(e -> updateCaretPosition(dataInputTextArea, dataPositionLabel));
        outputJsonTextArea.addCaretListener(e -> updateCaretPosition(outputJsonTextArea, outputPositionLabel));

    }

    // Utility to create input panel with top bar and optional bottom panel, now with title
    private JPanel createInputPanelWithTitle(String title, JPanel findReplacePanel, JScrollPane scrollPane, JPanel bottomPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = createSectionTitleLabel(title);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Wrap the scrollPane in a JPanel to ensure the center is always a JPanel
        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(findReplacePanel, BorderLayout.NORTH);
        centerPanel.add(scrollPanel, BorderLayout.CENTER);

        // Add vertical space between scrollPane and bottomPanel if bottomPanel exists
        if (bottomPanel != null) {
            JPanel southPanel = new JPanel(new BorderLayout());
            southPanel.add(Box.createVerticalStrut(8), BorderLayout.NORTH); // 8px vertical space
            southPanel.add(bottomPanel, BorderLayout.CENTER);
            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(southPanel, BorderLayout.SOUTH);
        } else {
            panel.add(centerPanel, BorderLayout.CENTER);
        }
        return panel;
    }

    // Utility to create a section title JLabel
    private JLabel createSectionTitleLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        return label;
    }

    // Keyboard shortcuts Ctrl+F/Ctrl+R to show/hide find/replace bar
    private void addFindReplaceKeyBindings(RSyntaxTextArea area, FindReplacePanel panel) {
        InputMap im = area.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = area.getActionMap();

        im.put(KeyStroke.getKeyStroke("control F"), "showFindBar");
        am.put("showFindBar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.showPanel(false);
            }
        });

        im.put(KeyStroke.getKeyStroke("control R"), "showReplaceBar");
        am.put("showReplaceBar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.showPanel(true);
            }
        });

        // Escape to close the bar if visible
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "hideFindReplaceBar");
        am.put("hideFindReplaceBar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (panel.isVisible()) panel.hidePanel();
            }
        });
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

    private void formatTemplateInputArea() {
        String template = templateInputTextArea.getText();
        String formatted = formatFreemarkerTemplateCombined(template);
        templateInputTextArea.beginAtomicEdit();
        try {
            templateInputTextArea.setText(formatted);
        } finally {
            templateInputTextArea.endAtomicEdit();
        }
    }

    private void setTemplateToSingleLine() {
        String template = templateInputTextArea.getText();
        String singleLine = TemplateValidator.toSingleLine(template);
        templateInputTextArea.setText(singleLine);
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
