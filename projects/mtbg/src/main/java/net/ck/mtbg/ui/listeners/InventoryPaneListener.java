package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.items.Utility;
import net.ck.mtbg.ui.components.InventoryPane;
import net.ck.mtbg.ui.dialogs.ContainerDialog;
import net.ck.mtbg.util.communication.graphics.CursorChangeEvent;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.CursorUtils;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

@Log4j2
@Getter
@Setter
public class InventoryPaneListener implements ActionListener, ItemListener, ListDataListener, ListSelectionListener, MouseListener
{
    private InventoryPane inventoryPane;
    private ContainerDialog containerDialog;
    private AbstractKeyboardAction action;

    public InventoryPaneListener(InventoryPane invPane, AbstractKeyboardAction action)
    {
        setInventoryPane(invPane);
        setAction(action);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        //logger.info(e);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "Drop":
                break;
            case "Equip":
                logger.info("equip");
                Game.getCurrent().getCurrentPlayer().equipItem(getInventoryPane().getSelectionIndex());
                getInventoryPane().repaint();
                break;
        }

    }


    @Override
    public void intervalAdded(ListDataEvent e)
    {
        //logger.info("intervalAdded: {}", e);
    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {
        //logger.info("interval removed: {}", e);
    }

    @Override
    public void contentsChanged(ListDataEvent e)
    {
        //logger.info("contentsChanged: {}", e);
    }

    @Override
    /**
     * so the standard is that this is fired twice, once for mouse pressed and once for mouse released. as always, stackoverflow has a solution:
     * https://stackoverflow.com/questions/12461627/jlist-fires-valuechanged-twice-when-a-value-is-changed-via-mouse
     */
    public void valueChanged(ListSelectionEvent e)
    {
        if (!(e.getValueIsAdjusting()))
        {
            //logger.info("value changed");
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty())
            {
                //logger.info("none");
            }
            else
            {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++)
                {
                    if (lsm.isSelectedIndex(i))
                    {
                        // logger.info(" " + i);
                        //logger.info("selected item: {}", getInventoryPane().getModel().getElementAt(i));
                        getInventoryPane().setSelectionIndex(i);
                    }
                }
            }
        }
    }

    @Override
    /**
     * https://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
     */
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1)
        {
            if (getAction().getType().equals(KeyboardActionType.DROP))
            {
                e.consume();
                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("fire cursor event change");
                }
                EventBus.getDefault().post(new CursorChangeEvent(CursorUtils.createCustomCursorFromImage(getInventoryPane().getSelectedValue().getItemImage())));
                WindowBuilder.getController().setCurrentItemInHand(getInventoryPane().getSelectedValue());
                WindowClosingAction close = new WindowClosingAction(getInventoryPane().getParentDialog());
                close.actionPerformed(null);
				
				/*getInventoryPane().getParentDialog().dispatchEvent(new WindowEvent(getInventoryPane().getParentDialog(), WindowEvent.WINDOW_CLOSING));
				Game.getCurrent().getController().setDialogOpened(false);
				Game.getCurrent().getController().getFrame().requestFocus();
				Game.getCurrent().getController().getFrame().repaint();
				*/
            }
            else
            {
                e.consume();
                // logger.info("double click");
                if (getInventoryPane().getSelectedValue().isContainer())
                {
                    containerDialog = new ContainerDialog((JFrame) getInventoryPane().getOwner(), getInventoryPane().getSelectedValue().getName() + " " + "contents", false, (((Utility) getInventoryPane().getSelectedValue()).getInventory()));
                    //logger.info("inventory: {}", ((Utility) getInventoryPane().getSelectedValue()).getInventory());
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

}
