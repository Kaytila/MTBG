package net.ck.game.ui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.items.AbstractItem;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;

public class JGridCanvasDropTargetHandler implements DropTargetListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private JGridCanvas gridcanvas;

	public JGridCanvas getGridcanvas()
	{
		return gridcanvas;
	}

	public void setGridcanvas(JGridCanvas gridcanvas)
	{
		this.gridcanvas = gridcanvas;
	}

	public JGridCanvasDropTargetHandler(JGridCanvas gridCanvas)
	{
		gridcanvas = gridCanvas;
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
	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		// logger.info("dragEnter");

	}
	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		logger.info("dragOver: {}", dtde.getLocation());
		MapTile tile = MapUtils.calculateMapTileUnderCursor(dtde.getLocation());
		if (tile.isBlocked())
		{
			dtde.rejectDrag();
		}
	}
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
		// logger.info("dropActionChanged");

	}
	@Override
	public void dragExit(DropTargetEvent dte)
	{
		// logger.info("dragExit");

	}
	@Override
	/**
	 * https://stackoverflow.com/questions/29187546/java-get-mouse-coordinates-within-window helped with the mouse locations as I am forgetful and dont actually want to remember things like that.
	 */
	public void drop(DropTargetDropEvent dtde)
	{
		logger.info("drop");
		if (Game.getCurrent().getController().getCurrentAction() != null)
		{
			return;
		}
		else
		{
			if (dtde.getTransferable().isDataFlavorSupported(ItemTransferable.ITEM_DATA_FLAVOR))
			{
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(ItemTransferable.ITEM_DATA_FLAVOR))
				{
					try
					{
						Object transferData = t.getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
						if (transferData instanceof AbstractItem)
						{
							AbstractItem item = (AbstractItem) transferData;
							logger.info("item: {}", item);
							int x = MouseInfo.getPointerInfo().getLocation().x - gridcanvas.getLocationOnScreen().x;
							int y = MouseInfo.getPointerInfo().getLocation().y - gridcanvas.getLocationOnScreen().y;
							logger.info("mouse position: {}", new Point(x, y));
							MapTile tile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
							logger.info("map tile: {}", tile);
							if (Game.getCurrent().getCurrentPlayer().getMapPosition().equals(tile.getMapPosition()))
							{
								logger.info("dragging on player");
								Game.getCurrent().getCurrentPlayer().getInventory().add(item);
							}
							else
							{
								if (tile.isBlocked())
								{
									logger.info("tile is blocked, no drop");
									dtde.rejectDrop();
								}
								else
								{
									logger.info("tile is available, drop");
									tile.getInventory().add(item);
								}
							}
							dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						}
						else
						{
							dtde.rejectDrop();
						}
					}
					catch (UnsupportedFlavorException ex)
					{
						ex.printStackTrace();
						dtde.rejectDrop();
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
						dtde.rejectDrop();
					}
				}
				else
				{
					dtde.rejectDrop();
				}
			}
		}
	}
}
