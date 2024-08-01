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
public class JGridCanvasDragSourceHandler implements DragSourceListener
{

    @Override
    public void dragEnter(DragSourceDragEvent dsde)
    {
        logger.info("drag Enter");
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde)
    {
        logger.info("drag over");
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde)
    {
        logger.info("dropActionChanged");
    }

    @Override
    public void dragExit(DragSourceEvent dse)
    {
        logger.info("dragExit");
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde)
    {
        logger.info("dragDropEnd");
    }
}
