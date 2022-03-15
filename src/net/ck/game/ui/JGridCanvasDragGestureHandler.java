package net.ck.game.ui;

import java.awt.*;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

import net.ck.game.backend.Game;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JGridCanvasDragGestureHandler implements DragGestureListener
{
	private final Logger logger = LogManager.getLogger(getRealClass());
	private JGridCanvas grid;
	public JGridCanvasDragGestureHandler(JGridCanvas gridCanvas)
	{
		setGrid(gridCanvas);
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
	/**
	 * so this is also called when the right mouse is dragged.
	 * and it appears the mouse action listener calls an action
	 * pressed and a mouse released action each time.
	 * which actually would make sense as it
	 * the problem is that right mouse drag is also a drag gesture.
	 * not sure i can turn this off, this would make things way easier.
	 * but for the time being, checking if the tile is null appears to fix the issue
	 * i do not trust this fix, not at all.
	 */
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		if (Game.getCurrent().getController().isMousePressed())
		{
			return;
		}
		else
		{
			logger.info("dragGestureRecognized");
			MapTile tile = MapUtils.getTileByCoordinates(dge.getDragOrigin());
			//how the flying fuck this can be null i do not comprehend but it appears to fix it.
			if (tile == null)
			{

			}
			else
			{
				if (tile.isBlocked())
				{
					getGrid().getDropTarget().setActive(false);
				}
			}
		}
	}

	public JGridCanvas getGrid()
	{
		return grid;
	}

	public void setGrid(JGridCanvas grid)
	{
		this.grid = grid;
	}
}
