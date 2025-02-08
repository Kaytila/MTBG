package net.ck.mtbg.backend.entities.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.queuing.CommandQueue;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.state.CommandSuccessMachine;
import net.ck.mtbg.backend.state.EnvironmentalStoryTeller;
import net.ck.mtbg.backend.state.ItemManager;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.mainframes.SceneFrame;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.astar.AStar;
import net.ck.mtbg.util.communication.graphics.AdvanceTurnEvent;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.mtbg.util.communication.graphics.PlayerPositionChanged;
import net.ck.mtbg.util.communication.keyboard.*;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

@Log4j2
@Getter
@Setter
@ToString
public class Player extends AbstractEntity implements LifeForm
{

    /**
     * for player the UI Position is always centered
     */
    private final Point uiPosition = new Point(MapUtils.getMiddle(), MapUtils.getMiddle());
    /**
     * spell level selected in the spellbook - needs to be stored somewhere, makes most sense at player
     * is updated by flipping through the spell book
     */
    private int selectedSpellLevel = 1;

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
        setQueuedActions(new CommandQueue());


        setType(NPCType.PLAYER);


        this.getItem(ItemManager.getArmorList().get(1));
        this.getItem(ItemManager.getArmorList().get(2));
        this.getItem(ItemManager.getArmorList().get(3));
        this.getItem(ItemManager.getArmorList().get(4));
        this.getItem(ItemManager.getArmorList().get(5));
        this.getItem(ItemManager.getArmorList().get(6));
        this.getItem(ItemManager.getArmorList().get(7));

        wearItem(ItemManager.getArmorList().get(1));
        wearItem(ItemManager.getArmorList().get(2));
        wearItem(ItemManager.getArmorList().get(3));
        wearItem(ItemManager.getArmorList().get(4));
        wearItem(ItemManager.getArmorList().get(5));
        wearItem(ItemManager.getArmorList().get(6));
        wearItem(ItemManager.getArmorList().get(7));


        this.getItem(ItemManager.getWeaponList().get(1));
        this.getItem(ItemManager.getWeaponList().get(2));
        this.getItem(ItemManager.getUtilityList().get(1));

        getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
        getAttributes().get(AttributeTypes.DEXTERITY).setValue(20);
        getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
        getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);

        AbstractSpell spell1 = new AbstractSpell();
        spell1.setName("Fireball");
        spell1.setId(1);
        spell1.setLevel(1);

        AbstractSpell spell2 = new AbstractSpell();
        spell2.setName("Heal");
        spell2.setId(2);
        spell2.setLevel(2);


        AbstractSpell spell8 = new AbstractSpell();
        spell8.setName("Resurrection");
        spell8.setId(3);
        spell8.setLevel(3);

        getSpells().add(spell1);
        getSpells().add(spell2);
