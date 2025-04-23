package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.MapCanvas;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.MapUtils;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

@Getter
@Setter
@Log4j2
@ToString
public class JGridCanvasDragGestureHandler implements DragGestureListener
{

    private MapCanvas grid;

    public JGridCanvasDragGestureHandler(MapCanvas gridCanvas)
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
        if (WindowBuilder.getGameController().isMousePressed())
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
}
