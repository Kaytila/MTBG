package net.ck.mtbg.backend.entities.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.actions.RandomAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.communication.keyboard.gameactions.AbstractKeyboardAction;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.UILense;

import java.awt.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * environment is the counterpart to player, the AI so to speak
 *
 * @author Claus
 */
@Log4j2
@Getter
@Setter
public class World extends AbstractEntity implements LifeForm
{
    private boolean spawned = false;

    public World()
    {
        super();
    }

    public RandomAction createRandomEvent(PlayerAction action)
    {
        RandomAction event = new RandomAction();
        Random rand = new Random();
        int result = 1;//rand.nextInt(1);

        switch (result)
        {
            case 0:
                event.setTitle("BINGO test" + result);
                break;
            case 1:
                event.setTitle("Bla " + result);
                break;
            default:
                event.setTitle("test " + result);
                break;
        }
        //getCurrentTurn().getEvents().add(event);
        //logger.info("random event: " + event.getTitle());
        return event;
    }

    @Override
    public Point getMapPosition()
    {
        // logger.error("world does not have a position, return empty point");
        return new Point(-1, -1);
    }

    @Override
    public void setMapPosition(Point p)
    {
        logger.error("world does not have a position, do not set");
    }


    @Override
    public boolean attack(MapTile tile)
    {
        return false;
    }

    @Override
    public int getHealth()
    {
        return 0;
    }

    @Override
    public void setHealth(int i)
    {

    }

    @Override
    public void increaseHealth(int i)
    {

    }

    @Override
    public void decreaseHealth(int i)
    {

    }

    @Override
    public int getArmorClass()
    {
        return 0;
    }

    @Override
    public void setArmorClass(int armorClass)
    {

    }

    @Override
    public Weapon getWeapon()
    {
        return null;
    }

    @Override
    public void setWeapon(Weapon weapon)
    {

    }

    @Override
    public void search()
    {

    }

    @Override
    public int getId()
    {
        logger.error("world does not have a number");
        return -1;
    }

    @Override
    public void setId(int i)
    {
        logger.error("world does not have a number");
    }

    public String toString()
    {
        return "World TBD";
    }

    @Override
    public boolean wieldWeapon(Weapon weapon)
    {
        return false;
    }

    @Override
    public Point getUIPosition()
    {
        return new Point(-1, -1);
    }

    @Override
    public boolean moveTo(MapTile tileByCoordinates)
    {
        return false;
    }

    @Override
    public LifeForm getVictim()
    {
        return null;
    }

    @Override
    public void setVictim(LifeForm npc)
    {

    }

    @Override
    public Point getOriginalMapPosition()
    {
        return null;
    }

    @Override
    public boolean isRanged()
    {
        return false;
    }

    @Override
    public void switchWeapon(WeaponTypes ranged)
    {

    }

    @Override
    public boolean isStatic()
    {
        return true;
    }

    @Override
    public boolean isHostile()
    {
        return false;
    }

    @Override
    public void setHostile(boolean b)
    {

    }

    @Override
    public void evade()
    {

    }

    @Override
    public Point getOriginalTargetMapPosition()
    {
        return null;
    }

    @Override
    public void setOriginalTargetMapPosition(Point targetMapPosition)
    {

    }

    @Override
    public Point getTargetMapPosition()
    {
        return null;
    }

    @Override
    public void setTargetMapPosition(Point targetMapPosition)
    {

    }

    @Override
    public boolean isPatrolling()
    {
        return false;
    }

    @Override
    public void setPatrolling(boolean patrolling)
    {

    }

    @Override
    public Hashtable<String, String> getMobasks()
    {
        return null;
    }

    @Override
    public Schedule getSchedule()
    {
        return null;
    }

    @Override
    public void setSchedule(Schedule schedule)
    {

    }

    @Override
    public AbstractKeyboardAction getRunningAction()
    {
        return null;
    }

    @Override
    public void setRunningAction(AbstractKeyboardAction action)
    {

    }

    @Override
    public AbstractKeyboardAction lookForExit()
    {
        return null;
    }

    @Override
    public void look(MapTile tile)
    {

    }

    @Override
    public void say(String message)
    {

    }

    @Override
    public boolean isPlayer()
    {
        return false;
    }

    @Override
    public String toXML()
    {
        return "";
    }

    @Override
    public Collection<NPCProperty> getProperties()
    {
        return List.of();
    }

    @Override
    public LifeFormState getState()
    {
        return null;
    }

    @Override
    public void setState(LifeFormState state)
    {

    }

    @Override
    public CommandQueue getQueuedActions()
    {
        logger.error("world does not need a command queue - yet");
        return null;
    }

    public void doAction(AbstractAction action)
    {
        switch (action.getTitle())
        {
            case "Bla 1":
                //logger.info("do something");
                if (Game.getCurrent().getCurrentMap().isSpawnMobs())
                {
                    spawnNPC();
                }
                break;
            default:
                //logger.info("nothing");
                break;
        }
    }

    /**
     * spawn generator - needs to be done properly
     * So how do we want to spawn an npc?
     * TODO add additional factors - time, place, level luck?
     */
    private void spawnNPC()
    {
        if (GameConfiguration.debugWorld == true)
        {
            logger.info("spawning NPC begin");
        }
        if (Game.getCurrent().getCurrentMap().getSpawnCounter() >= Game.getCurrent().getCurrentMap().getSpawnCounterThreshold())
        {
            if (GameConfiguration.debugWorld == true)
            {
                logger.debug("reached max spawn limit");
            }
            return;
        }

        Random rand = new Random();

        if (rand.nextInt(100) >= Game.getCurrent().getCurrentMap().getSpawnProbabilityThreshold())
        {
            NPC n1 = NPCFactory.createNPC(1);
            n1.setSpawned(true);
            //identify which direction player is moving that we can spawn the npc:
            MapTile tile = null;
            switch (Game.getCurrent().getPlayerAction().getEvent().getType())
            {
                case NORTH:
                    tile = UILense.getCurrent().mapTiles[MapUtils.getMiddle()][0];
                    break;
                case EAST:
                    tile = UILense.getCurrent().mapTiles[MapUtils.getMiddle()][GameConfiguration.numberOfTiles - 1];
                    break;
                case WEST:
                    tile = UILense.getCurrent().mapTiles[GameConfiguration.numberOfTiles - 1][MapUtils.getMiddle()];
                    break;
                case SOUTH:
                    tile = UILense.getCurrent().mapTiles[0][MapUtils.getMiddle()];
                    break;
                default:
                    logger.info("do nothing");
                    break;
            }
            if (tile != null)
            {
                if (tile.isBlocked() == false)
                {
                    n1.setMapPosition(new Point(tile.getMapPosition().x, tile.getMapPosition().y));
                    n1.setHostile(true);
                    n1.setVictim(Game.getCurrent().getCurrentPlayer());
                    Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
                    tile.setLifeForm(n1);
                    n1.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(n1.getMapPosition()));
                    Game.getCurrent().getCurrentMap().setSpawnCounter(Game.getCurrent().getCurrentMap().getSpawnCounter() + 1);
                    if (GameConfiguration.debugWorld == true)
                    {
                        logger.debug("spawned NPC");
                    }
                }
            }
        }
        if (GameConfiguration.debugWorld == true)
        {
            logger.debug("spawning NPC end");
        }
    }
}
