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

    // Add per-area find/replace bars
    private JToolBar templateFindReplaceBar;
    private JToolBar dataFindReplaceBar;
    private JToolBar expectedFieldsFindReplaceBar;
    private JToolBar outputFindReplaceBar;

    private final TemplateValidator templateValidator = new TemplateValidator(new FreemarkerProcessor());


    public TemplateEditor() {

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
        // Arrays for easy access to components
        textAreas = new RSyntaxTextArea[]{templateInputTextArea, dataInputTextArea, expectedFieldsTextArea, outputJsonTextArea};

        // Initialize per-area find/replace bars
        templateFindReplaceBar = createFindReplaceBar(templateInputTextArea);
        dataFindReplaceBar = createFindReplaceBar(dataInputTextArea);
        expectedFieldsFindReplaceBar = createFindReplaceBar(expectedFieldsTextArea);
        outputFindReplaceBar = createFindReplaceBar(outputJsonTextArea);

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
        templateBottomPanel.setLayout(new BoxLayout(templateBottomPanel, BoxLayout.X_AXIS));


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

        // Template bottom panel addition
        templateBottomPanel.add(validateTemplateModelButton);
        templateBottomPanel.add(Box.createHorizontalStrut(10));
        templateBottomPanel.add(singleLineTemplateButton);
        templateBottomPanel.add(Box.createHorizontalGlue());
        templateBottomPanel.add(templatePositionLabel);

        // Usa método utilitario para crear paneles de entrada
        JPanel dataInputPanel = createInputPanel(dataFindReplaceBar, dataInputScrollPane, dataBottomPanel);
        JPanel templateInputPanel = createInputPanel(templateFindReplaceBar, templateInputScrollPane, templateBottomPanel);

        rightPanel.add(dataInputPanel, BorderLayout.CENTER);
        leftPanel.add(templateInputPanel, BorderLayout.CENTER);

        // Columns addition
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);

        JPanel expectedFieldsPanel = new JPanel(new BorderLayout());
        expectedFieldsPanel.add(expectedFieldsFindReplaceBar, BorderLayout.NORTH);
        expectedFieldsPanel.add(expectedFieldsScrollPane, BorderLayout.CENTER);


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

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputFindReplaceBar, BorderLayout.NORTH);
        outputPanel.add(outputJsonScrollPane, BorderLayout.CENTER);

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

        // Add to main panel
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add caret listeners to update position labels
        templateInputTextArea.addCaretListener(e -> updateCaretPosition(templateInputTextArea, templatePositionLabel));
        dataInputTextArea.addCaretListener(e -> updateCaretPosition(dataInputTextArea, dataPositionLabel));
        outputJsonTextArea.addCaretListener(e -> updateCaretPosition(outputJsonTextArea, outputPositionLabel));

    }

    // Método utilitario para crear paneles de entrada con barra superior y panel inferior opcional
    private JPanel createInputPanel(JToolBar findReplaceBar, JScrollPane scrollPane, JPanel bottomPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(findReplaceBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        if (bottomPanel != null) {
            panel.add(bottomPanel, BorderLayout.SOUTH);
        }
        return panel;
    }

    public void paintComponents() {

        // Apply theme and styles
        applyRSyntaxThemeToAllAreas(this);

    }

    // Helper to create a find/replace bar for a given RSyntaxTextArea
    private JToolBar createFindReplaceBar(RSyntaxTextArea area) {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        JTextField searchField = new JTextField(12);
        searchField.setToolTipText("Text to search for");
        JTextField replaceField = new JTextField(12);
        replaceField.setToolTipText("Text to replace with");

        JCheckBox regexCB = new JCheckBox();
        regexCB.setToolTipText("Enable regular expression search");
        regexCB.setText(".*");

        JCheckBox matchCaseCB = new JCheckBox();
        matchCaseCB.setToolTipText("Match case");
        matchCaseCB.setText("Cc");

        JLabel matchInfoLabel = new JLabel("0 de 0");
        matchInfoLabel.setPreferredSize(new Dimension(60, 20));
        matchInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Estado para coincidencia resaltada
        final int[] currentMatchIndex = {0}; // 1-based, 0 = ninguna resaltada

        bar.add(new JLabel("Buscar: "));
        bar.add(searchField);
        bar.add(new JLabel("Reemplazar: "));
        bar.add(replaceField);

        // Flecha hacia arriba (↑): buscar anterior
        JButton findPrevBtn = new JButton();
        findPrevBtn.setIcon(UIManager.getIcon("Table.ascendingSortIcon"));
        findPrevBtn.setToolTipText("Buscar anterior (↑)");
        findPrevBtn.setText("");
        findPrevBtn.addActionListener(e -> findOrReplaceAndHighlight(area, searchField, replaceField, regexCB, matchCaseCB, "findPrev", matchInfoLabel, findPrevBtn, currentMatchIndex));
        bar.add(findPrevBtn);

        // Flecha hacia abajo (↓): buscar siguiente
        JButton findNextBtn = new JButton();
        findNextBtn.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
        findNextBtn.setToolTipText("Buscar siguiente (↓)");
        findNextBtn.setText("");
        findNextBtn.addActionListener(e -> findOrReplaceAndHighlight(area, searchField, replaceField, regexCB, matchCaseCB, "findNext", matchInfoLabel, findNextBtn, currentMatchIndex));
        bar.add(findNextBtn);

        JButton replaceBtn = new JButton("Reemplazar");
        replaceBtn.setToolTipText("Replace current occurrence");
        replaceBtn.addActionListener(e -> findOrReplaceAndHighlight(area, searchField, replaceField, regexCB, matchCaseCB, "replace", matchInfoLabel, null, currentMatchIndex));
        bar.add(replaceBtn);

        JButton replaceAllBtn = new JButton("Reemplazar todo");
        replaceAllBtn.setToolTipText("Replace all occurrences");
        replaceAllBtn.addActionListener(e -> findOrReplaceAndHighlight(area, searchField, replaceField, regexCB, matchCaseCB, "replaceAll", matchInfoLabel, null, currentMatchIndex));
        bar.add(replaceAllBtn);

        bar.add(regexCB);
        bar.add(matchCaseCB);
        bar.add(Box.createHorizontalStrut(10));
        bar.add(matchInfoLabel);

        // Al cambiar búsqueda o texto, reinicia el contador visual
        Runnable resetMatchInfo = () -> {
            currentMatchIndex[0] = 0;
            updateMatchInfoLabelStatic(matchInfoLabel, 0, getTotalMatches(area, searchField, regexCB, matchCaseCB));
            area.select(0, 0); // Quita selección
        };
        addMatchInfoListeners(area, searchField, regexCB, matchCaseCB, resetMatchInfo);

        return bar;
    }

    // Nueva función: búsqueda y resaltado visual, contador solo cambia al buscar
    private void findOrReplaceAndHighlight(RSyntaxTextArea area, JTextField searchField, JTextField replaceField,
                              JCheckBox regexCB, JCheckBox matchCaseCB, String action, JLabel matchInfoLabel, JButton triggerButton, int[] currentMatchIndex) {
        String search = searchField.getText();
        if (search.isEmpty()) {
            updateMatchInfoLabelStatic(matchInfoLabel, 0, 0);
            area.select(0, 0);
            currentMatchIndex[0] = 0;
            return;
        }

        org.fife.ui.rtextarea.SearchContext context = new org.fife.ui.rtextarea.SearchContext();
        context.setSearchFor(search);
        context.setReplaceWith(replaceField.getText());
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        boolean forward = !"findPrev".equals(action);
        context.setSearchForward(forward);
        context.setWholeWord(false);

        java.util.List<int[]> matches = getAllMatches(area, context);
        int total = matches.size();

        if (total == 0) {
            updateMatchInfoLabelStatic(matchInfoLabel, 0, 0);
            area.select(0, 0);
            currentMatchIndex[0] = 0;
            if (triggerButton != null) showBalloonTooltip(triggerButton, "No se encontraron coincidencias");
            return;
        }

        switch (action) {
            case "findNext": {
                int idx = currentMatchIndex[0];
                idx = (idx < total) ? idx + 1 : 1; // Avanza, wrap
                currentMatchIndex[0] = idx;
                int[] m = matches.get(idx - 1);
                area.select(m[0], m[1]);
                updateMatchInfoLabelStatic(matchInfoLabel, idx, total);
                // Si dimos la vuelta, muestra tooltip
                if (idx == 1 && triggerButton != null) showBalloonTooltip(triggerButton, "Llegaste al final, continúa desde el inicio");
                break;
            }
            case "findPrev": {
                int idx = currentMatchIndex[0];
                idx = (idx > 1) ? idx - 1 : total; // Retrocede, wrap
                currentMatchIndex[0] = idx;
                int[] m = matches.get(idx - 1);
                area.select(m[0], m[1]);
                updateMatchInfoLabelStatic(matchInfoLabel, idx, total);
                // Si dimos la vuelta, muestra tooltip
                if (idx == total && triggerButton != null) showBalloonTooltip(triggerButton, "Llegaste al inicio, continúa desde el final");
                break;
            }
            case "replace": {
                if (currentMatchIndex[0] < 1 || currentMatchIndex[0] > total) return;
                int[] m = matches.get(currentMatchIndex[0] - 1);
                area.select(m[0], m[1]);
                area.replaceSelection(replaceField.getText());
                // Recalcula matches y resalta la misma posición si posible
                java.util.List<int[]> newMatches = getAllMatches(area, context);
                int newTotal = newMatches.size();
                int idx = Math.min(currentMatchIndex[0], newTotal);
                currentMatchIndex[0] = idx;
                if (newTotal > 0 && idx > 0) {
                    int[] nm = newMatches.get(idx - 1);
                    area.select(nm[0], nm[1]);
                } else {
                    area.select(0, 0);
                    currentMatchIndex[0] = 0;
                }
                updateMatchInfoLabelStatic(matchInfoLabel, currentMatchIndex[0], newTotal);
                break;
            }
            case "replaceAll": {
                for (int i = total - 1; i >= 0; i--) {
                    int[] m = matches.get(i);
                    area.select(m[0], m[1]);
                    area.replaceSelection(replaceField.getText());
                }
                area.select(0, 0);
                currentMatchIndex[0] = 0;
                updateMatchInfoLabelStatic(matchInfoLabel, 0, getAllMatches(area, context).size());
                break;
            }
        }
    }

    // Añade listeners para actualizar el contador de coincidencias al cambiar búsqueda o texto
    private void addMatchInfoListeners(RSyntaxTextArea area, JTextField searchField, JCheckBox regexCB, JCheckBox matchCaseCB, Runnable updateMatchInfo) {
        javax.swing.event.DocumentListener docListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateMatchInfo.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateMatchInfo.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateMatchInfo.run(); }
        };
        searchField.getDocument().addDocumentListener(docListener);
        area.getDocument().addDocumentListener(docListener);
        regexCB.addActionListener(e -> updateMatchInfo.run());
        matchCaseCB.addActionListener(e -> updateMatchInfo.run());
    }

    // Muestra un tooltip temporal tipo "balloon" sobre el botón
    private void showBalloonTooltip(JButton button, String message) {
        JToolTip tip = button.createToolTip();
        tip.setTipText(message);

        Point location = button.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(location, button.getParent());

        PopupFactory factory = PopupFactory.getSharedInstance();
        Popup popup = factory.getPopup(button, tip, button.getLocationOnScreen().x, button.getLocationOnScreen().y - tip.getPreferredSize().height - 5);
        popup.show();

        // Oculta el tooltip después de 1.5 segundos
        Timer timer = new Timer(1500, e -> popup.hide());
        timer.setRepeats(false);
        timer.start();
    }

    // Devuelve todas las coincidencias como lista de [start, end)
    private java.util.List<int[]> getAllMatches(RSyntaxTextArea area, org.fife.ui.rtextarea.SearchContext context) {
        String text = area.getText();
        String search = context.getSearchFor();
        boolean matchCase = context.getMatchCase();
        boolean regex = context.isRegularExpression();

        java.util.List<int[]> matches = new java.util.ArrayList<>();
        if (search == null || search.isEmpty() || text.isEmpty()) return matches;

        if (regex) {
            try {
                java.util.regex.Pattern pattern = matchCase
                        ? java.util.regex.Pattern.compile(search)
                        : java.util.regex.Pattern.compile(search, java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    matches.add(new int[]{matcher.start(), matcher.end()});
                }
            } catch (Exception e) {
                return matches;
            }
        } else {
            String searchFor = matchCase ? search : search.toLowerCase();
            String haystack = matchCase ? text : text.toLowerCase();
            int idx = 0;
            while ((idx = haystack.indexOf(searchFor, idx)) != -1) {
                matches.add(new int[]{idx, idx + searchFor.length()});
                idx += (!searchFor.isEmpty() ? searchFor.length() : 1);
            }
        }
        return matches;
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

    // Devuelve solo el total de coincidencias
    private int getTotalMatches(RSyntaxTextArea area, JTextField searchField, JCheckBox regexCB, JCheckBox matchCaseCB) {
        org.fife.ui.rtextarea.SearchContext context = new org.fife.ui.rtextarea.SearchContext();
        context.setSearchFor(searchField.getText());
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setWholeWord(false);
        return getAllMatches(area, context).size();
    }

    // Actualiza el label del contador de coincidencias
    private void updateMatchInfoLabelStatic(JLabel label, int current, int total) {
        label.setText((Math.max(current, 0)) + " de " + total);
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
        templateInputTextArea.setText(formatted);
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
