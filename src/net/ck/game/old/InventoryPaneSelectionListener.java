package net.ck.game.old;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.ui.InventoryPane;

public class InventoryPaneSelectionListener implements ListSelectionListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private InventoryPane inventoryPane;
	
	public InventoryPane getInventoryPane()
	{
		return inventoryPane;
	}

	public void setInventoryPane(InventoryPane inventoryPane)
	{
		this.inventoryPane = inventoryPane;
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

	public InventoryPaneSelectionListener(InventoryPane invPane)
	{
		setInventoryPane(invPane);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty())
		{
			logger.info("none");
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
					//logger.info(" " + i);
					logger.info("selected item: {}", getInventoryPane().getModel().getElementAt(i));
					getInventoryPane().setSelectionIndex(i);
				}
			}
		}

	}
}
