package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.ui.components.game.InventoryPane;
import net.ck.mtbg.ui.renderers.game.InventoryPaneListCellRenderer;
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
        super(owner, title, modal);
        setTitle(title);

        InventoryPane invP = new InventoryPane(owner, Game.getCurrent().getCurrentPlayer().getInventory(), this, action);
        invP.setBounds(0, 0, 300, 200);
        getContentPane().add(invP.initializeScrollPane());

        InventoryPaneListCellRenderer listCellRenderer = new InventoryPaneListCellRenderer();
        invP.setCellRenderer(listCellRenderer);


        addButtons();
        this.setVisible(true);
    }


    public void paintBorder(Graphics g)
    {

    }

}