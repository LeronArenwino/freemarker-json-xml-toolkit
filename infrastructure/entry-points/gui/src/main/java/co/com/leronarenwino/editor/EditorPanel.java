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

import co.com.leronarenwino.utils.CaretUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public abstract class EditorPanel extends JPanel {
    protected RSyntaxTextArea textArea;
    protected RTextScrollPane scrollPane;
    protected JLabel positionLabel;
    protected JPanel bottomPanel;

    public EditorPanel(int rows, int cols, String labelText) {
        setLayout(new BorderLayout());
        textArea = new RSyntaxTextArea(rows, cols);
        scrollPane = new RTextScrollPane(textArea, true);
        positionLabel = new JLabel("1:1");
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        initComponents();
        setComponents();
        addComponents(labelText);

        textArea.addCaretListener(e -> updateCaretPosition());
    }

    protected abstract void initComponents();
    protected abstract void setComponents();
    protected abstract void addComponents(String labelText);

    private void updateCaretPosition() {
        CaretUtil.updateCaretPosition(textArea, positionLabel);
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }
}