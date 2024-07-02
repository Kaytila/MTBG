package net.ck.mtbg.backend.entities.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.attributes.Attributes;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.state.CommandSuccessMachine;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.items.*;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.astar.AStar;
import net.ck.mtbg.util.communication.keyboard.*;
import net.ck.mtbg.util.communication.time.GameTimeChanged;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.*;

@Log4j2
@Getter
@Setter
@ToString
public class NPC extends AbstractEntity implements LifeForm
{
    private Hashtable<String, String> mobasks;

    private boolean hostile;

    /**
     * describes whether a npc is moving or not (outside of schedules)
     *
     * @return true - meaning static, or false, meaning moving
     */
    private boolean isStatic;

    /**
     * helper variable - does the npc have a ranged weapon?
     */
    private boolean ranged;
    /**
     * original position on the map - remember the placement that the npc does not wander off too much
     */
    private Point originalMapPosition;

    /**
     * the original target map position
     */
    private Point originalTargetMapPosition;

    /**
     * the current target map position
     */
    private Point targetMapPosition;

    /**
     * is the npc patrolling between two fix points
     */
    private boolean patrolling;

    /**
     * does the NPC have a full schedule?
     */
    private Schedule schedule;

    private AbstractKeyboardAction runningAction;


    public NPC(Integer i, Point p)
    {
        //logger.info("initialize properly");
        setStatic(false);
        setOriginalMapPosition(new Point(p.x, p.y));
        setMapPosition(new Point(p.x, p.y));

        setQueuedActions(new CommandQueue());
        EventBus.getDefault().register(this);

        if (getAttributes().get(AttributeTypes.STRENGTH).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.DEXTERITY).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.CONSTITUTION).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.INTELLIGENCE).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        }

        setHealth(GameConfiguration.baseHealth + (getLevel() * 10));
        setState(LifeFormState.ALIVE);
        setArmorClass(0);
        getInventory().add(ItemManager.getWeaponList().get(3));
        wieldWeapon(ItemManager.getWeaponList().get(1));
    }

    public NPC()
    {
        super();
        mobasks = new Hashtable<>();
        setStatic(false);
        setQueuedActions(new CommandQueue());
        setHealth(GameConfiguration.baseHealth + (getLevel() * 10));
        setState(LifeFormState.ALIVE);
        setArmorClass(0);
        getInventory().add(ItemManager.getWeaponList().get(3));
        wieldWeapon(ItemManager.getWeaponList().get(1));
        EventBus.getDefault().register(this);

    }

    public NPC(NPC that)
    {

        this(that.getId(), that.isStatic(), that.isRanged(), that.isHostile(), that.isPatrolling(), that.isLightSource(), that.getLevel(), that.getMapPosition(), that.getSchedule(), that.getAttributes(), that.getOriginalMapPosition(), that.getOriginalTargetMapPosition(), that.getTargetMapPosition(), that.getMobasks(), that.getRunningAction(), that.getArmorClass(), that.getQueuedActions(), that.getCurrImage(), that.getHealth(), that.getHoldEquipment(), that.getInventory(), that.getLightRange(), that.getState(), that.getType());
    }

    public NPC(int id, boolean aStatic, boolean ranged, boolean hostile, boolean patrolling, boolean lightSource, int level, Point mapPosition, Schedule schedule, Attributes attributes, Point originalMapPosition, Point originalTargetMapPosition, Point targetMapPosition, Hashtable<String, String> mobasks, AbstractKeyboardAction runningAction, int armorClass, CommandQueue queuedActions, int currImage, int health, Hashtable<Weapon, AbstractItem> holdEquipment, Inventory inventory, int lightrange, LifeFormState state, NPCType type)
    {
        this.setId(id);
        this.setStatic(aStatic);
        this.setRanged(ranged);
        this.setHostile(hostile);
        this.setPatrolling(patrolling);
        this.setLightSource(lightSource);
        this.setLevel(level);
        //this.setMapPosition(mapPosition);
        this.setSchedule(schedule);
        this.setAttributes(attributes);
        this.setOriginalMapPosition(originalMapPosition);
        this.setOriginalTargetMapPosition(originalTargetMapPosition);
        this.setTargetMapPosition(targetMapPosition);
        this.setMobasks(mobasks);
        this.setRunningAction(runningAction);
        this.setQueuedActions(queuedActions);
        this.setArmorClass(armorClass);
        this.setCurrImage(currImage);
        this.setHealth(health);
        this.setHoldEquipment(holdEquipment);
        this.setInventory(inventory);
        this.setLightRange(lightrange);
        this.setState(state);
        this.setType(type);


        setStatic(false);
        //setOriginalMapPosition(new Point(getMapPosition().x, getMapPosition().y));
        setQueuedActions(new CommandQueue());
        EventBus.getDefault().register(this);

        if (getAttributes().get(AttributeTypes.STRENGTH).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.DEXTERITY).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.CONSTITUTION).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        }
        if (getAttributes().get(AttributeTypes.INTELLIGENCE).getValue() == 0)
        {
            getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        }

        setHealth(GameConfiguration.baseHealth + (getLevel() * 10));
        setState(LifeFormState.ALIVE);
        setArmorClass(0);
        getInventory().add(ItemFactory.createWeapon(ItemManager.getWeaponList().get(3).getId()));
        wieldWeapon(ItemFactory.createWeapon(ItemManager.getWeaponList().get(1).getId()));
        if (getTargetMapPosition() != null)
        {
            setPatrolling(true);
        }
    }

    @Override
    public boolean wieldWeapon(Weapon weapon)
    {
        if (getInventory().contains(weapon))
        {
            if (getWeapon() == null)
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("wield weapon: {}", weapon);
                }
                setWeapon(weapon);
                getInventory().remove(weapon);
                return true;
            }
            else
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("weapon: {}", getWeapon());
                    logger.info("cannot wield weapon");
                }
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
        setHealth(GameConfiguration.baseHealth + (getLevel() * 10));
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
            if (getMapPosition() != null)
            {
                setOriginalMapPosition(new Point(getMapPosition()));
            }
        }
    }

    /**
     * @param event GameTime has changed, check if there is a defined schedule for the npc
     */
    @Subscribe
    public void onMessageEvent(GameTimeChanged event)
    {
        //checkSchedules(event);
    }

    private void checkSchedules(GameTimeChanged event)
    {
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("npc id {} checking schedule", this.getId());
        }
    }


    /**
     * looks at the maptile - will show tile type, furniture, inventory in some way or another.
     *
     * @param maptile the maptile which was selected as target
     */
    public void look(MapTile maptile)
    {
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("looking:");
            logger.info("maptile: {}", maptile);
            logger.info("maptile furniture: {}", maptile.getFurniture());
            logger.info("maptile inventory: {}", maptile.getInventory());
            logger.info("maptile blocked: {}", maptile.isBlocked());
        }

        for (AbstractItem item : maptile.getInventory().getInventory())
        {
            logger.info("item: {}", item);
        }

        if (maptile.getFurniture() != null)
        {
            logger.debug("there is furniture");
            FurnitureItem item = maptile.getFurniture();
            if (item.isFurniture())
            {
                logger.debug("it really is furniture");
                if (item.isBurning())
                {
                    logger.debug("item is burning, turn it off");
                    item.setBurning(false);
                }
                else
                {
                    logger.debug("item is not burning, turn it on");
                    item.setBurning(true);
                }
            }
        }

        if (maptile.getType().equals(TileTypes.SIGNPOST))
        {
            move(maptile.getTargetCoordinates().x, maptile.getTargetCoordinates().y);
        }
    }

    public void say(String message)
    {
        logger.info("NPC {} says: {}", this.id, message);
    }


    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("do action: {}", action.toString());
        }
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
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("move east");
                        }
                        this.move((p.x + 1), p.y);
                        success = true;
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("EAST blocked");
                        }
                    }
                }
                else
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.info("eastern border, ignore wrapping for now");
                    }
                }
                break;
            case ENTER:
                logger.info("loading new map");
                switchMap();
                break;
            case ESC:
                break;
            case NORTH:
                // logger.info("p: {}", p.toString());
                if (p.y > 0)
                {
                    if (!(MapUtils.lookAhead((p.x), (p.y - 1))))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("move north");
                        }
                        this.move((p.x), (p.y - 1));
                        success = true;
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("NORTH blocked");
                        }
                    }
                }
                else
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.info("already at zero y");
                    }
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
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("move south");
                        }
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
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.info("southern border, ignore wrapping for now");
                    }
                }
                break;
            case WEST:
                // logger.info("p: {}", p.toString());
                if (p.x > 0)
                {
                    if (!(MapUtils.lookAhead((p.x - 1), (p.y))))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.info("move west");
                        }
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
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.info("already at zero x");
                    }
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
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("doing talk action");
                }
                break;

            case MOVE:
                success = this.moveTo(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere()));
                break;

            case SEARCH:
                search();
                break;

            case LOOK:
                this.look(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                /* this break decides whether after a look an attack follows, or now :D                 */
                break;

            case ATTACK:
                success = attack(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;
            default:
                logger.error("doing default action, inventory does not need to be reverted for instance");
                break;

        }
        action.setSuccess(success);
        CommandSuccessMachine.calculateSoundEffectNPC(action);
    }


    @Override
    public void search()
    {

    }


    public boolean isRanged()
    {
        if (this.getWeapon() == null)
        {
            for (AbstractItem item : this.getInventory().getInventory())
            {
                if (item instanceof Weapon)
                {
                    if (((Weapon) item).getType() != null)
                    {
                        if (((Weapon) item).getType().equals(WeaponTypes.RANGED))
                        {
                            return true;
                        }
                    }
                    else
                    {
                        logger.error("how the heck?");
                        ((Weapon) item).setType(WeaponTypes.RANGED);
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


    /**
     * @param ranged - switch to this weapon type
     */
    public void switchWeapon(WeaponTypes ranged)
    {
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("switching weapon");
        }
        if (getWeapon() != null)
        {
            getInventory().add(getWeapon());
        }
        this.setWeapon(null);
        for (int i = 0; i < getInventory().getSize(); i++)
        {
            if (getInventory().getElementAt(i) instanceof Weapon)
            {
                Weapon weapon = (Weapon) getInventory().getElementAt(i);
                if (weapon.getType() == null)
                {
                    //TODO hack not sure why this could be the case, but it is currently
                    weapon.setType(WeaponTypes.MELEE);
                }
                if (((Weapon) getInventory().getElementAt(i)).getType().equals(ranged))
                {
                    this.wieldWeapon((Weapon) getInventory().getElementAt(i));
                }
            }
        }
    }

    public boolean moveTo(MapTile tileByCoordinates)
    {
        setQueuedActions(new CommandQueue());
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("start: {}", MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()));
            logger.info("finish: {}", tileByCoordinates);
        }

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

    /**
     * specialized method for NPC for switching maps.
     * Kudos to:
     * <a href="https://stackoverflow.com/questions/37793591/how-do-i-copy-arraylistt-in-java-multi-threaded-environment">https://stackoverflow.com/questions/37793591/how-do-i-copy-arraylistt-in-java-multi-threaded-environment</a>
     *
     * @return
     */
    public boolean switchMap()
    {
        //TODO something is buggy there somewhere
        logger.info("start: switching map");

        MapTile exit = MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition());
        String mapName = null;
        if (exit != null)

        {
            mapName = exit.getTargetMap();
        }
        Point target = new Point();
        if (exit != null)
        {
            target = exit.getTargetCoordinates();
        }
        logger.info("mapname: {}, target Tile: {}", mapName, target);
        for (Map m : Game.getCurrent().getMaps())
        {
            if (m.getName().equalsIgnoreCase(mapName))
            {
                MapTile targetTile = MapUtils.getMapTileByCoordinates(m, target.x, target.y);

                Game.getCurrent().getCurrentMap().getLifeForms().remove(this);
                MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setLifeForm(null);
                m.getLifeForms().add(this);
                assert targetTile != null;
                this.setMapPosition(new Point(targetTile.x, targetTile.y));
                targetTile.setLifeForm(this);
                logger.debug("new position: {}", this.getMapPosition());
                //setAnimatedEntities(animatedEntities = new ArrayList<>());
                //addAnimatedEntities();
            }
        }
        logger.info("end: switching map");
        return true;
    }

    public EnterAction lookForExit()
    {
        EnterAction action = null;

        MapTile currentTile = MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition());

        if (currentTile.getTargetCoordinates() != null)
        {
            action = new EnterAction();
        }
        return action;
    }
}
