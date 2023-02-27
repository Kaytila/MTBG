package net.ck.game.ui.dnd;

import net.ck.game.map.MapTile;
import net.ck.game.ui.components.JGridCanvas;
import net.ck.util.CodeUtils;
import net.ck.util.MapUtils;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

public class JGridCanvasDragGestureHandler implements DragGestureListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private JGridCanvas grid;
	public JGridCanvasDragGestureHandler(JGridCanvas gridCanvas)
	{
		setGrid(gridCanvas);
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
        if (WindowBuilder.getController().isMousePressed())
        {
            return;
        }
        else
        {
            logger.info("dragGestureRecognized");
            MapTile tile = MapUtils.getMapTileByCoordinatesAsPoint(dge.getDragOrigin());
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
