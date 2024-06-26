package net.ck.mtbg.backend.entities.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.animation.lifeform.AnimationSystemTimerTask;
import net.ck.mtbg.animation.lifeform.AnimationSystemUtilTimer;
import net.ck.mtbg.animation.lifeform.HitMissImageTimerTask;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.Inventory;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.attributes.Attributes;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.items.*;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.astar.AStar;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.mtbg.util.communication.keyboard.*;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.Directions;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.NPCUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractEntity is the abstract parent class known subclasses are: Player NPC World
 *
 * @author Claus
 */
@Log4j2
@Getter
@Setter
@ToString
public abstract class AbstractEntity implements LifeForm, Serializable
{
    /**
     * the position on the map, filled for NPC and Player, not filled for World
     */
    protected Point mapPosition;

    /**
     * which weapon does entity wield?
     */
    protected Weapon weapon;
    /**
     * how much health does the entity have?
     */
    protected int health;
    /**
     * armor class - we just add ac on top of each other, no body parts and so on
     */
    protected int armorClass;

    /**
     * state of the PC/NPC - alive or dead or any other really.
     */
    protected LifeFormState state;

    /**
     * number has either the player number or the number of the npc on the current map
     */
    protected int id;
    /**
     * the position on the UI, not sure whether it makes more sense to take position
     */
    private Point UIPosition;
    /**
     * hashtable with weapon, not sure whether this is one or two slots.
     */
    private Hashtable<Weapon, AbstractItem> holdEquipment = new Hashtable<>();

    /**
     * which shield does entity hold?
     */
    private AbstractItem shield;
    /**
     * armor types contains all the armor positions, so this is the way to go.
     */
    private Hashtable<ArmorPositions, AbstractItem> wearEquipment = new Hashtable<>();
    /**
     * each AbstractEntity has an inventory.
     */
    private Inventory inventory;
    /**
     * each Abstract Entity has attributes
     */
    private Attributes attributes;

    /**
     * is the entity a light source
     */
    private boolean lightSource;

    /**
     * how far does the light range go
     */
    private int lightRange;

    /**
     * what level does the entity have?
     */
    private int level;

    /**
     * what is the index of the  current image in the list
     */
    private int currImage;

    /**
     * what actions does the entity have in the queque?
     */
    private CommandQueue queuedActions;

    /**
     * what spells does the entity know?
     */
    private CopyOnWriteArrayList<AbstractSpell> spells;

    /**
     * what npctype is the entity?
     */
    private NPCType type;

    /**
     * what skills does the entity know?
     */
    private CopyOnWriteArrayList<AbstractSkill> skills;
    /**
     * who is the victim of the hostile npc?
     * Can also be used for non-hostile in case of healing and so on
     */
    private LifeForm victim;
    /**
     * has the NPC been spawned?
     */

    private boolean spawned;


    public AbstractEntity()
    {
        spells = new CopyOnWriteArrayList<>();
        skills = new CopyOnWriteArrayList<>();
        inventory = new Inventory();
        attributes = new Attributes();

        setLevel(1);
        setCurrImage(0);
        setState(LifeFormState.ALIVE);
    }

    public void setVictim(LifeForm victim)
    {
        if (!(victim.getState().equals(LifeFormState.DEAD)))
        {
            this.victim = victim;
        }
    }

