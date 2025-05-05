package net.ck.mtbg.ui.dnd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.game.EQPanel;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

@Getter
@Setter
@Log4j2
@ToString
public class EQPanelDragGestureHandler implements DragGestureListener
{
    public EQPanelDragGestureHandler(EQPanel eqPanel)
    {

    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge)
    {

    }


}
