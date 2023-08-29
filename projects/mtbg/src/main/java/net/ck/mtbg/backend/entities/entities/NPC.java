package net.ck.mtbg.backend.entities.entities;

import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.queuing.ScheduleActivity;
import net.ck.mtbg.backend.state.CommandSuccessMachine;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.MapUtils;
import net.ck.mtbg.util.astar.AStar;
import net.ck.mtbg.util.communication.keyboard.*;
import net.ck.mtbg.util.communication.time.GameTimeChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.*;

public class NPC extends AbstractEntity implements LifeForm
{

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private Hashtable<String, String> mobasks;
    private boolean hostile;
    private boolean isStatic;


    private LifeForm victim;
    private boolean ranged;
    /**
     * original position on the map - remember the placement that the npc does not wander off too much
     */
    private Point originalMapPosition;


    private Point originalTargetMapPosition;

    private Point targetMapPosition;
    private boolean patrolling;

    private Schedule schedule;

    private AbstractKeyboardAction runningAction;

    public NPC(Integer i, Point p) {
        //logger.info("initialize properly");
        setStatic(false);
        setOriginalMapPosition(new Point(p.x, p.y));
        setMapPosition(new Point(p.x, p.y));

        setQueuedActions(new CommandQueue());
        EventBus.getDefault().register(this);

        if (getAttributes().get(AttributeTypes.STRENGTH).getValue() == 0) {
            getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.DEXTERITY).getValue() == 0) {
            getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.CONSTITUTION).getValue() == 0) {
            getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.INTELLIGENCE).getValue() == 0) {
            getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        }

        setHealth(Game.getCurrent().getBaseHealth() + (getLevel() * 10));
        setState(LifeFormState.ALIVE);
        setArmorClass(0);
        getInventory().add(ItemManager.getWeaponList().get(3));
        wieldWeapon(ItemManager.getWeaponList().get(1));
    }

    public NPC()
    {
        super();

        mobasks = new Hashtable<>();
    }

    /**
     * describes whether a npc is moving or not (outside of schedules)
     *
     * @return true - meaning static, or false, meaning moving
     */
    public boolean isStatic()
    {
        return isStatic;
    }

    public void setStatic(boolean aStatic)
    {
        isStatic = aStatic;
    }

    public boolean isHostile()
    {
        return hostile;
    }

    public void setHostile(boolean hostile)
    {
        this.hostile = hostile;
    }



    @Override
    public Point getOriginalTargetMapPosition()
    {
        return originalTargetMapPosition;
    }

    @Override
    public void setOriginalTargetMapPosition(Point targetMapPosition)
    {
        this.originalTargetMapPosition = targetMapPosition;
    }

    @Override
    public Point getTargetMapPosition()
    {
        return targetMapPosition;
    }

    @Override
    public void setTargetMapPosition(Point targetMapPosition)
    {
        this.targetMapPosition = targetMapPosition;
    }

    public Point getOriginalMapPosition()
    {
        return originalMapPosition;
    }

    public void setOriginalMapPosition(Point originalMapPosition)
    {
        this.originalMapPosition = originalMapPosition;
    }

    @Override
    public LifeFormState getState()
    {
        return state;
    }

    @Override
    public void setState(LifeFormState state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "NPC [id=" + id + ", mapposition=" + mapPosition + ", mobasks=" + (mobasks != null ? toString(mobasks.entrySet()) : null) + "]";
    }

    @Override
    public boolean wieldWeapon(Weapon weapon)
    {
        if (getInventory().contains(weapon))
        {
            if (getWeapon() == null)
            {
                logger.info("wield weapon: {}", weapon);
                setWeapon(weapon);
                getInventory().remove(weapon);
                return true;
            }
            else
            {
                logger.info("weapon: {}", getWeapon());
                logger.info("cannot wield weapon");
                return false;
            }
        }
        //logger.info("should not be reachable");
        return false;
    }

    private String toString(Collection<?> collection)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < 2; i++)
        {
            if (i > 0)
            {
                builder.append(", ");
            }
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

    public void initialize()
    {
        setStatic(false);
        setQueuedActions(new CommandQueue());
        EventBus.getDefault().register(this);
        getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        setHealth(Game.getCurrent().getBaseHealth() + (getLevel() * 10));
        setState(LifeFormState.ALIVE);
        setArmorClass(0);
        getInventory().add(ItemManager.getWeaponList().get(3));
        getInventory().add(ItemManager.getWeaponList().get(1));
        wieldWeapon(ItemManager.getWeaponList().get(1));

        setCurrImage(0);

        //TODO broken as fuck:
        //TODO initialized a npc not even on this map, breaking things because it does not have a type then

        /*if (getId() == 4)
        {
            logger.info("add schedule");
            setSchedule(new Schedule(this));
        }*/

        if (getOriginalMapPosition() == null)
        {
            setOriginalMapPosition(new Point(getMapPosition()));
        }
    }

    public Point getMapPosition()
    {
        return mapPosition;
    }

    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
    }

    public Logger getLogger()
    {
        return logger;
    }



    public Hashtable<String, String> getMobasks()
    {
        return mobasks;
    }

    public void setMobasks(Hashtable<String, String> mobasks)
    {
        this.mobasks = mobasks;
    }



    /**
     * @param event GameTime has changed, check if there is a defined schedule for the npc
     */
    @Subscribe
    public void onMessageEvent(GameTimeChanged event)
    {
        checkSchedules(event);
    }

    private void checkSchedules(GameTimeChanged event)
    {
        if (getSchedule() != null)
        {
            //easy for now
            if (getSchedule().getActivities() != null)
            {
                for (ScheduleActivity activity : getSchedule().getActivities())
                {
                    GameTime startTime = activity.getStartTime();

                    if (Game.getCurrent().getGameTime().getCurrentHour() == startTime.getCurrentHour())
                    {
                        if (Game.getCurrent().getGameTime().getCurrentMinute() == startTime.getCurrentMinute())
                        {
                            logger.error("activity: {}", activity.getActionString());
                            setRunningAction(activity.getAction());
                            doAction(new PlayerAction(activity.getAction()));
                        }
                    }
                }
            }
        }
    }

    /**
     * @param event Game Time has changed
     */
    private void checkSchedulesOld(GameTimeChanged event)
    {
        //logger.info("start: check schedules");
        if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 2)
        {
            // logger.info("check schedule");
            if (getMobasks().size() > 0)
            {
                //logger.info("running");
                // MoveAction action = new MoveAction();
                // action.setGetWhere(new Point(1, 0));
                //doAction(new PlayerAction(action));
            }
        }

        if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 20)
        {
            //logger.info("check schedule");
            if (getMobasks().size() > 0)
            {
                //logger.info("running");
                //MoveAction action = new MoveAction();
                //action.setGetWhere(getOriginalMapPosition());
                //doAction(new PlayerAction(action));
            }
        }
        //logger.info("end: check schedules");
    }

    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {
        logger.info("do action: {}", action.toString());
        if (getState() == LifeFormState.DEAD)
        {
            logger.info("npc dead");
            return;
        }

        Point p = getMapPosition();
        Point mapsize = Game.getCurrent().getCurrentMap().getSize();

        int xBorder = mapsize.x;
        int yBorder = mapsize.y;

        boolean success = false;

        switch (action.getType())
        {
            case EAST:
                // logger.info("p: {}", p.toString());

                if (p.x + 1 <= xBorder)
                {
                    if (!(MapUtils.lookAhead((p.x + 1), (p.y))))
                    {
                        logger.info("move east");
                        this.move((p.x + 1), p.y);
                        success = true;
                    }
                    else
                    {
                        logger.info("EAST blocked");
                    }
                }
                else
                {
                    logger.info("eastern border, ignore wrapping for now");
                }
                break;
            case ENTER:
                logger.info("loading new map");
                Game.getCurrent().switchMap();
                break;
            case ESC:
                break;
            case NORTH:
                // logger.info("p: {}", p.toString());
                if (p.y > 0)
                {
                    if (!(MapUtils.lookAhead((p.x), (p.y - 1))))
                    {
                        logger.info("move north");
                        this.move((p.x), (p.y - 1));
                        success = true;
                    }
                    else
                    {
                        logger.info("NORTH blocked");
                    }
                }
                else
                {
                    logger.info("already at zero y");
                }
                break;
            case NULL:
                logger.error("NULL?");
                break;
            case SOUTH:
                // logger.info("p: {}", p.toString());
                if (p.y + 1 <= yBorder)
                {
                    if (!(MapUtils.lookAhead((p.x), (p.y + 1))))
                    {
                        logger.info("move south");
                        this.move((p.x), (p.y + 1));
                        success = true;
                    }
                    else
                    {
                        //logger.info("SOUTH blocked");
                    }

                }
                else
                {
                    logger.info("southern border, ignore wrapping for now");
                }
                break;
            case WEST:
                // logger.info("p: {}", p.toString());
                if (p.x > 0)
                {
                    if (!(MapUtils.lookAhead((p.x - 1), (p.y))))
                    {
                        logger.info("move west");
                        this.move((p.x - 1), (p.y));
                        success = true;
                    }
                    else
                    {
                        //logger.info("WEST blocked");
                    }
                }
                else
                {
                    logger.info("already at zero x");
                }
                break;
            case SPACE:
                success = true;
                break;
            case GET:
                success = this.getItem(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;

            case DROP:
                success = this.dropItem(action.getEvent().getAffectedItem(), Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;

            case TALK:
                logger.info("doing talk action");
                break;

            case MOVE:
                success = this.moveTo(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere()));
                break;

            case SEARCH:
                search();
                break;

            case ATTACK:
                success = attack(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;
            default:
                logger.info("doing default action, inventory does not need to be reverted for instance");
                break;

        }
        action.setSuccess(success);
        CommandSuccessMachine.calculateSoundEffectNPC(action);
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
    public int getArmorClass()
    {
        return this.armorClass;
    }

    @Override
    public void setArmorClass(int armorClass)
    {
        this.armorClass = armorClass;
    }

    @Override
    public Weapon getWeapon()
    {
        return this.weapon;
    }

    @Override
    public void setWeapon(Weapon weapon)
    {
        logger.info("weapon {} for npc: {}", weapon, this.getId());
        this.weapon = weapon;
    }

    @Override
    public void search()
    {

    }

    public LifeForm getVictim()
    {
        return victim;
    }

    @Override
    public void setVictim(LifeForm victim)
    {
        this.victim = victim;
    }

    public boolean isRanged()
    {
        if (this.getWeapon() == null)
        {
            for (AbstractItem item : this.getInventory().getInventory())
            {
                if (item instanceof Weapon)
                {
                    if (((Weapon) item).getType().equals(WeaponTypes.RANGED))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            if (this.getWeapon().getType().equals(WeaponTypes.RANGED))
            {
                return true;
            }

            if (this.getWeapon().getType().equals(WeaponTypes.MELEE))
            {
                for (AbstractItem item : this.getInventory().getInventory())
                {
                    if (item instanceof Weapon)
                    {
                        if (((Weapon) item).getType().equals(WeaponTypes.RANGED))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }


    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    /**
     * @param ranged - switch to this weapon type
     */
    public void switchWeapon(WeaponTypes ranged)
    {
        logger.info("switching weapon");

        if (getWeapon() != null)
        {
            getInventory().add(getWeapon());
        }
        this.setWeapon(null);
        for (int i = 0; i < getInventory().getSize(); i++)
        {
            if (getInventory().getElementAt(i) instanceof Weapon)
            {
                if (((Weapon) getInventory().getElementAt(i)).getType().equals(ranged))
                {
                    this.wieldWeapon((Weapon) getInventory().getElementAt(i));
                }
            }
        }
    }




    public boolean isPatrolling()
    {
        return patrolling;
    }

    public void setPatrolling(boolean patrolling)
    {
        this.patrolling = patrolling;
    }

    public boolean moveTo(MapTile tileByCoordinates)
    {
        setQueuedActions(new CommandQueue());
        logger.info("start: {}", MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()), tileByCoordinates, Game.getCurrent().getCurrentMap());
        ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();
        Point futureMapPosition = new Point(getMapPosition().x, getMapPosition().y);
        for (MapTile node : path)
        {
            if (node.getMapPosition().equals(getMapPosition()))
            {
                //logger.info("start node");
            }
            else
            {
                //logger.info(node);
                if (node.x > futureMapPosition.x)
                {
                    getQueuedActions().addEntry(new EastAction());
                    futureMapPosition.move(futureMapPosition.x + 1, futureMapPosition.y);
                }
                else if (node.x < futureMapPosition.x)
                {
                    getQueuedActions().addEntry(new WestAction());
                    futureMapPosition.move(futureMapPosition.x - 1, futureMapPosition.y);
                }
                else if (node.y > futureMapPosition.y)
                {
                    getQueuedActions().addEntry(new SouthAction());
                    futureMapPosition.move(futureMapPosition.x, futureMapPosition.y + 1);
                }
                else if (node.y < futureMapPosition.y)
                {
                    getQueuedActions().addEntry(new NorthAction());
                    futureMapPosition.move(futureMapPosition.x, futureMapPosition.y - 1);
                }
            }
            if (node.getMapPosition().equals(tileByCoordinates.getMapPosition()))
            {
                //logger.info("target can be reached");
                //return true;
                doAction(new PlayerAction((AbstractKeyboardAction) getQueuedActions().poll()));
            }
        }
        return false;
    }


    public Schedule getSchedule()
    {
        return schedule;
    }

    @Override
    public void setSchedule(Schedule schedule)
    {
        this.schedule = schedule;
    }

    @Override
    public AbstractKeyboardAction getRunningAction()
    {
        return runningAction;
    }

    @Override
    public void setRunningAction(AbstractKeyboardAction action)
    {
        this.runningAction = action;
    }
}