    /**
     * so this is the method where the a* algorithm needs to go into.
     * TBD.
     *
     * @param tileByCoordinates the target map tile
     * @return success or not
     */
    private boolean moveToRecursive(MapTile tileByCoordinates)
    {
        logger.info("start: {}", MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        while (!(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition())).equals(tileByCoordinates)))
        {
            AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()), tileByCoordinates, Game.getCurrent().getCurrentMap());
            ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();
            for (MapTile node : path)
            {
                logger.info(node);
            }
            MapTile nextTile = path.get(1);
            if (nextTile.getMapPosition().equals(getMapPosition()))
            {
                logger.info("start node");
                path.remove(0);
            }
            else
            {
                logger.info("next tile: {} ", nextTile);
                if (nextTile.x > getMapPosition().x)
                {
                    logger.info("add east");
                    Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new EastAction());
                }

                else if (nextTile.x < getMapPosition().x)
                {
                    logger.info("add west");
                    Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new WestAction());
                }

                else if (nextTile.y > getMapPosition().y)
                {
                    logger.info("add south");
                    Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new SouthAction());
                }

                else if (nextTile.y < getMapPosition().y)
                {
                    logger.info("add north");
                    Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new NorthAction());
                }
                WindowBuilder.getController().runQueue();
            }
        }
        return true;
    }

    private boolean moveToBasic(MapTile tileByCoordinates)
    {
        logger.info("start: {}", MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        Point p = new Point(getMapPosition().x, getMapPosition().y);
        while (p.x < tileByCoordinates.getMapPosition().x)
        {
            p.move((p.x + 1), p.y);
            Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new EastAction());
            logger.info("move east");
        }

        while (p.x > tileByCoordinates.getMapPosition().x)
        {
            Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new WestAction());
            p.move((p.x - 1), p.y);
            logger.info("move west");
        }

        while (p.y < tileByCoordinates.getMapPosition().y)
        {
            p.move((p.x), p.y + 1);
            Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new SouthAction());
            logger.info("move south");
        }

        while (p.y > tileByCoordinates.getMapPosition().y)
        {
            p.move((p.x), p.y - 1);
            Game.getCurrent().getCurrentPlayer().getQueuedActions().addEntry(new NorthAction());
            logger.info("move north");
        }
        return true;
    }


    public boolean dropItem(AbstractItem affectedItem, MapTile tile)
    {
        tile.getInventory().add(affectedItem);
        return this.dropItem(affectedItem);
    }


    public boolean wearItemAtPosition(Armor armor, ArmorPositions pos)
    {
        if (getInventory().contains(armor))
        {
            if (getWearEquipment().get(pos) == null)
            {
                getWearEquipment().put(pos, armor);
                getInventory().remove(armor);
                return true;
            }
        }
        return false;
    }

    public boolean wearItem(Armor armor)
    {
        if (getInventory().contains(armor))
        {
            if (getWearEquipment().get(armor.getPosition()) == null)
            {
                getWearEquipment().put(armor.getPosition(), armor);
                getInventory().remove(armor);
                return true;
            }
            else
            {
                logger.info("already wearing something");
                return false;
            }
        }
        return false;
    }

    public abstract boolean wieldWeapon(Weapon weapon);

    public boolean removeItem(Armor armor)
    {
        if (getWearEquipment().get(armor.getPosition()) != null)
        {
            getWearEquipment().put(armor.getPosition(), null);
            getInventory().add(armor);
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean removeItem(ArmorPositions pos)
    {
        if (getWearEquipment().get(pos) != null)
        {
            getInventory().add(getWearEquipment().get(pos));
            logger.info("Wear  equipment: {}", getWearEquipment());
            getWearEquipment().remove(pos);
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean removeWeapon(Weapon weapon)
    {
        if (getHoldEquipment() != null)
        {
            getHoldEquipment().put(weapon, null);
            getInventory().add(weapon);
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized Inventory getInventory()
    {
        return inventory;
    }


    public boolean getItem(AbstractItem item)
    {
        getInventory().add(item);
        return true;
    }

    /**
     * gets the top item from the map tile
     *
     * @param tile the map tile under the cursor
     * @return success or false
     */
    public boolean getItem(MapTile tile)
    {
        if (tile.getInventory() != null)
        {
            if (tile.getInventory().isEmpty())
            {
                return false;
            }
            else
            {
                getInventory().add(tile.getInventory().get(tile.getInventory().getSize() - 1));
                tile.getInventory().remove(tile.getInventory().get(tile.getInventory().getSize() - 1));
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean getItems(ArrayList<AbstractItem> items)
    {
        for (AbstractItem i : items)
        {
            getInventory().add(i);
        }
        return true;
    }

    public boolean dropItem(AbstractItem item)
    {
        getInventory().remove(item);
        return true;
    }

    public void equipItem(int selectionIndex)
    {
        AbstractItem item = getInventory().getElementAt(selectionIndex);
        if (item instanceof Weapon)
        {
            logger.info("wielding weapon");
            wieldWeapon((Weapon) item);
        }
        else if (item instanceof Armor)
        {
            wearItem((Armor) item);
        }
    }

    public GetAction lookAroundForItems()
    {
        Point north = new Point(getMapPosition().x, (getMapPosition().y - 1));
        Point east = new Point(getMapPosition().x + 1, (getMapPosition().y));
        Point south = new Point(getMapPosition().x, (getMapPosition().y + 1));
        Point west = new Point(getMapPosition().x - 1, (getMapPosition().y));
        GetAction getAction = null;

        if ((north.x >= 0 && north.y >= 0) && (!(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(north)).getInventory().isEmpty())))
        {
            getAction = new GetAction(north);
        }
        else if ((east.x >= 0 && east.y >= 0) && (!(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(east)).getInventory().isEmpty())))
        {
            getAction = new GetAction(east);
        }
        else if ((south.x >= 0 && south.y >= 0) && (!(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(south)).getInventory().isEmpty())))
        {
            getAction = new GetAction(south);
        }
        else if ((west.x >= 0 && west.y >= 0) && (!(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(west)).getInventory().isEmpty())))
        {
            getAction = new GetAction(west);
        }
        return getAction;
    }

    /**
     * @param x
     * @param y
     */
    public void move(int x, int y)
    {


        Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition())).setBlocked(false);
        MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setLifeForm(null);
        this.getMapPosition().move(x, y);
        Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition())).setBlocked(true);
        MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setLifeForm(this);


        //EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
    }

    /**
     * @param tileByCoordinates target tile
     */
    public boolean moveTo(MapTile tileByCoordinates)
    {
        if (GameConfiguration.debugASTAR == true)
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
                // logger.info("target can be reached");
                //return true;
                //((NPC) this).doAction(new PlayerAction((AbstractKeyboardAction) getQueuedActions().poll()));
            }
        }
        return false;
    }


    public boolean attack(MapTile tileByCoordinates)
    {
        logger.info("{} attacking {}", this.getId(), tileByCoordinates.getLifeForm());
        MapTile tile;

        if (getVictim() != null)
        {
            if (!(getVictim().getState().equals(LifeFormState.DEAD)))
            {
                tile = MapUtils.getMapTileByCoordinatesAsPoint(getVictim().getMapPosition());
            }
            else
            {
                tile = tileByCoordinates;
            }
        }
        else
        {
            tile = tileByCoordinates;
        }

        EventBus.getDefault().post(new GameStateChanged(GameState.COMBAT));

        if (tile != null)
        {
            if (tile.getLifeForm() != null)
            {
                LifeForm n = tile.getLifeForm();
                logger.info("we found the tile {}, its the victims: {}", tile, n);

                if (getWeapon() == null)
                {
                    setWeapon(ItemManager.getWeaponList().get(1));
                }

                n.setHostile(true);
                n.setVictim(this);

                if (getWeapon().getType().equals(WeaponTypes.RANGED))
                {
                    logger.info("here at ranged attack");
                    Point sourcePosition = NPCUtils.calculateScreenPositionFromMapPosition(this.getMapPosition());
                    Point targetPosition = NPCUtils.calculateScreenPositionFromMapPosition(tile.getMapPosition());
                    Missile m = new Missile(sourcePosition, targetPosition);
                    Game.getCurrent().getCurrentMap().getMissiles().add(m);

                    try
                    {
                        UIStateMachine.setHitAnimationRunning(true);
                        HitMissImageTimerTask task = new HitMissImageTimerTask(n);
                        TimerManager.getHitMissImageTimer().setHitMissImageTimerTask(task);
                        TimerManager.getHitMissImageTimer().schedule(TimerManager.getHitMissImageTimer().getHitMissImageTimerTask(), GameConfiguration.hitormissTimerDuration);
                        TimerManager.getAnimationSystemUtilTimer().cancel();
                        TimerManager.getAnimationSystemUtilTimer().purge();
                        TimerManager.setAnimationSystemUtilTimer(new AnimationSystemUtilTimer());
                        AnimationSystemTimerTask animationSystemTimerTask = new AnimationSystemTimerTask();
                        TimerManager.getAnimationSystemUtilTimer().schedule(animationSystemTimerTask, GameConfiguration.animationLifeformDelay, GameConfiguration.animationLifeformDelay);
                        UIStateMachine.setHitAnimationRunning(false);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    logger.info("hitting victim: {}", n);
                    if (!(n.getState().equals(LifeFormState.DEAD)))
                    {
                        if (NPCUtils.calculateHit(this, n))
                        {
                            logger.info("hit");
                            n.decreaseHealth(5);
                            EventBus.getDefault().post(new AnimatedRepresentationChanged(n));
                            return true;
                        }
                        else
                        {
                            logger.info("miss");
                            n.evade();
                            EventBus.getDefault().post(new AnimatedRepresentationChanged(n));
                            return false;
                        }
                    }
                    else
                    {
                        logger.info("victim {} is dead", n);
                        n.evade();
                        EventBus.getDefault().post(new AnimatedRepresentationChanged(n));
                        return false;
                    }
                }
                else
                {
                    logger.info("meleee");

                    logger.info("hitting victim: {}", n);
                    if (NPCUtils.calculateHit(this, n))
                    {
                        logger.info("hit");
                        n.decreaseHealth(5);
                        return true;
                    }
                    else
                    {
                        logger.info("miss");
                        n.evade();
                        return false;
                    }
                }
            }
            else
            {
                logger.debug("no lifeform on tile");
                return false;
            }
        }
        else
        {
            logger.debug("no tile");
            return false;
        }
    }


    @Override
    public void evade()
    {
        this.setCurrImage(ImageManager.getActionImage(ActionStates.MISS));
        EventBus.getDefault().post(new AnimatedRepresentationChanged(this));
    }


    @Override
    public void increaseHealth(int i)
    {
        this.setCurrImage(ImageManager.getActionImage(ActionStates.HEAL));
        EventBus.getDefault().post(new AnimatedRepresentationChanged(this));

        if (getHealth() >= 0)
        {
            setHealth(getHealth() + i);
            setState(LifeFormState.ALIVE);
        }
        else
        //what is dead will stay dead
        {
            setHealth(-1);
            setState(LifeFormState.DEAD);
            setHostile(false);
        }
    }

    @Override
    public void decreaseHealth(int i)
    {
        this.setCurrImage(ImageManager.getActionImage(ActionStates.HIT));
        EventBus.getDefault().post(new AnimatedRepresentationChanged(this));

        if (getHealth() - i > 0)
        {
            setHealth(getHealth() - i);
        }
        else if (getHealth() - i == 0)
        {
            setHealth(0);
            setState(LifeFormState.UNCONSCIOUS);
            setHostile(false);
        }
        else
        {
            setHealth(-1);
            setState(LifeFormState.DEAD);
            setHostile(false);
        }
        setHealth(-1);
        setState(LifeFormState.DEAD);
        setHostile(false);

        if (getState().equals(LifeFormState.DEAD))
        {
            Game.getCurrent().getCurrentMap().setSpawnCounter(Game.getCurrent().getCurrentMap().getSpawnCounter() - 1);
            MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setBlocked(false);
        }
    }


    protected boolean push(Point source)
    {
        MapTile sourceTile = Game.getCurrent().getCurrentMap().mapTiles[source.x][source.y];
        Directions sourceDir = MapUtils.calculateDirectionOfMapTileFromPlayer(sourceTile.getMapPosition(), this.getMapPosition());
        MapTile targetTile = MapUtils.calculateTileByDirection(sourceTile.getMapPosition(), sourceDir);

        if (sourceTile.getFurniture() != null)
        {
            if (targetTile.isBlocked())
            {
                return false;
            }
            else
            {
                targetTile.setFurniture(sourceTile.getFurniture());
                sourceTile.setFurniture(null);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    protected boolean yank(Point source)
    {
        MapTile sourceTile = Game.getCurrent().getCurrentMap().mapTiles[source.x][source.y];
        Directions sourceDir = MapUtils.calculateDirectionOfMapTileFromPlayer(sourceTile.getMapPosition(), this.getMapPosition());
        Directions targetDir = MapUtils.invertDirection(sourceDir);
        MapTile targetTile = MapUtils.calculateTileByDirection(this.getMapPosition(), targetDir);

        if (sourceTile.getFurniture() != null)
        {
            if (targetTile.isBlocked())
            {
                return false;
            }
            else
            {
                targetTile.setFurniture(sourceTile.getFurniture());
                sourceTile.setFurniture(null);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean hasTwoActions()
    {
        return getAttributes().get(AttributeTypes.DEXTERITY).getValue() >= GameConfiguration.dexterityThreshold;
    }

    /**
     * So we have found an exit point - switch to the target map - map is identified by name for now NPCs stay on the map on the places in the state that they are in. If there is a time system in the
     * future, perhaps there will be npc routines that are running even if the player is not on the map and only drawn once the player is on the map? Player needs to be removed from the map once the
     * map is being switched. needs to be added to the new map.
     */
    public boolean switchMap()
    {
        logger.info("start: switching map");

        MapTile exit = MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition());
        String mapName = null;
        if (exit != null)
        {
            mapName = exit.getTargetMap();
        }
        else
        {
            return false;
        }
        Point target = new Point();

        if (exit != null)
        {
            target = exit.getTargetCoordinates();
        }
        logger.info("mapname: {}, target Tile coordinates: {}", mapName, target);

        for (Map m : Game.getCurrent().getMaps())
        {
            if (m.getName().equalsIgnoreCase(mapName))
            {
                m.initialize();
                logger.debug("mapname: {}", mapName);
                logger.debug("current map before: {}", Game.getCurrent().getCurrentMap());
                MapTile targetTile = MapUtils.getMapTileByCoordinates(m, target.x, target.y);
                Game.getCurrent().setCurrentMap(m);
                logger.debug("current map after: {}", Game.getCurrent().getCurrentMap());
                assert targetTile != null;
                Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition())).setBlocked(false);
                MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setLifeForm(null);
                this.getMapPosition().move(targetTile.x, targetTile.y);
                Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition())).setBlocked(true);
                logger.info("player map pos: {}", this.getMapPosition());
                logger.debug("target tile: {}", targetTile);
                MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()).setLifeForm(this);
                //EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));

                logger.debug("new position: {}", this.getMapPosition());
                break;
                //setAnimatedEntities(animatedEntities = new ArrayList<>());
                //addAnimatedEntities();
            }
        }
        logger.info("end: switching map");
        return true;
    }

    protected void castSpell(AbstractAction action)
    {
        MapTile tile = Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere()));
        logger.info("cast spell: {}", action.getEvent().getCurrentSpell().getName());
        switch (action.getEvent().getCurrentSpell().getId())
        {
            case 1:
                if (tile.getLifeForm() != null)
                {
                    tile.getLifeForm().decreaseHealth(5);
                    tile.getLifeForm().setHostile(true);
                    tile.getLifeForm().setVictim(this);
                }
                break;
            case 2:
                if (tile.getLifeForm() != null)
                {
                    tile.getLifeForm().increaseHealth(5);
                }
                break;
            case 3:
                if (tile.getLifeForm() != null)
                {
                    tile.getLifeForm().setState(LifeFormState.ALIVE);
                }
                break;
            default:
                logger.debug("unknown spell");
        }

        //logger.debug("to be implemented");
    }

    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
        if (UIStateMachine.isUiOpen())
        {
            Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(position)).setBlocked(true);
        }
    }
}
