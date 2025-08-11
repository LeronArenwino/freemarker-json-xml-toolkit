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

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;

public class ExpectedFieldsPanel extends EditorPanel {
    private static ExpectedFieldsPanel instance;
    private JButton validateFieldsButton;
    private JLabel validationResultLabel;

    private ExpectedFieldsPanel() {
        super("Expected fields");
    }

    @Override
    protected void initComponents() {
        validateFieldsButton = new JButton("🔍");
        validateFieldsButton.setToolTipText("Validate Expected Fields");
        validationResultLabel = new JLabel("Validation result will appear here");
        validationResultLabel.setForeground(Color.GRAY);
    }

    @Override
    protected void setComponents() {
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        textArea.setLineWrap(false);
        textArea.setCodeFoldingEnabled(true);
        textArea.setWrapStyleWord(false);
        textArea.setHighlightCurrentLine(false);
        scrollPane.setFoldIndicatorEnabled(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        positionLabel.setEnabled(false);
        positionLabel.setVisible(false);
    }

    @Override
    protected void addComponents() {
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(validationResultLabel);
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(validateFieldsButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.EAST);
    }

    public static ExpectedFieldsPanel getInstance() {
        if (instance == null) {
            instance = new ExpectedFieldsPanel();
        }
        return instance;
    }

    public JButton getValidateFieldsButton() {
        return validateFieldsButton;
    }

    public void validateFields(String output) {
        if (output.contains("\\\"")) {
            output = output.replace("\\\"", "\"");
        }

        String expectedFieldsText = textArea.getText();
        if (expectedFieldsText.trim().isEmpty()) {
            validationResultLabel.setText("No expected fields specified");
            validationResultLabel.setForeground(Color.GRAY);
            return;
        }

        String[] expectedFields = expectedFieldsText.split("\\s*,\\s*|\\s+");
        try {
            java.util.List<String> missing = TemplateUtils.validateFields(output, expectedFields);
            if (missing.isEmpty()) {
                validationResultLabel.setText("All expected fields are present");
                validationResultLabel.setForeground(new java.awt.Color(0, 128, 0));
            } else {
                validationResultLabel.setText("Missing fields: " + String.join(", ", missing));
                validationResultLabel.setForeground(java.awt.Color.RED);
            }
        } catch (Exception e) {
            validationResultLabel.setText("Invalid JSON output");
            validationResultLabel.setForeground(java.awt.Color.RED);
        }
    }
}