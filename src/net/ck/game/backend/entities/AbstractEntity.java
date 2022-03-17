package net.ck.game.backend.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Armor;
import net.ck.game.items.ArmorPositions;
import net.ck.game.items.Weapon;
import net.ck.game.map.MapTile;
import net.ck.util.MapUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.GetAction;
import net.ck.util.communication.keyboard.KeyboardActionType;

import javax.swing.*;

/**
 * AbstractEntity is the abstract parent class known subclasses are: Player NPC World
 * 
 * @author Claus
 *
 */
public abstract class AbstractEntity
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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
	private Hashtable<Weapon, AbstractItem> holdEquipment = new Hashtable<Weapon, AbstractItem>();

	/**
	 * armortypes contains all the armor positions, so this is the way to go.
	 */
	private Hashtable<ArmorPositions, AbstractItem> wearEquipment = new Hashtable<ArmorPositions, AbstractItem>();

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


	public AbstractEntity()
	{
		inventory = new Inventory();
		attributes = new Attributes();
	}

	/**
	 * 
	 * @param action
	 *            KeyboardAction.type
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
			case EAST :
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
			case ENTER :
				logger.info("loading new map");
				Game.getCurrent().switchMap();
				break;
			case ESC :
				break;
			case NORTH :
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
			case NULL :
				break;
			case SOUTH :
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
			case WEST :
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
			case SPACE :
				success = true;
				break;
			case GET :
				success = this.getItem(MapUtils.getTileByCoordinates(action.getEvent().getGetWhere()));
				break;
				
			case DROP:
				success = this.dropItem(action.getEvent().getAffectedItem(), MapUtils.getTileByCoordinates(action.getEvent().getGetWhere()));
				break;
			
			case TALK:
				logger.info("doing talk action");
				break;
			default :
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

	private boolean dropItem(AbstractItem affectedItem, MapTile tile)
	{		
		tile.getInventory().add(affectedItem);
		return this.dropItem(affectedItem);
	}

	public abstract AbstractRepresentation getAppearance();

	public Hashtable<Weapon, AbstractItem> getHoldEquipment()
	{
		return holdEquipment;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public Point getMapPosition()
	{
		return mapPosition;
	}

	public int getNumber()
	{
		return number;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public Point getUIPosition()
	{
		return UIPosition;
	}

	public Hashtable<ArmorPositions, AbstractItem> getWearEquipment()
	{
		return wearEquipment;
	}

	// public abstract Point getPosition();

	// public abstract void setPosition(Point p);

	public void setHoldEquipment(Hashtable<Weapon, AbstractItem> holdEquipment)
	{
		this.holdEquipment = holdEquipment;
	}

	public void setMapPosition(Point mapPosition)
	{
		this.mapPosition = mapPosition;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public void setUIPosition(Point uIPosition)
	{
		UIPosition = uIPosition;
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
			if (getHoldEquipment().isEmpty())
			{
				logger.info("wield weapon");
				getHoldEquipment().put(weapon, weapon);
				getInventory().remove(weapon);
				return true;
			}
			else
			{
				logger.info("hold equipment: {}", getHoldEquipment());
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
	 * @param tile
	 * @return
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

		if ((north.x >= 0 && north.y >= 0) && (!(MapUtils.getTileByCoordinates(north).getInventory().isEmpty())))
		{
			getAction = new GetAction(north);
		}
		else if ((east.x >= 0 && east.y >= 0) && (!(MapUtils.getTileByCoordinates(east).getInventory().isEmpty())))
		{
			getAction = new GetAction(east);
		}
		else if ((south.x >= 0 && south.y >= 0) && (!(MapUtils.getTileByCoordinates(south).getInventory().isEmpty())))
		{
			getAction = new GetAction(south);
		}
		else if ((west.x >= 0 && west.y >= 0) && (!(MapUtils.getTileByCoordinates(west).getInventory().isEmpty())))
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
	
	
	public void move (int x, int y)
	{
		MapUtils.getTileByCoordinates(this.getMapPosition()).setBlocked(false);
		this.getMapPosition().move(x, y);
		MapUtils.getTileByCoordinates(this.getMapPosition()).setBlocked(true);
	}


	public boolean talk (MapTile tile)
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
}
