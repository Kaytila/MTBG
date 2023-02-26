package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.actions.RandomAction;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.queuing.CommandQueue;
import net.ck.game.backend.queuing.Schedule;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.CodeUtils;
import net.ck.util.MapUtils;
import net.ck.util.UILense;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

/**
 * environment is the counterpart to player, the AI so to speak
 *
 * @author Claus
 */
public class World extends AbstractEntity implements LifeForm
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private boolean spawned = false;

	public World()
	{
		super();
	}

	public RandomAction createRandomEvent(PlayerAction action)
	{
		RandomAction event = new RandomAction();
		Random rand = new Random();
		int result = rand.nextInt(1111113);

		switch (result)
		{
			case 0:
				event.setTitle("BINGO test" + result);
				break;
			case 1:
				event.setTitle("Bla " + result);
				break;
			default :
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
	public void setWeapon(Weapon weapon)
	{

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
	public void setHostile(boolean b)
	{

	}

	@Override
	public void setVictim(LifeForm npc)
	{

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
				spawnNPC();
				break;
			default:
				//logger.info("nothing");
				break;
		}
	}

	private void spawnNPC()
	{

		if (spawned == true)
		{
			return;
		}

		NPC n1 = new NPC();
		n1.setType(NPCTypes.WARRIOR);
		//identify which direction player is moving that we can spawn the npc:
		MapTile tile = null;
		switch (Game.getCurrent().getPlayerAction().getEvent().getType())
		{
			case NORTH:
				tile = UILense.getCurrent().mapTiles[0][MapUtils.getMiddle()];
				break;
			case EAST:
				tile = UILense.getCurrent().mapTiles[MapUtils.getMiddle()][GameConfiguration.numberOfTiles - 1];
				break;
			case SOUTH:
				tile = UILense.getCurrent().mapTiles[GameConfiguration.numberOfTiles - 1][MapUtils.getMiddle()];
				break;
			case WEST:
				tile = UILense.getCurrent().mapTiles[MapUtils.getMiddle()][0];
				break;
			default:
				logger.info("do nothing");
				break;
		}
		if (tile != null)
		{
			n1.setMapPosition(new Point(tile.getMapPosition().x, tile.getMapPosition().y));
			n1.initialize();
			n1.setId(1000);
			n1.setHostile(true);
			n1.setVictim(Game.getCurrent().getCurrentPlayer());
			Game.getCurrent().getCurrentMap().getLifeForms().add(n1);
			tile.setLifeForm(n1);
			n1.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(n1.getMapPosition()));
			spawned = true;
			logger.info("spawned NPC");
		}
	}
}
