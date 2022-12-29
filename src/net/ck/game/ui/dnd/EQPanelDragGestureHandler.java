package net.ck.game.ui.dnd;

import net.ck.game.ui.components.EQPanel;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

public class EQPanelDragGestureHandler implements DragGestureListener
{
    @Override
    public void dragGestureRecognized(DragGestureEvent dge)
    {

    }

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public EQPanelDragGestureHandler(EQPanel eqPanel)
    {

    }



}
