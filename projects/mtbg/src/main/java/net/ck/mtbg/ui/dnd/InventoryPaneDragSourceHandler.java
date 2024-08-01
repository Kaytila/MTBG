package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

@Getter
@Setter
@Log4j2
@ToString
public class InventoryPaneDragSourceHandler implements DragSourceListener
{


    @Override
    public void dragEnter(DragSourceDragEvent dsde)
    {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde)
    {

    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde)
    {

    }

    @Override
    public void dragExit(DragSourceEvent dse)
    {
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde)
    {
    }
}
