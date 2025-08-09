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
import co.com.leronarenwino.utils.CaretUtil;
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

    // Component for template input
    private TemplatePanel templatePanel;

    // Components for data input
    private DataPanel dataPanel;

    // Components for expected fields
    private RSyntaxTextArea expectedFieldsTextArea;
    private RTextScrollPane expectedFieldsScrollPane;

    // Components for output/result area
    private RSyntaxTextArea outputJsonTextArea;
    private RTextScrollPane outputJsonScrollPane;

    // Button to toggle wrap in output area
    private JButton toggleWrapButton;
    private boolean isOutputWrapEnabled = false;

    // Caret position labels
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

        // Template input
        templatePanel = TemplatePanel.getInstance();

        // Data input
        dataPanel = DataPanel.getInstance();

        // Expected fields input
        expectedFieldsTextArea = new RSyntaxTextArea(1, 40);
        expectedFieldsScrollPane = new RTextScrollPane(expectedFieldsTextArea, false);

        // Output/result area
        outputJsonTextArea = new RSyntaxTextArea(12, 80);
        outputJsonScrollPane = new RTextScrollPane(outputJsonTextArea, true);

        // Button to toggle wrap in output area
        toggleWrapButton = new JButton();

        // Validation result label
        validationResultLabel = new JLabel("Validation result will appear here.");

        // Buttons
        processTemplateButton = new JButton("Evaluate Template");
        clearOutputButton = new JButton("Clear Output");
        formatJsonButton = new JButton("Format to JSON");
        validateFieldsButton = new JButton("Validate Output Fields");

        // Caret position labels
        outputPositionLabel = new JLabel("Line: 1  Column: 1");

        // Initialize arrays for easy access
        textAreas = new RSyntaxTextArea[]{templatePanel.getTextArea(), dataPanel.getTextArea(), expectedFieldsTextArea, outputJsonTextArea};

        // Initialize find/replace bars (hidden by default)
        templateFindReplacePanel = new FindReplacePanel(templatePanel.getTextArea());
        dataFindReplacePanel = new FindReplacePanel(dataPanel.getTextArea());
        expectedFieldsFindReplacePanel = new FindReplacePanel(expectedFieldsTextArea);
        outputFindReplacePanel = new FindReplacePanel(outputJsonTextArea);

    }

    public void setComponents() {

        // Main setup
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left and right panels setup
        leftPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setLayout(new BorderLayout(5, 5));


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
        outputJsonTextArea.setLineWrap(isOutputWrapEnabled);
        outputJsonTextArea.setWrapStyleWord(isOutputWrapEnabled);
        toggleWrapButton.setToolTipText("Wrap");
        outputJsonTextArea.setHighlightCurrentLine(false);
        outputJsonScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Buttons setup
        validationResultLabel.setForeground(Color.GRAY);
        validationResultLabel.setVerticalAlignment(SwingConstants.CENTER);
        validationResultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Validation panel setup
        validationPanel.setLayout(new BoxLayout(validationPanel, BoxLayout.X_AXIS));
        validateFieldsButton.setToolTipText("Validate Output Fields");

        // Set default configuration to JFrame
        setTitle("FreeMarker JSON/XML Toolkit (Apache FreeMarker 2.3.34)");
        setMinimumSize(new Dimension(600, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setJMenuBar(menuBar);
        setContentPane(mainPanel);
        setToggleWrapIcon();

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

        // Output panel with toggle wrap
        JPanel outputPanel = new JPanel(new BorderLayout());
        JLabel outputTitle = createSectionTitleLabel("Rendered Result");
        outputPanel.add(outputTitle, BorderLayout.NORTH);

        JPanel outputCenter = new JPanel(new BorderLayout());
        outputCenter.add(outputFindReplacePanel, BorderLayout.NORTH);
        outputCenter.add(outputJsonScrollPane, BorderLayout.CENTER);

        JPanel outputSidePanel = new JPanel();
        outputSidePanel.setLayout(new BoxLayout(outputSidePanel, BoxLayout.Y_AXIS));
        outputSidePanel.add(Box.createVerticalStrut(8));
        outputSidePanel.add(toggleWrapButton);
        outputSidePanel.add(Box.createVerticalGlue());
        outputPanel.add(outputSidePanel, BorderLayout.EAST);

        outputPanel.add(outputCenter, BorderLayout.CENTER);

        toggleWrapButton.addActionListener(e -> toggleOutputWrap());

        // Bottom panel addition
        bottomPanel.add(validationPanel, BorderLayout.NORTH);
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
        addFindReplaceKeyBindings(templatePanel.getTextArea(), templateFindReplacePanel);
        addFindReplaceKeyBindings(dataPanel.getTextArea(), dataFindReplacePanel);
        addFindReplaceKeyBindings(expectedFieldsTextArea, expectedFieldsFindReplacePanel);
        addFindReplaceKeyBindings(outputJsonTextArea, outputFindReplacePanel);

        // Add to main panel
        addMainPanelComponents();

        // Add caret listeners to update position labels
        outputJsonTextArea.addCaretListener(e -> updateCaretPosition(outputJsonTextArea, outputPositionLabel));

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
        String templateContent = templatePanel.getTextArea().getText();
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
        String json = dataPanel.getTextArea().getText().trim();
        return parseDataModel(json);
    }

    private void formatJsonOutput() {
        TemplateUtils.formatJsonIfNeeded(outputJsonTextArea, lastFormattedResultOutput, formatted -> lastFormattedResultOutput = formatted);
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

    private void updateCaretPosition(RSyntaxTextArea textArea, JLabel label) {
        CaretUtil.updateCaretPosition(textArea, label);
    }

    private void toggleOutputWrap() {
        isOutputWrapEnabled = !isOutputWrapEnabled;
        outputJsonTextArea.setLineWrap(isOutputWrapEnabled);
        outputJsonTextArea.setWrapStyleWord(isOutputWrapEnabled);
        setToggleWrapIcon();
    }

    private void setToggleWrapIcon() {
        if (isOutputWrapEnabled) {
            toggleWrapButton.setText("↵");
        } else {
            toggleWrapButton.setText("→");
        }
    }

}
