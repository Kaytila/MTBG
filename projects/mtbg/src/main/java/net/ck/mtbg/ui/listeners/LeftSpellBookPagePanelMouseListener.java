package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.ui.components.SpellbookPane;
import net.ck.mtbg.ui.models.SpellBookListDataModel;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@Log4j2
@Getter
@Setter
public class LeftSpellBookPagePanelMouseListener implements MouseListener, MouseMotionListener, MouseInputListener
{
    private SpellbookPane spellbookPane;

    public LeftSpellBookPagePanelMouseListener(SpellbookPane spellbookPane)
    {
        this.spellbookPane = spellbookPane;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        //logger.debug("mouse clicked on left page");
        if (Game.getCurrent().getCurrentPlayer().decreaseLevel())
        {
            ((SpellBookListDataModel) spellbookPane.getModel()).filterSpellsByLevel();

        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
