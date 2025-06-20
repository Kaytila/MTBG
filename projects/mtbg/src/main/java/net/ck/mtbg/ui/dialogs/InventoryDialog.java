package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.ui.components.game.InventoryPane;
import net.ck.mtbg.ui.listeners.game.WindowClosingListener;
import net.ck.mtbg.ui.renderers.game.InventoryPaneListCellRenderer;
import net.ck.mtbg.util.communication.keyboard.framework.WindowClosingAction;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class InventoryDialog extends AbstractDialog
{


    public InventoryDialog(JFrame owner, String title, boolean modal, AbstractKeyboardAction action)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(null);
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
        this.setUndecorated(true);
        InventoryPane invP = new InventoryPane(owner, Game.getCurrent().getCurrentPlayer().getInventory(), this, action);
        invP.setBounds(0, 0, 300, 200);
        this.add(invP.initializeScrollPane());

        InventoryPaneListCellRenderer listCellRenderer = new InventoryPaneListCellRenderer();
        invP.setCellRenderer(listCellRenderer);


        addButtons();
        this.setVisible(true);
    }

    public InventoryDialog()
    {
    }


    public void paintBorder(Graphics g)
    {

    }

}