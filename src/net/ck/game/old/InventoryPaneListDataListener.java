package net.ck.game.old;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InventoryPaneListDataListener implements ListDataListener
{
	
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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
	public InventoryPaneListDataListener()
	{
		
	}

	@Override
	public void intervalAdded(ListDataEvent e)
	{
		logger.info("intervalAdded: {}", e);
	}

	@Override
	public void intervalRemoved(ListDataEvent e)
	{
		logger.info("interval removed: {}", e);
	}

	@Override
	public void contentsChanged(ListDataEvent e)
	{
		logger.info("contentsChanged: {}", e);
	}
}
