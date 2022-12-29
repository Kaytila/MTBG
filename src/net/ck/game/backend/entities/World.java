package net.ck.game.backend.entities;

import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.RandomAction;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.game.Turn;
import net.ck.game.backend.queuing.CommandQueue;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.CodeUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Random;

/**
 * environment is the counterpart to player, the AI so to speak
 * 
 * @author Claus
 *
 */
public class World extends AbstractEntity implements LifeForm
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public World()
	{
		super();
	}

	public RandomAction createRandomEvent()
	{
		RandomAction event = new RandomAction();
		Random rand = new Random();
		int result = rand.nextInt(100);

		switch (result)
		{
			case 0 :
				event.setTitle("BINGO test" + result);

			case 1:
				event.setTitle("Bla " + result);
			default :
				event.setTitle("test " + result);
		}
		getCurrentTurn().getEvents().add(event);
		// logger.info("random event: " + event.getTitle());
		event.setEntity(this);
		return event;
	}

	public Turn getCurrentTurn()
	{
		return Game.getCurrent().getCurrentTurn();
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
	public boolean attack(AbstractKeyboardAction action)
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
	public AnimatedRepresentation getAppearance()
	{
		logger.error("world does not have a graphical appearance");
		return null;
	}

	@Override
	public int getNumber()
	{
		logger.error("world does not have a number");
		return -1;
	}

	@Override
	public void setNumber(int i)
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

	}
}
