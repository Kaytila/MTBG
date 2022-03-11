package net.ck.game.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.game.backend.Game;
import net.ck.game.items.Utility;
import net.ck.util.CursorUtils;
import net.ck.util.communication.graphics.CursorChangeEvent;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.KeyboardActionType;
import net.ck.util.communication.keyboard.WindowClosingAction;

public class InventoryPaneListener implements ActionListener, ItemListener, ListDataListener, ListSelectionListener, MouseListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private InventoryPane inventoryPane;
	private ContainerDialog containerDialog;
	private AbstractKeyboardAction action;
	public AbstractKeyboardAction getAction()
	{
		return action;
	}

	public void setAction(AbstractKeyboardAction action)
	{
		this.action = action;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Logger getLogger()
	{
		return logger;
	}
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
			case "Drop" :
				break;
			case "Equip" :
				logger.info("equip");
				Game.getCurrent().getCurrentPlayer().equipItem(getInventoryPane().getSelectionIndex());
				getInventoryPane().repaint();
				break;
		}

	}

	public InventoryPane getInventoryPane()
	{
		return inventoryPane;
	}

	public void setInventoryPane(InventoryPane inventoryPane)
	{
		this.inventoryPane = inventoryPane;
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
				EventBus.getDefault().post(new CursorChangeEvent(CursorUtils.createCustomCursorFromImage(getInventoryPane().getSelectedValue().getItemImage())));
				Game.getCurrent().getController().setCurrentItemInHand(getInventoryPane().getSelectedValue());
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
				containerDialog = new ContainerDialog(getInventoryPane().getOwner(), getInventoryPane().getSelectedValue().getName() + " " + "contents", false, (((Utility) getInventoryPane().getSelectedValue()).getInventory()));
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

	public ContainerDialog getContainerDialog()
	{
		return containerDialog;
	}

	public void setContainerDialog(ContainerDialog containerDialog)
	{
		this.containerDialog = containerDialog;
	}
}
