package net.ck.mtbg.util.communication.keyboard.gameactions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.communication.keyboard.framework.KeyboardActionType;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Getter
@Setter
@Log4j2
public class AbstractKeyboardAction extends AbstractAction
{

    /**
     * can the action be run immediately (NORTH, WEST ....) ,
     * or is this a two step action (get -> where)
     */
    private boolean actionimmediately = false;

    /**
     * has the action finished
     */
    private boolean done = false;


    /**
     * this is the tile where the crosshair clicked on
     * getWhere is the tile where something happens
     */
    private Point getWhere;

    /**
     * the item affected by the action, can be get, can be drop, can be equip
     */
    private AbstractItem affectedItem;

    /**
     * sourceCoordinates refers to a UI position (in relative pixels inside the UI)
     */
    private Point sourceCoordinates;

    /**
     * targetCoordinates refers to a UI position (in relative pixels inside the UI)
     */
    private Point targetCoordinates;

    private boolean haveNPCAction;


    private AbstractSpell currentSpell;

    private Point oldMousePosition;

    private MapTile mapTile;

    /**
     * how many fields can you hear this action?
     */
    private int soundReach = 0;

    public AbstractKeyboardAction()
    {
        setActionimmediately(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (TimerManager.getMissileTimer() != null)
        {
            if (TimerManager.getMissileTimer().isRunning())
            {
                logger.info("missile timer dont run, ignore command");
            }
            else
            {
                //logger.info(getType() + " pressed");
                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("fire new event?");
                }
                EventBus.getDefault().post(this);
            }
        }
        else
        {
            //logger.info(getType() + " pressed");
            if (GameConfiguration.debugEvents == true)
            {
                logger.debug("fire new event?");
            }
            EventBus.getDefault().post(this);
        }
    }


    public KeyboardActionType getType()
    {
        return KeyboardActionType.NULL;
    }

    public int getSoundReach()
    {
        return 0;
    }


}