//        getSpells().add(spell3);
//        getSpells().add(spell4);
//        getSpells().add(spell5);
//        getSpells().add(spell6);
//        getSpells().add(spell7);
        getSpells().add(spell8);

        AbstractSkill skill1 = new AbstractSkill();
        skill1.setName("Bash");
        skill1.setLevel(1);
        skill1.setId(1);

        AbstractSkill skill2 = new AbstractSkill();
        skill2.setName("Block");
        skill2.setLevel(1);
        skill2.setId(2);

        AbstractSkill skill3 = new AbstractSkill();
        skill2.setName("Kick");
        skill2.setLevel(2);
        skill2.setId(3);

        getSkills().add(skill1);
        getSkills().add(skill2);
        getSkills().add(skill3);
    }


    @Override
    public Point getUIPosition()
    {
        return uiPosition;
        //return new Point(MapUtils.getMiddle(), MapUtils.getMiddle());
    }

    @Override
    public Point getOriginalMapPosition()
    {
        return null;
    }

    public NPCType getType()
    {
        return NPCType.PLAYER;
    }

    @Override
    public AbstractKeyboardAction lookForExit()
    {
        return null;
    }


    /**
     * search is handled today by just searching around the player.
     * But is this a  good way?
     */
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
                AbstractKeyboardAction realAction = getQueuedActions().poll();
                PlayerAction playerAction = new PlayerAction(realAction);
                doAction(playerAction);
                if (GameConfiguration.debugEvents == true)
                {
                    logger.debug("fire advance turn event");
                }
                EventBus.getDefault().post(new AdvanceTurnEvent(playerAction));
                return;
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
            case PUSH:
                this.push(action.getEvent().getGetWhere());
                break;
            case YANK:
                this.yank(action.getEvent().getGetWhere());
                break;

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
                    switchMap();
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
                this.talk((MapUtils.getMapTileByCoordinates(action.getEvent().getGetWhere().x, action.getEvent().getGetWhere().y).getLifeForm()));
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
            case SPELLBOOK:
                this.castSpell(action);
                break;
            case CAST:
                this.castSpell(action);
                break;
            case OPEN:
            {
                this.openDoor(action);
                break;
            }

            case JIMMY:
            {
                this.unlockDoor(action);
                break;
            }

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

    private void unlockDoor(AbstractAction action)
    {
        logger.debug("UNLOCK DOOR");
        //TODO properly this is just simple
        MapTile tile = action.getEvent().getMapTile();

        if (tile.isOpenable())
        {
            if (tile.isLocked())
            {
                logger.debug("unlocked");
                tile.setLock(null);
            }
            else
            {
                logger.debug("already unlocked");
            }
        }
        else
        {
            logger.debug("nothing openable!");
        }

    }

    private void openDoor(AbstractAction action)
    {
        logger.info("open door");
        MapTile tile = action.getEvent().getMapTile();

        if (tile.isLocked())
        {
            logger.debug("locked!");
            return;
        }

        if (tile.getType().equals(TileTypes.GATECLOSED))
        {

            logger.info("opening gate");
            tile.setType(TileTypes.GATEOPEN);

        }

        else if (tile.getType().equals(TileTypes.WOODDOORCLOSED))
        {
            logger.info("opening wooden door");
            tile.setType(TileTypes.WOODDOOROPEN);
        }

        else if (tile.getType().equals(TileTypes.STONEDOORCLOSED))
        {
            logger.info("opening stone door");
            tile.setType(TileTypes.STONEDOOROPEN);
        }

        else if (tile.getType().equals(TileTypes.GATEOPEN))
        {
            logger.info("closing gate");
            tile.setType(TileTypes.GATECLOSED);
        }

        else if (tile.getType().equals(TileTypes.WOODDOOROPEN))
        {
            logger.info("closing wooden door");
            tile.setType(TileTypes.WOODDOORCLOSED);
        }

        else if (tile.getType().equals(TileTypes.STONEDOOROPEN))
        {
            logger.info("closing stone door");
            tile.setType(TileTypes.STONEDOORCLOSED);
        }
        else
        {
            logger.error("No door or gate here!");
        }

    }

    private void talk(LifeForm lifeForm)
    {
        if (lifeForm.getMobasks() != null)
        {
            if (lifeForm.getMobasks().size() > 0)
            {
                logger.info("npc {} has something to say", lifeForm);
            }
        }
        else
        {
            logger.info("nothing");
        }
    }

    public String talk(LifeForm lifeForm, String question)
    {
        if (lifeForm.getMobasks() != null)
        {
            if (lifeForm.getMobasks().size() > 0)
            {
                //logger.info("npc {} has something to say", lifeForm);
                if (lifeForm.getMobasks().get(question) != null)
                {
                    logger.info("lifeform {} has something to say about {}", lifeForm, question);
                    //TODO this is icky as fuck but works for trying out
                    if (question.equalsIgnoreCase("hh"))
                    {
                        logger.info("play cutscene");
                        UIStateMachine.setUiState(UIState.CUTSCENE);
                        if (EventQueue.isDispatchThread())
                        {
                            SceneFrame frame = new SceneFrame();
                            //not necessary but I dont like the frame to be greyed out due to not being used for this is heavy TBD.
                            frame.setVisible(true);
                        }
                    }
                    else
                    {
                        return lifeForm.getMobasks().get(question);
                    }
                }
                else
                {
                    logger.info("lifeform {} has nothing to say about {}", lifeForm, question);
                    return null;
                }
            }
            else
            {
                logger.info("initialized Mobasks but nothing to say");
            }
        }
        else
        {
            logger.info("nothing");
        }
        return null;
    }

    /**
     * looks at the maptile - will show tile type, furniture, inventory in some way or another.
     *
     * @param maptile the maptile which was selected as target
     */
    public void look(MapTile maptile)
    {
        logger.info("looking:");
        logger.info("maptile: {}", maptile);
        logger.info("maptile furniture: {}", maptile.getFurniture());
        logger.info("maptile inventory: {}", maptile.getInventory());
        logger.info("maptile blocked: {}", maptile.isBlocked());

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

        if (!(maptile.isLocked()))
        {
            maptile.setLock(0);
        }
    }

    @Override
    public void say(String message)
    {

    }

    @Override
    public boolean isPlayer()
    {
        return true;
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
    public void decreaseHealth(int i)
    {
        this.setCurrImage(ImageManager.getActionImage(ActionStates.HIT));
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire new life form animation");
        }
        EventBus.getDefault().post(new AnimatedRepresentationChanged(this));
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
    public void setHostile(boolean b)
    {
        logger.error(() -> "dont, its player");
    }

    @Override
    public Point getOriginalTargetMapPosition()
    {
        return null;
    }

    @Override
    public void setOriginalTargetMapPosition(Point targetMapPosition)
    {
        logger.error(() -> "dont, its player");
    }

    @Override
    public Point getTargetMapPosition()
    {
        return null;
    }

    @Override
    public void setTargetMapPosition(Point targetMapPosition)
    {
        logger.error(() -> "dont, its player");
    }

    @Override
    public boolean isPatrolling()
    {
        return false;
    }

    @Override
    public void setPatrolling(boolean patrolling)
    {
        logger.error(() -> "dont, its player");
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
        logger.error(() -> "dont, its player");
    }

    @Override
    public AbstractKeyboardAction getRunningAction()
    {
        return null;
    }

    @Override
    public void setRunningAction(AbstractKeyboardAction action)
    {
        logger.error(() -> "dont, its player");
    }


    public boolean wieldWeapon(Weapon weapon)
    {
        if (getInventory().contains(weapon))
        {
            if (getWeapon() == null)
            {
                logger.debug("wield weapon");
                setWeapon(weapon);
                getInventory().remove(weapon);
                return true;
            }
            else
            {
                logger.debug("weapon: {}", getWeapon());
                logger.debug("cannot wield weapon");
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
        if (GameConfiguration.debugPC == true)
        {
            logger.debug("move to x: {} y: {}", x, y);
        }
        EnvironmentalStoryTeller.tellStoryLeave(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()));
        super.move(x, y);
        EnvironmentalStoryTeller.tellStoryEnter(MapUtils.getMapTileByCoordinatesAsPoint(this.getMapPosition()));
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire player position change");
        }
        EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
    }

    public boolean moveTo(MapTile tileByCoordinates)
    {
        setQueuedActions(new CommandQueue());
        if (GameConfiguration.debugPC == true)
        {
            logger.debug("start: {}", MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()));
            logger.debug("finish: {}", tileByCoordinates);
        }
        AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getMapTileByCoordinatesAsPoint(getMapPosition()), tileByCoordinates, Game.getCurrent().getCurrentMap());
        ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();
        Point futureMapPosition = new Point(getMapPosition().x, getMapPosition().y);
        for (MapTile node : path)
        {
            if (node.getMapPosition().equals(getMapPosition()))
            {
                if (GameConfiguration.debugPC == true)
                {
                    logger.debug("start node");
                }
            }
            else
            {
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
                if (GameConfiguration.debugPC == true)
                {
                    logger.debug("target can be reached");
                }
            }
        }
        return false;
    }

    public boolean switchMap()
    {
        if (GameConfiguration.debugPC == true)
        {
            logger.debug("switching map");
        }
        Map oldMap = Game.getCurrent().getCurrentMap();
        super.switchMap();
        //TODO this one is kind of ugly - can this be handled otherwise?
        Game.getCurrent().finishMapSwitch(oldMap);
        return true;
    }

    public boolean decreaseLevel()
    {
        if (getSelectedSpellLevel() > 1)
        {
            setSelectedSpellLevel(getSelectedSpellLevel() - 1);
            return true;
        }
        return false;
    }

    public boolean increaseLevel()
    {
        if (getSelectedSpellLevel() < 8)
        {
            setSelectedSpellLevel(getSelectedSpellLevel() + 1);
            return true;
        }
        return false;
    }


    @Override
    /**
     * player is not represented in a map, but in game instead.
     * hmmmm, perhaps this should go into map?
     */
    public String toXML()
    {
        return "";
    }
}
