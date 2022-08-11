package net.ck.game.backend.entities;

import net.ck.game.backend.CommandQueue;
import net.ck.game.backend.Game;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Armor;
import net.ck.game.items.ArmorPositions;
import net.ck.game.items.Weapon;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;
import net.ck.util.astar.AStar;
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


    protected Weapon weapon;

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
    protected int armorClass;

    /**
     * state of the PC/NPC - alive or dead or any other really.
     */
    protected LifeFormState state;

    public AbstractEntity()
    {
        inventory = new Inventory();
        attributes = new Attributes();
        setLevel(1);
    }



    /**


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

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }



    public AbstractItem getShield()
    {
        return shield;
    }

    public void setShield(AbstractItem shield)
    {
        this.shield = shield;
    }

    /**
     *
     * @param tileByCoordinates target tile
     */
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
                    getQueuedActions().addEntry(new EastAction());
                    futureMapposition.move(futureMapposition.x + 1, futureMapposition.y);
                }

                else if (node.x < futureMapposition.x)
                {
                    getQueuedActions().addEntry(new WestAction());
                    futureMapposition.move(futureMapposition.x - 1, futureMapposition.y);
                }

                else if (node.y > futureMapposition.y)
                {
                    getQueuedActions().addEntry(new SouthAction());
                    futureMapposition.move(futureMapposition.x, futureMapposition.y + 1);
                }

                else if (node.y < futureMapposition.y)
                {
                    getQueuedActions().addEntry(new NorthAction());
                    futureMapposition.move(futureMapposition.x, futureMapposition.y - 1);
                }
            }
            if (node.getMapPosition().equals(tileByCoordinates.getMapPosition()))
            {
                logger.info("target can be reached");
                return true;
            }
        }
        return false;
    }

    protected abstract CommandQueue getQueuedActions();
}
