package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.ui.components.game.InventoryPane;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

@Getter
@Setter
@Log4j2
@ToString
public class InventoryPaneDragGestureHandler implements DragGestureListener
{


    private InventoryPane inventoryPane;

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
}
