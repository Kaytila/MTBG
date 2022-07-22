package net.ck.game.backend.entities;

import net.ck.game.backend.CommandQueue;
import net.ck.game.backend.Game;
import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
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
		MapUtils.getTileByCoordinates(position).setBlocked(true);		
		//logger.info("player number {}, map position {}", getNumber(), mapPosition.toString());
	}

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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

	/**
	 * constructor for the player player has two images types defined now.
	 * standard image is what is used if animation is turned off moving image is
	 * for whatever images is used to catch all images for the animation cycles.
	 * there needs to be an image per animation cycle per player.
	 * 
	 * to check for caller of method:
	 * https://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection
	 * 
	 *
	 * parameters: player number
	 */
	public Player(int number)
	{
		super();
		
		//StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();		
		//logger.info("calling player constructor from: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
		setLightSource(true);
		setLightRange(4);
		setNumber(number);

		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		BufferedImage standardImage = null;
		ArrayList<BufferedImage> movingImages = new ArrayList<BufferedImage>();

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
	public void setAgressive(boolean b)
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
			Game.getCurrent().getCurrentTurn().getActions().add(new PlayerAction(new AbstractKeyboardAction()));
		}
		//Game.getCurrent().getController().setCurrentAction(null);
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
		logger.info("player attacking");
		MapTile	tile = MapUtils.calculateMapTileUnderCursor(action.getTargetCoordinates());


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
				//logger.info("tile: {}", tile);
				if (Game.getCurrent().getCurrentMap().getNpcs().size() > 0)
				{
					for (LifeForm n : Game.getCurrent().getCurrentMap().getLifeForms())
					{
						if (n.getMapPosition().equals(tile.getMapPosition()))
						{
							n.setAgressive(true);
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
				else
				{
					//No NPCs
				}
			}
		}
		return true;
	}

	public CommandQueue getQueuedActions()
	{
		return Game.getCurrent().getCommandQueue();
	}

}
