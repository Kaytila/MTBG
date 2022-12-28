package net.ck.game.ui.dnd;

import net.ck.game.ui.components.EQPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.util.Objects;

public class EQPanelDragGestureHandler implements DragGestureListener
{
    @Override
    public void dragGestureRecognized(DragGestureEvent dge)
    {

    }

    private final Logger logger = LogManager.getLogger(getRealClass());

    public EQPanelDragGestureHandler(EQPanel eqPanel)
    {

    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
