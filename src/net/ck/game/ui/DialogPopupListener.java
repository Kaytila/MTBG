package net.ck.game.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class DialogPopupListener extends MouseAdapter
{

	private final Logger logger = LogManager.getLogger(getRealClass());
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
		getLogger().info("pressed");
		maybeShowPopup(e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		getLogger().info("clicked");
	}

	public void mouseReleased(MouseEvent e)
	{
		getLogger().info("released");
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
			getLogger().info(getComponent().getParent().toString());
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
