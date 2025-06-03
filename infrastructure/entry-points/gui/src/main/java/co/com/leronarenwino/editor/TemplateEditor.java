/*
 * This file is part of FreeMarker Tool.
 *
 * FreeMarker Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FreeMarker Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeMarker Tool. If not, see <https://www.gnu.org/licenses/>.
 */

package co.com.leronarenwino.editor;

import co.com.leronarenwino.TemplateValidator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TemplateEditor extends JFrame {

    // Main container panel
    private JPanel containerPanel;

    // Main panels for layout
    private JPanel westPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel centerPanel;

    // Center panels for layout
    private JPanel westCenterPanel;
    private JPanel northCenterPanel;
    private JPanel eastCenterPanel;
    private JPanel southCenterPanel;
    private JPanel centerCenterPanel;

    // Menu components
    private JMenuBar containerJMenuBar;
    private JMenu fileJMenu;
    private JMenu editJMenu;
    private JMenuItem settingsJMenuItem;

    // Components for the editor
    private JLabel titleTemplateValidatorLabel;
    private JTextArea templateInputTextArea;
    private JScrollPane templateInputScrollPane;
    private JButton templateInputValidateButton;

    // Components for formatted output
    private JTextArea formattedTextArea;
    private JButton formatButton;

    // Components for output
    private JTextArea outputJsonTextArea;
    private JButton runTemplateButton;

    public TemplateEditor() {

        // Initialize the components
        initComponents();

        // Add the components to the frame
        addComponents();

        // Make the window visible
        setVisible(true);

    }

    public void initComponents() {

        // Set default configuration to JFrame
        setTitle("FreeMarker Tool");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Menu components
        containerJMenuBar = new JMenuBar();
        fileJMenu = new JMenu("File");
        editJMenu = new JMenu("Edit");
        settingsJMenuItem = new JMenuItem("Settings");

        // Container panel configuration
        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(true);

        // Main panels
        northPanel = new JPanel(new BorderLayout());
        eastPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        westPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new BorderLayout());

        // Secondary panels
        northCenterPanel = new JPanel(new BorderLayout());
        eastCenterPanel = new JPanel(new BorderLayout());
        southCenterPanel = new JPanel(new BorderLayout());
        westCenterPanel = new JPanel(new BorderLayout());
        centerCenterPanel = new JPanel(new BorderLayout());

        // Initialize the left text field
        titleTemplateValidatorLabel = new JLabel("Template validator", SwingConstants.CENTER);
        titleTemplateValidatorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        templateInputTextArea = new JTextArea(15, 25);
        templateInputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        templateInputTextArea.setLineWrap(false);

        templateInputScrollPane = new JScrollPane(
                templateInputTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        templateInputValidateButton = new JButton("Validate Template");

        outputJsonTextArea = new JTextArea(15, 25);
        outputJsonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputJsonTextArea.setEditable(false);
        runTemplateButton = new JButton("Evaluate Template");

        formattedTextArea = new JTextArea(15, 25);
        formattedTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        formattedTextArea.setEditable(false);
        formattedTextArea.setLineWrap(true);
        formattedTextArea.setWrapStyleWord(true);

        formatButton = new JButton("Format Template");

        formatButton.addActionListener(e -> {
            String input = templateInputTextArea.getText();
            String formatted = TemplateValidator.formatFreemarkerTemplate(input);
            formattedTextArea.setText(formatted);
        });

    }

    public void addComponents() {

        // Add the menu items to the menu bar
        containerJMenuBar.add(fileJMenu);
        containerJMenuBar.add(editJMenu);
        editJMenu.add(settingsJMenuItem);
        setJMenuBar(containerJMenuBar);

        // Add the container panel to the frame
        setContentPane(containerPanel);

        // Adding main panels to containerPanel
        containerPanel.add(northPanel, BorderLayout.NORTH);
        containerPanel.add(eastPanel, BorderLayout.EAST);
        containerPanel.add(southPanel, BorderLayout.SOUTH);
        containerPanel.add(westPanel, BorderLayout.WEST);
        containerPanel.add(centerPanel, BorderLayout.CENTER);

        // Adding secondary panels to centerPanel
        centerPanel.add(northCenterPanel, BorderLayout.NORTH);
        centerPanel.add(eastCenterPanel, BorderLayout.EAST);
        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);
        centerPanel.add(westCenterPanel, BorderLayout.WEST);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);

        // Adding components to the main panels
        northPanel.add(titleTemplateValidatorLabel, BorderLayout.NORTH);
        northPanel.add(templateInputScrollPane, BorderLayout.CENTER);
        northPanel.add(templateInputValidateButton, BorderLayout.SOUTH);

        // Adding components to the center panel
        centerPanel.add(new JScrollPane(formattedTextArea), BorderLayout.CENTER);
        centerPanel.add(formatButton, BorderLayout.SOUTH);
        templateInputValidateButton.addActionListener(e -> validateTemplateFromInput());

        // Adding components to the east panel
        eastPanel.add(new JScrollPane(outputJsonTextArea), BorderLayout.CENTER);
        eastPanel.add(runTemplateButton, BorderLayout.SOUTH);
        runTemplateButton.addActionListener(e -> generatePrettyJsonOutput());

    }

    private void validateTemplateFromInput() {
        String templateContent = templateInputTextArea.getText();

        Map<String, Object> dataModel = buildDefaultDataModel();

        boolean isValid = TemplateValidator.validateTemplate(templateContent, dataModel);

        JOptionPane.showMessageDialog(this,
                isValid ? "Template is valid!" : "Template is NOT valid.",
                "Validation Result",
                isValid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private void generatePrettyJsonOutput() {
        String templateContent = templateInputTextArea.getText();

        Map<String, Object> dataModel = buildDefaultDataModel();

        try {
            String prettyJson = TemplateValidator.generatePrettyJsonOutput(templateContent, dataModel);
            outputJsonTextArea.setText(prettyJson);
        } catch (Exception ex) {
            outputJsonTextArea.setText("Error generating output: " + ex.getMessage());
        }
    }

    private Map<String, Object> buildDefaultDataModel() {
        Map<String, Object> dataModel = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("sessionId", "abc123");
        dataModel.put("headers", headers);
        return dataModel;
    }
}
