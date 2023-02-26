package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.queuing.CommandQueue;
import net.ck.game.backend.queuing.Schedule;
import net.ck.game.backend.state.CommandSuccessMachine;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.astar.AStar;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.graphics.PlayerPositionChanged;
import net.ck.util.communication.keyboard.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class Player extends AbstractEntity implements LifeForm
{

    private CommandQueue queuedActions;

    private final Point uiPosition = new Point(MapUtils.getMiddle(), MapUtils.getMiddle());

    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
        Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(position)).setBlocked(true);
        //logger.info("player number {}, map position {}", getNumber(), mapPosition.toString());
    }

    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


    /**
     * constructor for the player player has two images types defined now.
     * standard image is what is used if animation is turned off moving image is
     * for whatever images is used to catch all images for the animation cycles.
     * there needs to be an image per animation cycle per player
     * to check for caller of method:
     * <a href="https://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection">...</a>
     * parameters: player number
     */
    public Player(int number)
    {
        super();
        setLightSource(true);
        setLightRange(4);
        setId(number);

        ArrayList<BufferedImage> images = new ArrayList<>();

        BufferedImage standardImage;
        ArrayList<BufferedImage> movingImages;

        standardImage = ImageUtils.loadStandardPlayerImage(this);
        movingImages = ImageUtils.loadMovingPlayerImages(this);

        images.add(standardImage);
        images.addAll(movingImages);
        setAnimationImageList(images);
        setStandardImage(standardImage);

        setCurrentImage(getAnimationImageList().get(0));

        this.getItem(Game.getCurrent().getArmorList().get(1));
        this.getItem(Game.getCurrent().getArmorList().get(2));
        this.getItem(Game.getCurrent().getArmorList().get(3));
        this.getItem(Game.getCurrent().getArmorList().get(4));
        this.getItem(Game.getCurrent().getArmorList().get(5));
        this.getItem(Game.getCurrent().getArmorList().get(6));
        this.getItem(Game.getCurrent().getArmorList().get(7));

        wearItem(Game.getCurrent().getArmorList().get(1));
        wearItem(Game.getCurrent().getArmorList().get(2));
        wearItem(Game.getCurrent().getArmorList().get(3));
        wearItem(Game.getCurrent().getArmorList().get(4));
        wearItem(Game.getCurrent().getArmorList().get(5));
        wearItem(Game.getCurrent().getArmorList().get(6));
        wearItem(Game.getCurrent().getArmorList().get(7));


        this.getItem(Game.getCurrent().getWeaponList().get(1));
        this.getItem(Game.getCurrent().getWeaponList().get(2));
        this.getItem(Game.getCurrent().getUtilityList().get(1));

        getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
        getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);
        setState(LifeFormState.ALIVE);
    }

    @Override
    public Point getUIPosition() {
        return uiPosition;
        //return new Point(MapUtils.getMiddle(), MapUtils.getMiddle());
    }

    @Override
    public void setHostile(boolean b)
    {
        //nothing to do, this is player
    }

    @Override
    public void setVictim(LifeForm npc)
    {
        //nothing to do, this is player
    }

    @Override
    public LifeForm getVictim()
    {
        return null;
    }

    @Override
    public Point getOriginalMapPosition()
    {
        return null;
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
    public void search()
    {
        for (int xStart = getMapPosition().x - 1; xStart <= getMapPosition().x + 1; xStart++)
        {
            for (int yStart = getMapPosition().y - 1; yStart <= getMapPosition().y + 1; yStart++)
            {
                logger.info("searching maptile: {}", MapUtils.getMapTileByCoordinates(xStart, yStart));
            }
        }
    }

    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {
        if (getQueuedActions() != null)
        {
            if (!(getQueuedActions().isEmpty()))
            {
                logger.info("queued action, lets do this: {}", getQueuedActions().peek());
                AbstractKeyboardAction realAction = (AbstractKeyboardAction) getQueuedActions().poll();
                doAction(new PlayerAction(realAction));
                EventBus.getDefault().post(new AdvanceTurnEvent(realAction.isHaveNPCAction()));


            }
        }
        //TODO make this more elegant ... somehow
        //TODO think about specifying sound effects and music
        //logger.info("do action: {}", action.toString());
        Point p = getMapPosition();
        Point mapsize = Game.getCurrent().getCurrentMap().getSize();

        int xBorder = mapsize.x;
        int yBorder = mapsize.y;

        boolean success = false;
        action.setSuccess(false);
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
                        action.setSuccess(true);
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
                MapTile exit = MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition());
                assert exit != null;
                String mapName = exit.getTargetMap();
                int targetTileID = exit.getTargetID();
                if (mapName != null && targetTileID != -1)
                {
                    logger.info("loading new map");
                    Game.getCurrent().switchMap();
                    success = true;
                    action.setSuccess(true);
                }
                else
                {
                    logger.info("stay on map");
                    success = false;
                }
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
                        action.setSuccess(true);
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
                        action.setSuccess(true);
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
                        action.setSuccess(true);
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
                this.search();
                break;

            case ATTACK:
                success = attack(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;

            case LOOK:
                this.look(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(action.getEvent().getGetWhere())));
                break;
            default:
                logger.info("doing default action, inventory does not need to be reverted for instance");
                break;
        }

        // so if the action was done successful, add the action to the turn
        // if not, create a null action and add this to the turn.
       /* if (success)
        {
            Game.getCurrent().getCurrentTurn().getActions().add((PlayerAction) action);
        }
        else
        {
            Game.getCurrent().getCurrentTurn().getActions().add(new PlayerAction(new AbstractKeyboardAction()));
        }*/

        /*if (successfulMovemement > 0)
        {
            if (GameConfiguration.playSound == true)
            {
                if (successfulMovemement == 2)
                {
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                }
                else
                {
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                }
            }
        }
        else
        {

        }
        */
        CommandSuccessMachine.calculateSoundEffect(action);
        //Game.getCurrent().getController().setCurrentAction(null);
    }

    /**
     * looks at the maptile - will show tile type, furniture, inventory in some way or another.
     *
     * @param maptile the maptile which was selected as target
     */
    private void look(MapTile maptile)
    {
        logger.info("looking:");
        logger.info("maptile: {}", maptile);
        logger.info("maptile furniture: {}", maptile.getFurniture());
        logger.info("maptile inventory: {}", maptile.getInventory());
        for (AbstractItem item : maptile.getInventory().getInventory())
        {
            logger.info("item: {}", item);
        }

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
        return false;
    }

    @Override
    public boolean isHostile()
    {
        return false;
    }

    @Override
    public void evade()
    {
        this.setCurrentImage(ImageUtils.getMissImage());
        EventBus.getDefault().post(new AnimatedRepresentationChanged(this));
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
    public void setSchedule(Schedule schedule)
    {

    }

    @Override
    public Schedule getSchedule()
    {
        return null;
    }

    @Override
    public void setRunningAction(AbstractKeyboardAction action)
    {

    }

    @Override
    public AbstractKeyboardAction getRunningAction()
    {
        return null;
    }



    @Override
    public int getHealth()
    {
        return 0;
    }

    @Override
    public void setHealth(int i)
    {
        health = i;
    }

    @Override
    public void increaseHealth(int i)
    {
        health = health + i;
    }

    @Override
    public void decreaseHealth(int i)
    {
        health = health - i;
    }

    public CommandQueue getQueuedActions()
    {
        return Game.getCurrent().getCommandQueue();
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
        //logger.info("setting weapon: {}", weapon);
        this.weapon = weapon;
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
        //logger.info("should not be reachable");
        return false;
    }

    /**
     * @param x
     * @param y
     */
    public void move(int x, int y)
    {
        super.move(x, y);
        EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
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


    public void setQueuedActions(CommandQueue queuedActions)
    {
        this.queuedActions = queuedActions;
    }

}
