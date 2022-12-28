package net.ck.game.ui.dnd;

import net.ck.game.ui.components.EQPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Objects;

public class EQPanelDropTargetHandler implements DropTargetListener
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public EQPanelDropTargetHandler(EQPanel eqPanel)
    {

    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {

    }

    @Override
    public void dragExit(DropTargetEvent dte)
    {

    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {

    }
}
