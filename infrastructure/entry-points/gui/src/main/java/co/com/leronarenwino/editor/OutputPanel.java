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

public class OutputPanel extends EditorPanel {
    private static OutputPanel instance;
    private JButton processTemplateButton;
    private JButton formatJsonButton;
    private JButton clearOutputButton;
    private JButton toggleWrapButton;
    private boolean isWrapEnabled = false;

    private OutputPanel() {
        super(12, 80, "Rendered Result");
    }

    @Override
    protected void initComponents() {
        toggleWrapButton = new JButton("→");
        toggleWrapButton.setToolTipText("Toggle line wrap");
        toggleWrapButton.addActionListener(e -> toggleWrap());

        processTemplateButton = new JButton("Evaluate Template");
        processTemplateButton.setToolTipText("Evaluate the template with data");

        formatJsonButton = new JButton("Format to JSON");
        formatJsonButton.setToolTipText("Format output as JSON");

        clearOutputButton = new JButton("Clear Output");
        clearOutputButton.setToolTipText("Clear output area");
    }

    @Override
    protected void setComponents() {
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setEditable(false);
        textArea.setLineWrap(isWrapEnabled);
        textArea.setWrapStyleWord(isWrapEnabled);
        textArea.setHighlightCurrentLine(false);
    }

    @Override
    protected void addComponents(String labelText) {
        bottomPanel.add(processTemplateButton);
        bottomPanel.add(formatJsonButton);
        bottomPanel.add(clearOutputButton);
        bottomPanel.add(toggleWrapButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(positionLabel);

        add(new JLabel(labelText), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void toggleWrap() {
        isWrapEnabled = !isWrapEnabled;
        textArea.setLineWrap(isWrapEnabled);
        textArea.setWrapStyleWord(isWrapEnabled);
        toggleWrapButton.setText(isWrapEnabled ? "↵" : "→");
    }

    public static OutputPanel getInstance() {
        if (instance == null) {
            instance = new OutputPanel();
        }
        return instance;
    }

    public JButton getFormatJsonButton() {
        return formatJsonButton;
    }

    public JButton getClearOutputButton() {
        return clearOutputButton;
    }

    public JButton getProcessTemplateButton() {
        return processTemplateButton;
    }
}