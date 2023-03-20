package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.ui.components.InventoryPane;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class DialogPopupListener extends MouseAdapter
{
	private final Logger        logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private       InventoryPane component;

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
			logger.info(getComponent().getParent().toString());
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
