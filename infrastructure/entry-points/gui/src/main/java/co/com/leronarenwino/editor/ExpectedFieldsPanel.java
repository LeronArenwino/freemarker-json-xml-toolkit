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
        super(1, 40, "Expected fields");
    }

    @Override
    protected void initComponents() {
        validateFieldsButton = new JButton("Validate Output Fields");
        validationResultLabel = new JLabel("Validation result will appear here.");
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
    }

    @Override
    protected void addComponents() {
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(validationResultLabel);
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(validateFieldsButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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

    public JLabel getValidationResultLabel() {
        return validationResultLabel;
    }
}