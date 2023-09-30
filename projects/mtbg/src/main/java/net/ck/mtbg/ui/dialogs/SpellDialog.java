package net.ck.mtbg.ui.dialogs;

import net.ck.mtbg.ui.buttons.CancelButton;
import net.ck.mtbg.ui.buttons.OKButton;
import net.ck.mtbg.ui.components.Spellbook;
import net.ck.mtbg.ui.listeners.WindowClosingListener;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class SpellDialog extends AbstractDialog
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public SpellDialog(Frame owner, String title, boolean modal)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(new GridLayout(1, 0));
        this.setLocationRelativeTo(owner);
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



        /**/
        String[] columnNames = {"First Name", "Last Name"};
        Object[][] data = {{"Kathy", "Smith"}, {"John", "Doe"}, {"Sue", "Black"}, {"Jane", "White"}, {"Joe", "Brown"}};
        final Spellbook table = new Spellbook(data, columnNames);
        panel.add(table);

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
