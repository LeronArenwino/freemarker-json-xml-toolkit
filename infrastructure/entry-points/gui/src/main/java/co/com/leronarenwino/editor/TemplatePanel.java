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

public class TemplatePanel extends EditorPanel {
    private static TemplatePanel instance;
    private JButton formatTemplateButton;
    private JButton singleLineButton;

    private TemplatePanel() {
        super(10, 40, "Template");
    }

    @Override
    protected void initComponents() {
        formatTemplateButton = new JButton("🔨");
        formatTemplateButton.setToolTipText("Format Template");
        singleLineButton = new JButton("↔");
        singleLineButton.setToolTipText("Convert to Single Line");
    }

    @Override
    protected void setComponents() {
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        textArea.setCodeFoldingEnabled(true);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setHighlightCurrentLine(false);
    }

    @Override
    protected void addComponents() {
        bottomPanel.add(formatTemplateButton);
        bottomPanel.add(singleLineButton);
        bottomPanel.add(Box.createHorizontalGlue());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static TemplatePanel getInstance() {
        if (instance == null) {
            instance = new TemplatePanel();
        }
        return instance;
    }

    public JButton getFormatTemplateButton() {
        return formatTemplateButton;
    }

    public JButton getSingleLineButton() {
        return singleLineButton;
    }
}