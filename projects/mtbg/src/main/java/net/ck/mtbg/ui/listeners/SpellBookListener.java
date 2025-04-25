package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.state.NoiseManager;
import net.ck.mtbg.soundeffects.SoundEffects;
import net.ck.mtbg.ui.components.SpellbookPane;
import net.ck.mtbg.ui.controllers.GameController;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import net.ck.mtbg.util.utils.CursorUtils;

import javax.swing.*;
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

@Log4j2
@Getter
public class SpellBookListener implements MouseListener, MouseMotionListener, ListDataListener
{
    private SpellbookPane spellbookPane;
    private AbstractKeyboardAction action;

    public SpellBookListener(SpellbookPane pane, AbstractKeyboardAction action)
    {
        this.spellbookPane = pane;
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (e.getClickCount() == 2)
            {
                logger.info(() -> "double click - close dialog and go into crosshairs");
                if (getAction().getType().equals(KeyboardActionType.SPELLBOOK))
                {
                    GameController.getCurrent().setCurrentSpellInHand(((SpellbookPane) e.getSource()).getSelectedValue());
                    e.consume();
                    WindowClosingAction close = new WindowClosingAction(getSpellbookPane().getParentDialog());
                    close.actionPerformed(null);
                }
            }
            else if (e.getClickCount() == 1)
            {
                logger.info(() -> "single click - dont close dialog, closing with ESC should start crosshairs");
                GameController.getCurrent().setCurrentSpellInHand(((SpellbookPane) e.getSource()).getSelectedValue());
            }
            else
            {
                return;
            }
        }
        else
        {
            return;
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
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        getSpellbookPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        CursorUtils.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        getSpellbookPane().setCursor(Cursor.getDefaultCursor());
        CursorUtils.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

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
        if (GameConfiguration.playSound == true)
        {
            NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.PAGETURN);
        }
    }
}
