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

public class DataPanel extends EditorPanel {
    private static DataPanel instance;
    private JButton validateDataModelButton;

    private DataPanel() {
        super(12, 40, "Data Model");
    }

    @Override
    protected void initComponents() {
        validateDataModelButton = new JButton("▶️");
        validateDataModelButton.setToolTipText("Format Data Model JSON");
    }

    @Override
    protected void setComponents() {
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setCodeFoldingEnabled(true);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setHighlightCurrentLine(false);
    }

    @Override
    protected void addComponents() {
        bottomPanel.add(validateDataModelButton);
        bottomPanel.add(Box.createHorizontalGlue());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static DataPanel getInstance() {
        if (instance == null) {
            instance = new DataPanel();
        }
        return instance;
    }

    public JButton getValidateDataModelButton() {
        return validateDataModelButton;
    }
}