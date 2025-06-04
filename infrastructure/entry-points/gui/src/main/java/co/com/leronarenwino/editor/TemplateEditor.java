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
    private JLabel titleTemplateToolLabel;
    private JTextArea templateInputTextArea;
    private JScrollPane templateInputScrollPane;
    private JButton processTemplateButton;

    // Components for data input
    private JTextArea dataInputTextArea;
    private JScrollPane dataInputScrollPane;
    private JButton templateInputValidateButton;

    // Components for formatted output
    private JTextArea formattedTextArea;
    private JScrollPane formattedTextAreaScrollPane;
    private JButton formatButton;

    // Components for output
    private JTextArea outputJsonTextArea;
    private JScrollPane outputJsonScrollPane;
    private JButton runTemplateButton;

    private JPanel southButtonPanel;


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
        setTitle("Template Tool");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
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

        // Set layout and borders for main panels
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerCenterPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Initialize the left text field
        titleTemplateToolLabel = new JLabel("Template (Apache FreeMarker 2.3.34)", SwingConstants.CENTER);
        titleTemplateToolLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleTemplateToolLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        dataInputTextArea = new JTextArea(5, 75);
        dataInputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        dataInputTextArea.setLineWrap(false);
        dataInputTextArea.setToolTipText("Enter your data model in JSON format here");

        dataInputScrollPane = new JScrollPane(
                dataInputTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        dataInputScrollPane.setBorder(BorderFactory.createTitledBorder("Data"));

        templateInputTextArea = new JTextArea(2,0);
        templateInputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        templateInputTextArea.setLineWrap(false);
        templateInputTextArea.setToolTipText("Enter your FreeMarker template here");

        templateInputScrollPane = new JScrollPane(
                templateInputTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        templateInputScrollPane.setBorder(BorderFactory.createTitledBorder("Template"));

        templateInputValidateButton = new JButton("Validate Template");
        templateInputValidateButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        templateInputValidateButton.setToolTipText("Validate the FreeMarker template against the data model");

        outputJsonTextArea = new JTextArea(15, 25);
        outputJsonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        outputJsonTextArea.setEditable(false);
        outputJsonTextArea.setLineWrap(false);

        outputJsonScrollPane = new JScrollPane(
                outputJsonTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        processTemplateButton = new JButton("Process Template");
        runTemplateButton = new JButton("Evaluate Template");
        southButtonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        formattedTextArea = new JTextArea(15, 25);
        formattedTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        formattedTextArea.setEditable(false);
        formattedTextArea.setLineWrap(false);
        formattedTextArea.setWrapStyleWord(false);

        formattedTextAreaScrollPane = new JScrollPane(
                formattedTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        formatButton = new JButton("Format Template");

        formatButton.addActionListener(e -> {
            String input = templateInputTextArea.getText();
            String formatted = TemplateValidator.formatFreemarkerTemplate(input);
            formattedTextArea.setText(formatted);
        });

        templateInputTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateRows() {
                int lines = templateInputTextArea.getLineCount();
                templateInputTextArea.setRows(Math.min(10, Math.max(2, lines)));
                templateInputTextArea.revalidate();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateRows(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateRows(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateRows(); }
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

        // Adding components to the north panel
        northPanel.add(titleTemplateToolLabel, BorderLayout.NORTH);
        northPanel.add(templateInputScrollPane, BorderLayout.CENTER);

        // Adding components to the center panel
        centerCenterPanel.add(dataInputScrollPane, BorderLayout.WEST);
        centerCenterPanel.add(formattedTextAreaScrollPane, BorderLayout.CENTER);

        // Adding components to the east panel
        southCenterPanel.add(outputJsonScrollPane, BorderLayout.SOUTH);

        // Adding components to the south panel
        southButtonPanel.add(processTemplateButton);
        southButtonPanel.add(templateInputValidateButton);
        southButtonPanel.add(runTemplateButton);
        southButtonPanel.add(formatButton);
        southPanel.add(southButtonPanel, BorderLayout.CENTER);

        processTemplateButton.addActionListener(e -> processTemplateOutput());
        runTemplateButton.addActionListener(e -> generatePrettyJsonOutput());
        templateInputValidateButton.addActionListener(e -> validateTemplateFromInput());

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

    private void validateTemplateFromInput() {
        String output = outputJsonTextArea.getText();
        try {
            boolean isValid = TemplateValidator.validateJson(output);
            JOptionPane.showMessageDialog(this,
                    isValid ? "Template is valid!" : "Template is NOT valid.",
                    "Validation Result",
                    isValid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid JSON in output: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePrettyJsonOutput() {
        String templateContent = templateInputTextArea.getText();
        try {
            Map<String, Object> dataModel = getDataModelFromInput();
            String prettyJson = TemplateValidator.generatePrettyJsonOutput(templateContent, dataModel);
            outputJsonTextArea.setText(prettyJson);
        } catch (Exception ex) {
            outputJsonTextArea.setText("Error generating output: " + ex.getMessage());
        }
    }

    private Map<String, Object> getDataModelFromInput() throws Exception {
        String json = dataInputTextArea.getText();
        return TemplateValidator.parseJsonToDataModel(json);
    }

}
