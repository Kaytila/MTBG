package net.ck.game.backend.entities;

import net.ck.game.backend.Game;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.items.*;
import net.ck.game.map.MapTile;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.astar.AStar;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.graphics.PlayerPositionChanged;
import net.ck.util.communication.keyboard.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

/**
 * AbstractEntity is the abstract parent class known subclasses are: Player NPC World
 *
 * @author Claus
 */
public abstract class AbstractEntity
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    /**
     * the position on the map, filled for NPC and Player, not filled for World
     */
    protected Point mapPosition;

    /**
     * number has either the player number or the number of the npc on the current map
     */
    private int number;

    /**
     * the position on the UI, not sure whether it makes more sense to take position
     */
    private Point UIPosition;

    /**
     * hashtable with weapon, not sure whether this is one or two slots. TBD
     */
    private Hashtable<Weapon, AbstractItem> holdEquipment = new Hashtable<>();


    private Weapon weapon;

    private AbstractItem shield;

    /**
     * armortypes contains all the armor positions, so this is the way to go.
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
     * is the entity a lightsource
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
     * how much health does the entity have?
     */
    private int health;

    /**
     * armor class - we just add ac on top of each other, no bodyparts and so on
     */
    private int armorClass;


    public AbstractEntity()
    {
        inventory = new Inventory();
        attributes = new Attributes();
        setLevel(1);
    }

    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {

        //logger.info("do action: {}", action.toString());
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
                        this.move((p.x + 1), p.y);
                        success = true;
                    }
                    else
                    {
                        //logger.info("EAST blocked");
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
                        this.move((p.x), (p.y - 1));
                        success = true;
                    }
                    else
                    {
                        //logger.info("NORTH blocked");
                    }
                }
                else
                {
                    logger.info("already at zero y");
                }
                break;
            case NULL:
                break;
            case SOUTH:
                // logger.info("p: {}", p.toString());
                if (p.y + 1 <= yBorder)
                {
                    if (!(MapUtils.lookAhead((p.x), (p.y + 1))))
                    {
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
                success = this.getItem(Objects.requireNonNull(MapUtils.getTileByCoordinates(action.getEvent().getGetWhere())));
                break;

            case DROP:
                success = this.dropItem(action.getEvent().getAffectedItem(), Objects.requireNonNull(MapUtils.getTileByCoordinates(action.getEvent().getGetWhere())));
                break;

            case TALK:
                logger.info("doing talk action");
                break;

            case MOVE:
                success = this.moveTo(MapUtils.getTileByCoordinates(action.getEvent().getGetWhere()));
                break;

            case SEARCH:
                this.search();
                break;

            case ATTACK:
                success = this.attack(action.getEvent());
                break;
            default:
                logger.info("doing default action, inventory does not need to be reverted for instance");
                break;
        }

        // so if the action was done successful, add the action to the turn
        // if not, create a null action and add this to the turn.
        if (success == true)
        {
            Game.getCurrent().getCurrentTurn().getActions().add((PlayerAction) action);
        }
        else
        {
            Game.getCurrent().getCurrentTurn().getActions().add(new PlayerAction(new AbstractKeyboardAction(), action.getEntity()));
        }
        //Game.getCurrent().getController().setCurrentAction(null);
    }

    /**
     *
     * @param action keyboard action (attack action)
     * @return returns whether it is a hit
     *
     * currently this method works for both PC and NPC.
     * this needs a rewrite. npcs and players are kept separate
     * guess one implementation in player and one in entity or in NPC should do the trick
     *
     */
    private boolean attack(AbstractKeyboardAction action)
    {
        logger.info("player attacking");
        MapTile tile;
        if (action.getTargetCoordinates() == null)
        {
            //this is a npc attacking
            tile = MapUtils.getTileByCoordinates(Game.getCurrent().getCurrentPlayer().getMapPosition());
        }
        else
        {
            tile = MapUtils.calculateMapTileUnderCursor(action.getTargetCoordinates());
        }
        logger.info("tile: {}", tile);
        if (tile != null)
        {
            if (getWeapon() == null)
            {
                setWeapon(Game.getCurrent().getWeaponList().get(1));
            }

            if (getWeapon().getType().equals(WeaponTypes.RANGED))
            {
                Missile m = new Missile(action.getSourceCoordinates(), action.getTargetCoordinates());
                Game.getCurrent().getCurrentMap().getMissiles().add(m);
                logger.info("tile: {}", tile);
                if (Game.getCurrent().getCurrentMap().getNpcs().size() > 0)
                {
                    for (NPC n : Game.getCurrent().getCurrentMap().getNpcs())
                    {
                        if (n.getMapPosition().equals(tile.getMapPosition()))
                        {
                            logger.info("hitting NPC: {}", n);
                            n.setAgressive(true);
                            m.setSuccess(NPCUtils.calculateHit(this, n));
                            logger.info("hit or no hit: {}", m.isSuccess());
                            break;
                        }
                    }
                }
                else
                {
                    //No NPCs
                }
            }
            else
            {
                if (Game.getCurrent().getCurrentMap().getNpcs().size() > 0)
                {
                    for (NPC n : Game.getCurrent().getCurrentMap().getNpcs())
                    {
                        if (n.getMapPosition().equals(tile.getMapPosition()))
                        {
                            logger.info("hitting NPC: {}", n);
                            n.setAgressive(true);
                            if (NPCUtils.calculateHit(this, n))
                            {
                                logger.info("hit");
                                n.getAppearance().setCurrentImage(ImageUtils.getHitImage());
                                EventBus.getDefault().post(new AnimatedRepresentationChanged(n));
                            }
                            else
                            {
                                logger.info("miss");
                            }
                            break;
                        }
                    }
                }
                else
                {
                    //No NPCs
                }
            }
        }
        return true;
    }

    private void attack(MapTile tileByCoordinates)
    {
        Game.getCurrent().getCurrentMap().getMissiles().add(new Missile(MapUtils.getTileByCoordinates(getMapPosition()), tileByCoordinates));
    }

    void search()
    {
        for (int xStart = getMapPosition().x - 1; xStart <= getMapPosition().x + 1; xStart++)
        {
            for (int yStart = getMapPosition().y - 1; yStart <= getMapPosition().y + 1; yStart++)
            {
                logger.info("searching maptile: {}", MapUtils.getTileByCoordinates(new Point(xStart, yStart)));
            }
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
        logger.info("start: {}", MapUtils.getTileByCoordinates(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        while (!(Objects.requireNonNull(MapUtils.getTileByCoordinates(getMapPosition())).equals(tileByCoordinates)))
        {
            AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getTileByCoordinates(getMapPosition()), tileByCoordinates, Game.getCurrent().getCurrentMap());
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
                    Game.getCurrent().getCommandQueue().addEntry(new EastAction());
                }

                else if (nextTile.x < getMapPosition().x)
                {
                    logger.info("add west");
                    Game.getCurrent().getCommandQueue().addEntry(new WestAction());
                }

                else if (nextTile.y > getMapPosition().y)
                {
                    logger.info("add south");
                    Game.getCurrent().getCommandQueue().addEntry(new SouthAction());
                }

                else if (nextTile.y < getMapPosition().y)
                {
                    logger.info("add north");
                    Game.getCurrent().getCommandQueue().addEntry(new NorthAction());
                }
                Game.getCurrent().getController().runQueue();
            }
        }
        return true;
    }

    private boolean moveToBasic(MapTile tileByCoordinates)
    {
        logger.info("start: {}", MapUtils.getTileByCoordinates(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        Point p = new Point(getMapPosition().x, getMapPosition().y);
        while (p.x < tileByCoordinates.getMapPosition().x)
        {
            p.move((p.x + 1), p.y);
            Game.getCurrent().getCommandQueue().addEntry(new EastAction());
            logger.info("move east");
        }

        while (p.x > tileByCoordinates.getMapPosition().x)
        {
            Game.getCurrent().getCommandQueue().addEntry(new WestAction());
            p.move((p.x - 1), p.y);
            logger.info("move west");
        }

        while (p.y < tileByCoordinates.getMapPosition().y)
        {
            p.move((p.x), p.y + 1);
            Game.getCurrent().getCommandQueue().addEntry(new SouthAction());
            logger.info("move south");
        }

        while (p.y > tileByCoordinates.getMapPosition().y)
        {
            p.move((p.x), p.y - 1);
            Game.getCurrent().getCommandQueue().addEntry(new NorthAction());
            logger.info("move north");
        }
        return true;
    }

    public boolean moveTo(MapTile tileByCoordinates)
    {


        logger.info("start: {}", MapUtils.getTileByCoordinates(getMapPosition()));
        logger.info("finish: {}", tileByCoordinates);

        AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getTileByCoordinates(getMapPosition()), tileByCoordinates, Game.getCurrent().getCurrentMap());
        ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();
        Point futureMapposition = new Point(getMapPosition().x, getMapPosition().y);
        for (MapTile node : path)
        {
            if (node.getMapPosition().equals(getMapPosition()))
            {
                logger.info("start node");
            }
            else
            {
                logger.info(node);
                if (node.x > futureMapposition.x)
                {

                    if (this instanceof Player)
                    {
                        Game.getCurrent().getCommandQueue().addEntry(new EastAction());
                    }
                    else if (this instanceof NPC)
                    {
                        logger.info("add east");
                        NPC n = (NPC) this;
                        n.getQueuedActions().add(new EastAction());
                    }
                    futureMapposition.move(futureMapposition.x + 1, futureMapposition.y);
                }

                else if (node.x < futureMapposition.x)
                {

                    if (this instanceof Player)
                    {
                        Game.getCurrent().getCommandQueue().addEntry(new WestAction());
                    }
                    else if (this instanceof NPC)
                    {
                        logger.info("add west");
                        NPC n = (NPC) this;
                        n.getQueuedActions().add(new WestAction());
                    }
                    futureMapposition.move(futureMapposition.x - 1, futureMapposition.y);
                }

                else if (node.y > futureMapposition.y)
                {

                    if (this instanceof Player)
                    {
                        Game.getCurrent().getCommandQueue().addEntry(new SouthAction());
                    }
                    else if (this instanceof NPC)
                    {
                        logger.info("add south");
                        NPC n = (NPC) this;
                        n.getQueuedActions().add(new SouthAction());
                    }
                    futureMapposition.move(futureMapposition.x, futureMapposition.y + 1);
                }

                else if (node.y < futureMapposition.y)
                {

                    if (this instanceof Player)
                    {
                        Game.getCurrent().getCommandQueue().addEntry(new NorthAction());
                    }
                    else if (this instanceof NPC)
                    {
                        logger.info("add north");
                        NPC n = (NPC) this;
                        n.getQueuedActions().add(new NorthAction());
                    }
                    futureMapposition.move(futureMapposition.x, futureMapposition.y - 1);
                }
            }
        }
        return true;
    }


    boolean dropItem(AbstractItem affectedItem, MapTile tile)
    {
        tile.getInventory().add(affectedItem);
        return this.dropItem(affectedItem);
    }

    public abstract AbstractRepresentation getAppearance();

    public Hashtable<Weapon, AbstractItem> getHoldEquipment()
    {
        return holdEquipment;
    }

    public void setHoldEquipment(Hashtable<Weapon, AbstractItem> holdEquipment)
    {
        this.holdEquipment = holdEquipment;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public Point getMapPosition()
    {
        return mapPosition;
    }

    public void setMapPosition(Point mapPosition)
    {
        this.mapPosition = mapPosition;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    public Point getUIPosition()
    {
        return UIPosition;
    }

    public void setUIPosition(Point uIPosition)
    {
        UIPosition = uIPosition;
    }

    public Hashtable<ArmorPositions, AbstractItem> getWearEquipment()
    {
        return wearEquipment;
    }

    public void setWearEquipment(Hashtable<ArmorPositions, AbstractItem> wearEquipment)
    {
        this.wearEquipment = wearEquipment;
    }

    public String toString()
    {
        return (getClass().getName() + " Number: " + getNumber() + " Map Position: " + getMapPosition() + " UIPosition: " + getUIPosition());
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

    public boolean wieldWeapon(Weapon weapon)
    {
        if (getInventory().contains(weapon))
        {
            if (getWeapon() == null)
            {
                logger.info("wield weapon");
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
        logger.info("should not be reachable");
        return false;
    }


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
            getWearEquipment().put(pos, null);
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

    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    public boolean getItem(AbstractItem item)
    {
        getInventory().add(item);
        return true;
    }

    /**
     * gets the top item from the maptile
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
                Game.getCurrent().getCurrentMap().getItems().remove(tile.getInventory().get(tile.getInventory().getSize() - 1));
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

        if ((north.x >= 0 && north.y >= 0) && (!(Objects.requireNonNull(MapUtils.getTileByCoordinates(north)).getInventory().isEmpty())))
        {
            getAction = new GetAction(north);
        }
        else if ((east.x >= 0 && east.y >= 0) && (!(Objects.requireNonNull(MapUtils.getTileByCoordinates(east)).getInventory().isEmpty())))
        {
            getAction = new GetAction(east);
        }
        else if ((south.x >= 0 && south.y >= 0) && (!(Objects.requireNonNull(MapUtils.getTileByCoordinates(south)).getInventory().isEmpty())))
        {
            getAction = new GetAction(south);
        }
        else if ((west.x >= 0 && west.y >= 0) && (!(Objects.requireNonNull(MapUtils.getTileByCoordinates(west)).getInventory().isEmpty())))
        {
            getAction = new GetAction(west);
        }
        return getAction;
    }

    public Attributes getAttributes()
    {
        return attributes;
    }

    public void setAttributes(Attributes attributes)
    {
        this.attributes = attributes;
    }


    public void move(int x, int y)
    {
        Objects.requireNonNull(MapUtils.getTileByCoordinates(this.getMapPosition())).setBlocked(false);
        this.getMapPosition().move(x, y);
        Objects.requireNonNull(MapUtils.getTileByCoordinates(this.getMapPosition())).setBlocked(true);
        EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
    }


    public boolean talk(MapTile tile)
    {
        return false;
    }

    public boolean isLightSource()
    {
        return lightSource;
    }

    public void setLightSource(boolean lightSource)
    {
        this.lightSource = lightSource;
    }

    public int getLightRange()
    {
        return lightRange;
    }

    public void setLightRange(int lightRange)
    {
        this.lightRange = lightRange;
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getArmorClass()
    {
        return armorClass;
    }

    public void setArmorClass(int armorClass)
    {
        this.armorClass = armorClass;
    }

    public Weapon getWeapon()
    {
        return weapon;
    }

    public void setWeapon(Weapon weapon)
    {
        this.weapon = weapon;
    }

    public AbstractItem getShield()
    {
        return shield;
    }

    public void setShield(AbstractItem shield)
    {
        this.shield = shield;
    }
}
