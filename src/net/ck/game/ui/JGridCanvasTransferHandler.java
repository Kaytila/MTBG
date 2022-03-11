package net.ck.game.ui;

import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.items.AbstractItem;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;

public class JGridCanvasTransferHandler extends TransferHandler
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private MapTile sourceMapTile = null;
	private MapTile targetMapTile = null;

	public MapTile getSourceMapTile()
	{
		return sourceMapTile;
	}

	public void setSourceMapTile(MapTile sourceMapTile)
	{
		this.sourceMapTile = sourceMapTile;
	}

	public MapTile getTargetMapTile()
	{
		return targetMapTile;
	}

	public void setTargetMapTile(MapTile targetMapTile)
	{
		this.targetMapTile = targetMapTile;
	}

	private JGridCanvas gridCanvas;

	public JGridCanvasTransferHandler(JGridCanvas gridCanvas)
	{
		setGridCanvas(gridCanvas);
	}

	public JGridCanvas getGridCanvas()
	{
		return gridCanvas;
	}

	public void setGridCanvas(JGridCanvas gridCanvas)
	{
		this.gridCanvas = gridCanvas;
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
	public void setDragImage(Image img)
	{
		// TODO Auto-generated method stub
		super.setDragImage(img);
	}

	@Override
	public Image getDragImage()
	{
		// TODO Auto-generated method stub
		return super.getDragImage();
	}

	@Override
	public Point getDragImageOffset()
	{
		// TODO Auto-generated method stub
		return super.getDragImageOffset();
	}

	@Override
	public void exportAsDrag(JComponent comp, InputEvent e, int action)
	{
		// TODO Auto-generated method stub
		super.exportAsDrag(comp, e, action);
	}

	@Override
	public boolean importData(TransferSupport support)
	{
		int x = MouseInfo.getPointerInfo().getLocation().x - getGridCanvas().getLocationOnScreen().x;
		int y = MouseInfo.getPointerInfo().getLocation().y - getGridCanvas().getLocationOnScreen().y;
		logger.info("mouse position: {}", new Point(x, y));
		targetMapTile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
		AbstractItem item =	null;
		try
		{
			item = (AbstractItem) support.getTransferable().getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		targetMapTile.getInventory().add(item);
		return true;
	}

	@Override
	public boolean canImport(TransferSupport support)
	{
		if (Game.getCurrent().getController().getCurrentAction() != null)
		{
			return false;
		}
		return super.canImport(support);
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		return super.canImport(comp, transferFlavors);
	}

	@Override
	public int getSourceActions(JComponent c)
	{
		logger.info("get source actions");
		return TransferHandler.MOVE;
	}

	@Override
	protected Transferable createTransferable(JComponent c)
	{
		int x = MouseInfo.getPointerInfo().getLocation().x - getGridCanvas().getLocationOnScreen().x;
		int y = MouseInfo.getPointerInfo().getLocation().y - getGridCanvas().getLocationOnScreen().y;
		logger.info("mouse position: {}", new Point(x, y));
		sourceMapTile = MapUtils.calculateMapTileUnderCursor(new Point(x, y));
		if (sourceMapTile.getInventory().getSize() > 0)
		{
			setDragImage(sourceMapTile.getInventory().get(0).getItemImage());
			return new ItemTransferable(sourceMapTile.getInventory().get(0));
		}
		return null;
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		AbstractItem item = null;
		try
		{
			item = (AbstractItem) data.getTransferData(ItemTransferable.ITEM_DATA_FLAVOR);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		sourceMapTile.getInventory().remove(item);
	}
}
