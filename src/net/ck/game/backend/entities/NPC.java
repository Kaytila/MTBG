package net.ck.game.backend.entities;

import net.ck.game.backend.CommandQueue;
import net.ck.game.backend.Game;
import net.ck.game.backend.GameState;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.MoveAction;
import net.ck.util.communication.sound.GameStateChanged;
import net.ck.util.communication.time.GameTimeChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class NPC extends AbstractEntity implements LifeForm
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    private AbstractRepresentation appearance;

    private NPCTypes type;
    private Hashtable<String, String> mobasks;
    private boolean hostile;
    private boolean isStatic;
    private ArrayList<NPCSchedule> npcSchedules;
    private CommandQueue queuedActions;
    private LifeForm victim;
    private boolean ranged;
    /**
     * original position on the map - remember the placement that the npc does not wander off too much
     */
    private Point originalMapPosition;

    public NPC(Integer i, Point p)
    {
        logger.info("initialize properly");
        setStatic(false);
        setOriginalMapPosition(new Point(p.x, p.y));
        NPC master = Game.getCurrent().getNpcList().get(i);
        setMapPosition(new Point(p.x, p.y));
        setType(master.getType());
        setMobasks(master.getMobasks());
        setNpcSchedules(new ArrayList<>());
        setQueuedActions(new CommandQueue());
        EventBus.getDefault().register(this);
        getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        setHealth(Game.getCurrent().getBaseHealth() + (getLevel() * 10));
        setArmorClass(0);
        getInventory().add(Game.getCurrent().getWeaponList().get(3));
        wieldWeapon(Game.getCurrent().getWeaponList().get(1));
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

    public Point getOriginalMapPosition()
    {
        return originalMapPosition;
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

    public void setOriginalMapPosition(Point originalMapPosition)
    {
        this.originalMapPosition = originalMapPosition;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    @Override
    public String toString()
    {
        return "NPC [type=" + type + ", mapposition=" + mapPosition + ", mobasks=" + (mobasks != null ? toString(mobasks.entrySet()) : null) + "]";
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

    @Override
    public void setWeapon(Weapon weapon)
    {
        logger.info("weapon for npc: {}", weapon);
        this.weapon = weapon;
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
        ArrayList<BufferedImage> images = new ArrayList<>();

        BufferedImage standardImage;
        ArrayList<BufferedImage> movingImages;

        standardImage = ImageUtils.loadStandardPlayerImage(this);
        movingImages = ImageUtils.loadMovingPlayerImages(this);

        images.add(standardImage);
        images.addAll(movingImages);
        setAppearance(new AnimatedRepresentation(standardImage, images));
    }

    public Point getMapPosition()
    {
        return mapPosition;
    }

    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
    }

    @Override
    public AbstractRepresentation getAppearance()
    {
        return appearance;
    }

    public void setAppearance(AbstractRepresentation appearance)
    {
        this.appearance = appearance;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public NPCTypes getType()
    {
        return type;
    }

    public void setType(NPCTypes type)
    {
        this.type = type;
    }

    public Hashtable<String, String> getMobasks()
    {
        return mobasks;
    }

    public void setMobasks(Hashtable<String, String> mobasks)
    {
        this.mobasks = mobasks;
    }

    public ArrayList<NPCSchedule> getNpcSchedules()
    {
        return npcSchedules;
    }

    public void setNpcSchedules(ArrayList<NPCSchedule> npcSchedules)
    {
        this.npcSchedules = npcSchedules;
    }

    public CommandQueue getQueuedActions()
    {
        return queuedActions;
    }

    public void setQueuedActions(CommandQueue queuedActions)
    {
        this.queuedActions = queuedActions;
    }

    /**
     * @param event GameTime has changed, check if there is a defined schedule for the npc
     */
    @Subscribe
    public void onMessageEvent(GameTimeChanged event)
    {
        this.checkSchedules(event);
    }

    /**
     * @param event Game Time has changed
     */
    private void checkSchedules(GameTimeChanged event)
    {
        if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 10)
        {
           // logger.info("check schedule");
            if (getMobasks().size() > 0)
            {
                //logger.info("running");
                MoveAction action = new MoveAction();
                action.setGetWhere(new Point(1, 0));
                doAction(new PlayerAction(action));
            }
        }

        if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 30)
        {
            //logger.info("check schedule");
            if (getMobasks().size() > 0)
            {
                //logger.info("running");
                MoveAction action = new MoveAction();
                action.setGetWhere(getOriginalMapPosition());
                doAction(new PlayerAction(action));
            }
        }
    }


    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {
        if (getState() == LifeFormState.DEAD)
        {
            logger.info("npc dead");
            return;
        }
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
                search();
                break;

            case ATTACK:
                success = attack(action.getEvent());
                break;
            default:
                logger.info("doing default action, inventory does not need to be reverted for instance");
                break;

        }
    }

    /**
     * @param action keyboard action (attack action)
     * @return returns whether it is a hit
     * <p>
     */

    public boolean attack(AbstractKeyboardAction action)
    {
        if (getState() == LifeFormState.DEAD)
        {
            logger.info("NPC Dead");
            return false;
        }
        logger.info("NPC Attacking");
        MapTile tile = MapUtils.getTileByCoordinates(getVictim().getMapPosition());
        EventBus.getDefault().post(new GameStateChanged(GameState.COMBAT));
        action = NPCUtils.calculateCoordinatesFromActionAndTile(action, tile, this);

        if (tile != null)
        {
            logger.info("we found the file, its the players: {}", tile);
            if (getWeapon() == null)
            {
                setWeapon(Game.getCurrent().getWeaponList().get(1));
            }

            if (getWeapon().getType().equals(WeaponTypes.RANGED))
            {
                logger.info("here at ranged attack");
                Missile m = new Missile(action.getSourceCoordinates(), action.getTargetCoordinates());
                Game.getCurrent().getCurrentMap().getMissiles().add(m);

                if (Game.getCurrent().getCurrentMap().getLifeForms().size() > 0)
                {
                    for (LifeForm n : Game.getCurrent().getCurrentMap().getLifeForms())
                    {
                        if (n.getMapPosition().equals(tile.getMapPosition()))
                        {
                            logger.info("hitting player: {}", n);
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
                //No NPCs
            }
            else
            {
                logger.info("meleee");
                if (Game.getCurrent().getCurrentMap().getLifeForms().size() > 0)
                {
                    for (LifeForm n : Game.getCurrent().getCurrentMap().getLifeForms())
                    {
                        if (n.getMapPosition().equals(tile.getMapPosition()))
                        {
                            logger.info("hitting player: {}", n);
                            n.setHostile(true);
                            n.setVictim(this);
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

            }
        }
        else
        {
            logger.info("here, tile is null");
        }
        return true;
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
            //TODO set image to skeleton or pool of blood or anything really
        }
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
                return false;
            }
        }
        return false;
    }


    public void setRanged(boolean ranged)
    {
        this.ranged = ranged;
    }

    /**
     * @param ranged - switch to this weapon type
     */
    public void switchWeapon(WeaponTypes ranged)
    {
        logger.info("switching weapon");
        this.setWeapon(null);
        for (int i = 0; i <= getInventory().getSize(); i++)
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

    private void attack(MapTile tileByCoordinates)
    {
        Game.getCurrent().getCurrentMap().getMissiles().add(new Missile(MapUtils.getTileByCoordinates(getMapPosition()), tileByCoordinates));
    }

}
