package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.ui.components.SpellbookPane;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.CursorUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import net.ck.mtbg.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * spellbooklistener is the general listener class for SpellBook Table Component.
 * Currently, only mouse listener, but will definitely include more in the future.
 *
 * <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple">https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple</a>
 */
public class SpellBookListener implements MouseListener, MouseMotionListener, ListDataListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public SpellBookListener(SpellbookPane pane, AbstractKeyboardAction action)
    {
        this.spellbookPane = pane;
        this.action = action;
    }

    public SpellbookPane getSpellbookPane()
    {
        return spellbookPane;
    }

    private SpellbookPane spellbookPane;
    private AbstractKeyboardAction action;

    public void setSpellbookPane(SpellbookPane spellbookPane)
    {
        this.spellbookPane = spellbookPane;
    }


    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1)
        {
            //TODO how to get from select a spell to actually cast it
            if (getAction().getType().equals(KeyboardActionType.SPELLBOOK))
            {
                WindowBuilder.getController().setCurrentSpellInHand(((SpellbookPane) e.getSource()).getSelectedValue());
                logger.info("double click");
                e.consume();
                WindowClosingAction close = new WindowClosingAction(getSpellbookPane().getParentDialog());


                close.actionPerformed(null);

            }
            else
            {
                logger.debug("action type is not spellbook?");
            }
        }
        else
        {
            logger.debug("simple mouse click");
            logger.info("selected spell: {}", ((SpellbookPane) e.getSource()).getSelectedValue().getName());
        }
    }

    /**
     * <a href="https://stackoverflow.com/questions/14852719/double-click-listener-on-jtable-in-java">https://stackoverflow.com/questions/14852719/double-click-listener-on-jtable-in-java</a>
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        SpellbookPane spellbookPane = (SpellbookPane) e.getSource();
        Point point = e.getPoint();

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        logger.debug("simple mouse released");
        SpellbookPane table = (SpellbookPane) e.getSource();

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        logger.debug("simple mouse entered");
        SpellbookPane table = (SpellbookPane) e.getSource();
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CursorUtils.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        logger.debug("simple mouse exit");
        SpellbookPane table = (SpellbookPane) e.getSource();
        table.setCursor(Cursor.getDefaultCursor());
        CursorUtils.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public AbstractKeyboardAction getAction()
    {
        return action;
    }

    public void setAction(AbstractKeyboardAction action)
    {
        this.action = action;
    }

    @Override
    public void intervalAdded(ListDataEvent e)
    {

    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {

    }

    @Override
    public void contentsChanged(ListDataEvent e)
    {

    }
}
