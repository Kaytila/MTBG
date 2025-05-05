package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.ui.components.game.EQPanel;
import net.ck.mtbg.util.utils.NPCUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@Log4j2
@Getter
@Setter
public class EQPanelMouseListener implements MouseListener, MouseMotionListener
{

    private EQPanel eqPanel;

    public EQPanelMouseListener(EQPanel eqPanel)
    {
        this.eqPanel = eqPanel;
        eqPanel.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            logger.info("double click in {}", eqPanel.getArmorPosition().toString());
            if (Game.getCurrent().getCurrentPlayer().getWearEquipment().get(eqPanel.getArmorPosition()) != null)
            {
                logger.info("removing eq: {} ", eqPanel.getArmorPosition().toString());
                Game.getCurrent().getCurrentPlayer().removeItem(eqPanel.getArmorPosition());
                getEqPanel().repaint();
            }
            else
            {
                //logger.info("nothing to remove: {} ", eqPanel.getArmorPosition().toString());
                //logger.info("perhaps something to add?");
                Armor armor = NPCUtils.calculateArmorToWear(eqPanel.getArmorPosition());

                if (armor != null)
                {
                    Game.getCurrent().getCurrentPlayer().wearItemAtPosition(armor, armor.getPosition());
                    getEqPanel().repaint();
                }
            }
        }
    }

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

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        logger.info("dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
