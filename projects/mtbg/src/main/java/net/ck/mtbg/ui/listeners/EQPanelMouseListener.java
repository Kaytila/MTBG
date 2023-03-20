package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.ui.components.EQPanel;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.NPCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class EQPanelMouseListener implements MouseListener, MouseMotionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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

    public EQPanel getEqPanel()
    {
        return eqPanel;
    }

    public void setEqPanel(EQPanel eqPanel)
    {
        this.eqPanel = eqPanel;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        logger.info ("dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
