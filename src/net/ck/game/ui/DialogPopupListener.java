package net.ck.game.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DialogPopupListener extends MouseAdapter
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private InventoryPane component;

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
	public DialogPopupListener(InventoryPane inventoryPane)
	{
		setComponent(inventoryPane);
	}

	public void mousePressed(MouseEvent e)
	{
		logger.info("pressed");
		maybeShowPopup(e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		logger.info("clicked");
	}

	public void mouseReleased(MouseEvent e)
	{
		logger.info("released");
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e)
	{		
		if (e.isPopupTrigger())
		{					
			getComponent().getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}
		else
		{
			logger.info(component.getParent().toString());
		}
	}

	public InventoryPane getComponent()
	{
		return component;
	}

	public void setComponent(InventoryPane component)
	{
		this.component = component;
	}

}
