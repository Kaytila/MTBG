package net.ck.game.ui.dnd;

import net.ck.game.items.AbstractItem;
import net.ck.game.ui.components.InventoryPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

public class InventoryPaneDragGestureHandler implements DragGestureListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private InventoryPane inventoryPane;

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
	public InventoryPaneDragGestureHandler(InventoryPane invPane)
	{
		setInventoryPane(invPane);
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		AbstractItem selectedValue = inventoryPane.getSelectedValue();
		Transferable t = new ItemTransferable(selectedValue);
		
		DragSource ds = dge.getDragSource();
		
		Dimension bestSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
		Cursor curs = Toolkit.getDefaultToolkit().createCustomCursor(selectedValue.getItemImage(), new Point(bestSize.width / 2, bestSize.height / 2), selectedValue.getName());
		ds.startDrag(dge, curs, t, new InventoryPaneDragSourceHandler());


	}

	public InventoryPane getInventoryPane()
	{
		return inventoryPane;
	}

	public void setInventoryPane(InventoryPane inventoryPane)
	{
		this.inventoryPane = inventoryPane;
	}
}
