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

import co.com.leronarenwino.utils.ButtonStyleUtil;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;

public class OutputPanel extends EditorPanel {
    private static OutputPanel instance;
    private JButton processTemplateButton;
    private JButton formatJsonButton;
    private JButton clearOutputButton;

    private OutputPanel() {
        super("Rendered Result");
    }

    @Override
    protected void initComponents() {
        processTemplateButton = createStyledButton("▶", "Evaluate the template with data", ButtonStyleUtil.ButtonStyle.PRIMARY);
        formatJsonButton = createStyledButton("🔨", "Format output as JSON", ButtonStyleUtil.ButtonStyle.SUCCESS);
        clearOutputButton = createStyledButton("×", "Clear output area", ButtonStyleUtil.ButtonStyle.DANGER);
        processTemplateButton.setToolTipText("Evaluate the template with data");
        formatJsonButton.setToolTipText("Format output as JSON");
        clearOutputButton.setToolTipText("Clear output area");
    }

    @Override
    protected void setComponents() {
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        textArea.setEditable(false);
        textArea.setLineWrap(isWrapEnabled);
        textArea.setWrapStyleWord(isWrapEnabled);
        textArea.setHighlightCurrentLine(false);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void addComponents() {
        bottomPanel.add(processTemplateButton);
        bottomPanel.add(formatJsonButton);
        bottomPanel.add(clearOutputButton);
        bottomPanel.add(toggleWrapButton);
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

    @Override
    protected void addReplaceKeyBinding() {
        // No agregar binding para Ctrl+R - deshabilitar reemplazo en OutputPanel
    }
}