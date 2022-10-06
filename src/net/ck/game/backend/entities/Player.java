package net.ck.game.backend.entities;

import net.ck.game.backend.CommandQueue;
import net.ck.game.backend.Game;
import net.ck.game.backend.GameState;
import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.game.soundeffects.SoundEffects;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends AbstractEntity implements LifeForm
{

    /**
     * appearance is something that both player and npc have, en does not, so separate parent class?
     */
    private AbstractRepresentation appearance;

    private int animationCycles = 0;


    public void setMapPosition(Point position)
    {
        this.mapPosition = position;
        Objects.requireNonNull(MapUtils.getTileByCoordinates(position)).setBlocked(true);
        //logger.info("player number {}, map position {}", getNumber(), mapPosition.toString());
    }

    private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

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
        setNumber(number);

        ArrayList<BufferedImage> images = new ArrayList<>();

        BufferedImage standardImage;
        ArrayList<BufferedImage> movingImages;

        standardImage = ImageUtils.loadStandardPlayerImage(this);
        movingImages = ImageUtils.loadMovingPlayerImages(this);

        images.add(standardImage);
        images.addAll(movingImages);
        setAppearance(new AnimatedRepresentation(standardImage, images));

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
    }

    public int getAnimationCycles()
    {
        return this.animationCycles;
    }

    public void setAnimationCycles(int animationCycles)
    {
        this.animationCycles = animationCycles;
    }

    public AbstractRepresentation getAppearance()
    {
        return appearance;
    }

    public Turn getCurrentTurn()
    {
        return Game.getCurrent().getCurrentTurn();
    }

    public void setAppearance(AbstractRepresentation appearance)
    {
        this.appearance = appearance;
    }

    @Override
    public Point getUIPosition()
    {
        return new Point(MapUtils.getMiddle(), MapUtils.getMiddle());
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
                logger.info("searching maptile: {}", MapUtils.getTileByCoordinates(new Point(xStart, yStart)));
            }
        }
    }

    /**
     * @param action KeyboardAction.type
     */
    public void doAction(AbstractAction action)
    {
        //TODO make this more elegant ... somehow
        //TODO think about specifying sound effects and music
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
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                        success = true;
                    }
                    else
                    {
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                        //logger.info("EAST blocked");
                    }
                }
                else
                {
                    logger.info("eastern border, ignore wrapping for now");
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                }
                break;
            case ENTER:
                MapTile exit = MapUtils.getTileByCoordinates(this.getMapPosition());
                String mapName = exit.getTargetMap();
                int targetTileID = exit.getTargetID();
                if (mapName != null && targetTileID != -1)
                {
                    logger.info("loading new map");
                    Game.getCurrent().switchMap();
                    success = true;
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
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                        success = true;
                    }
                    else
                    {
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                        //logger.info("NORTH blocked");
                    }
                }
                else
                {
                    logger.info("already at zero y");
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
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
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                        success = true;
                    }
                    else
                    {
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                        //logger.info("SOUTH blocked");
                    }

                }
                else
                {
                    logger.info("southern border, ignore wrapping for now");
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                }
                break;
            case WEST:
                // logger.info("p: {}", p.toString());
                if (p.x > 0)
                {
                    if (!(MapUtils.lookAhead((p.x - 1), (p.y))))
                    {
                        this.move((p.x - 1), (p.y));
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                        success = true;
                    }
                    else
                    {
                        Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                        //logger.info("WEST blocked");
                    }
                }
                else
                {
                    logger.info("already at zero x");
                    Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
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

            case LOOK:
                this.look(Objects.requireNonNull(MapUtils.getTileByCoordinates(action.getEvent().getGetWhere())));
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
            Game.getCurrent().getCurrentTurn().getActions().add(new PlayerAction(new AbstractKeyboardAction()));
        }
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

    /*
     * @param action keyboard action (attack action)
     * @return returns whether it is a hit
     * different implementation for player and NPC now.
     *
     *
     */
    public boolean attack(AbstractKeyboardAction action)
    {
        //TODO clean this mess up somewhen not sure I need to have so much duplicate code
        logger.info("player attacking");
        MapTile tile = MapUtils.calculateMapTileUnderCursor(action.getTargetCoordinates());
        EventBus.getDefault().post(new GameStateChanged(GameState.COMBAT));
        //Game.getCurrent().getSoundSystem().restartMusic();

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
                //logger.info("tile: {}", tile)
            }

            if (Game.getCurrent().getCurrentMap().getLifeForms().size() > 0)
            {
                for (LifeForm n : Game.getCurrent().getCurrentMap().getLifeForms())
                {
                    if (n.getMapPosition().equals(tile.getMapPosition()))
                    {
                        n.setHostile(true);
                        n.setVictim(this);
                        this.setVictim(n);
                        if (NPCUtils.calculateHit(this, n))
                        {
                            logger.info("hit");
                            n.decreaseHealth(5);
                            n.getAppearance().setCurrentImage(ImageUtils.getHitImage());
                            EventBus.getDefault().post(new AnimatedRepresentationChanged(n));
                            Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.HIT);
                        }
                        else
                        {
                            logger.info("miss");
                            Game.getCurrent().getSoundPlayerNoThread().playSoundEffect(SoundEffects.ATTACK);
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

}
