package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.SpellbookPane;
import net.ck.mtbg.ui.listeners.LeftSpellBookPagePanelMouseListener;
import net.ck.mtbg.ui.listeners.RightSpellBookPageMouseListener;
import net.ck.mtbg.ui.listeners.WindowClosingListener;
import net.ck.mtbg.ui.renderers.SpellbookListCellRenderer;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class SpellDialog extends AbstractDialog
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public SpellDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action) {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(new GridLayout(1, 0));
        this.setLocationRelativeTo(owner);
        this.setModal(modal);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        final WindowClosingListener windowClosingListener = new WindowClosingListener();
        this.addWindowListener(windowClosingListener);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 300);
        panel.setLayout(null);
        this.setContentPane(panel);
        this.setUndecorated(true);
        final SpellbookPane spellbookPane = new SpellbookPane(owner, this, action);

        JPanel leftPagePanel = new JPanel();
        leftPagePanel.setBounds(0, 0, 20, 300);
        leftPagePanel.setBackground(Color.BLUE);
        leftPagePanel.addMouseListener(new LeftSpellBookPagePanelMouseListener(spellbookPane));
        panel.add(leftPagePanel);

        JPanel rightPagePanel = new JPanel();
        rightPagePanel.setBounds(280, 0, 20, 300);
        rightPagePanel.setBackground(Color.YELLOW);
        rightPagePanel.addMouseListener(new RightSpellBookPageMouseListener(spellbookPane));
        panel.add(rightPagePanel);

        spellbookPane.setBorder(BorderFactory.createLineBorder(Color.PINK));
        SpellbookListCellRenderer listCellRenderer = new SpellbookListCellRenderer();
        spellbookPane.setCellRenderer(listCellRenderer);
        panel.add(spellbookPane);

        cancelButton = new CancelButton();
        okButton = new OKButton();
        okButton.setBounds(300 - 160, 300 - 70, 70, 30);
        cancelButton.setBounds(300 - 90, 300 - 70, 70, 30);
        this.add(cancelButton);
        this.add(okButton);
        this.setVisible(true);
    }

    private void printDebugData(JTable table)
    {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i = 0; i < numRows; i++)
        {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++)
            {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

}
