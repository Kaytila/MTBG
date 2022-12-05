package net.ck.game.ui;

import net.ck.game.backend.Game;
import net.ck.game.items.AbstractItem;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;
import net.ck.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.Objects;

public class JGridCanvasDropTargetHandler implements DropTargetListener
{

	private final Logger logger = LogManager.getLogger(getRealClass());
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
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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
		//logger.info("dragOver: {}", dtde.getLocation());
		MapTile tile = MapUtils.calculateMapTileUnderCursor(dtde.getLocation());
		if (Game.getCurrent().getCurrentPlayer().getMapPosition().equals(tile.getMapPosition()))
		{
			dtde.acceptDrag(DnDConstants.ACTION_MOVE);
		}
		else
		{
			if (tile.isBlocked())
			{
				dtde.rejectDrag();
			}
			else
			{
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);
			}
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

	/**
	 * https://stackoverflow.com/questions/29187546/java-get-mouse-coordinates-within-window helped with the mouse locations as I am forgetful and dont actually want to remember things like that.
	 */
	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		logger.info("drop");
		if (!(Game.getCurrent().getController().getCurrentAction().getType().equals(KeyboardActionType.DROP)))
		{
			logger.info("current action: {}", Game.getCurrent().getController().getCurrentAction());
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
							if (tile.isHidden())
							{
								logger.info("tile {} is not visible right now", tile);
								return;
							}
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
