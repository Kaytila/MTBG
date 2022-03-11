package net.ck.game.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.game.map.MapTile;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.UILense;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.util.communication.graphics.BackgroundRepresentationChanged;
import net.ck.util.communication.graphics.CursorChangeEvent;
import net.ck.util.communication.graphics.ForegroundRepresentationChanged;
import net.ck.util.communication.keyboard.DropAction;
import net.ck.util.communication.keyboard.EQAction;
import net.ck.util.communication.keyboard.ESCAction;
import net.ck.util.communication.keyboard.EastAction;
import net.ck.util.communication.keyboard.EnterAction;
import net.ck.util.communication.keyboard.GetAction;
import net.ck.util.communication.keyboard.InventoryAction;
import net.ck.util.communication.keyboard.NorthAction;
import net.ck.util.communication.keyboard.SouthAction;
import net.ck.util.communication.keyboard.SpaceAction;
import net.ck.util.communication.keyboard.TalkAction;
import net.ck.util.communication.keyboard.WestAction;
import net.ck.util.communication.keyboard.ZStatsAction;

public class JGridCanvas extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final String newline = System.getProperty("line.separator");

	private int numberOfTiles = Game.getCurrent().getNumberOfTiles();

	private Range<Integer> rangeX = Range.between(0, numberOfTiles - 1);
	private Range<Integer> rangeY = Range.between(0, numberOfTiles - 1);

	private BufferedImage blackImage = ImageUtils.createImage((Color.black));

	private int currentBackgroundImage;
	private int currentForegroundImage;

	private int tileSize = Game.getCurrent().getTileSize();

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private boolean dragEnabled;

	public Logger getLogger()
	{
		return logger;
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

	public JGridCanvas()
	{
		EventBus.getDefault().register(this);

		this.setBounds(0, 0, tileSize * numberOfTiles, tileSize * numberOfTiles);
		// add plain black border for showing difference
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(blackline);
		this.setBackground(Color.green);
		this.setOpaque(false);
		this.setVisible(true);
		this.setFocusable(true);
		this.requestFocus();
		this.setKeyboardInput();
		this.setToolTipText(getLogger().getName());
		
		this.setTransferHandler(new JGridCanvasTransferHandler(this));
	}

	/**
	 * convenience method to encapsulate KeyboardInput Map and Action Map definition https://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed
	 */
	public void setKeyboardInput()
	{
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "south");
		this.getActionMap().put("south", new SouthAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "west");
		this.getActionMap().put("west", new WestAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "east");
		this.getActionMap().put("east", new EastAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "north");
		this.getActionMap().put("north", new NorthAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
		this.getActionMap().put("enter", new EnterAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		this.getActionMap().put("escape", new ESCAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
		this.getActionMap().put("space", new SpaceAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "inventory");
		this.getActionMap().put("inventory", new InventoryAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "z-stats");
		this.getActionMap().put("z-stats", new ZStatsAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "wear/wield");
		this.getActionMap().put("wear/wield", new EQAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "get");
		this.getActionMap().put("get", new GetAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "drop");
		this.getActionMap().put("drop", new DropAction());

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "talk");
		this.getActionMap().put("talk", new TalkAction());
	}

	public void paintComponent(Graphics g)
	{
		// logger.info("======================================================================");
		// start by clearing the rectangle completely, lets see how flashy that
		// is - not at all - yet
		emptySlate(g);

		// identify which tiles are visible at first!

		identifyVisibleTiles(g);

		// here comes the LoS Part

		// this somehow needs to be doable faster
		// draw the background images
		paintBackground(g);

		// identify the black tiles
		paintBlackTiles(g);

		// iterate over the entities on the map
		// calculate offset to the player.
		paintNPCs(g);

		// here come the items on the map :D
		paintItems(g);

		// here comes the paintWeather part
		// this somehow needs to be doable faster
		paintWeather(g);

		// ring of darkness around the player
		// paintDarkness(g);

		// take component size and draw lines every $tileSize pixels.
		paintGridLines(g);

	}

	private void paintGridLines(Graphics g)
	{
		int rows = this.getHeight() / tileSize;
		int cols = this.getWidth() / tileSize;
		int i = 0;
		for (i = 0; i < rows; i++)
		{
			g.drawLine(0, i * tileSize, this.getWidth(), i * tileSize);
		}

		for (i = 0; i < cols; i++)
		{
			g.drawLine(i * tileSize, 0, i * tileSize, this.getHeight());
		}
	}

	private void paintWeather(Graphics g)
	{
		if (Game.getCurrent().getCurrentMap().getCurrentWeather().getType() == WeatherTypes.SUN)
		{
			// brighten up the world more perhaps?
		}
		else if ((Game.getCurrent().getCurrentMap().getCurrentWeather().getType() == WeatherTypes.RAIN) || (Game.getCurrent().getCurrentMap().getCurrentWeather().getType() == WeatherTypes.HAIL)
			|| (Game.getCurrent().getCurrentMap().getCurrentWeather().getType() == WeatherTypes.SNOW))
		{
			for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
			{
				Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
				if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
				{
					// BufferedImage img = ImageUtils.loadRandomBackgroundImage(tile.getType());
					BufferedImage img = ImageUtils.getWeatherTypeImages().get(Game.getCurrent().getCurrentMap().getCurrentWeather().getType()).get(getCurrentForegroundImage());
					// logger.info("buffered image: {}", img.toString());
					g.drawImage(img, (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
				}
			}

		}

	}

	/**
	 * clears th visible rectangle and also clears the lense
	 * 
	 * @param g
	 */
	private void emptySlate(Graphics g)
	{
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		UILense.getCurrent().initialize();
	}

	@Subscribe
	/**
	 * 
	 * @param event
	 *            an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
	 */
	public void onMessageEvent(AnimatedRepresentationChanged event)
	{
		this.repaint();
	}

	@Subscribe
	/**
	 * 
	 * @param event
	 *            an animatedRepresentation has changed, repaint the canvas
	 *
	 */
	public void onMessageEvent(BackgroundRepresentationChanged event)
	{
		setCurrentBackgroundImage(event.getCurrentNumber());
		// logger.info("current background number: {}", getCurrentBackgroundImage());
		// this.repaint();
	}

	@Subscribe
	/**
	 * 
	 * @param event
	 *            an animatedRepresentation has changed, repaint the canvas
	 *
	 */
	public void onMessageEvent(ForegroundRepresentationChanged event)
	{
		setCurrentForegroundImage(event.getCurrentNumber());
		// logger.info("current Foreground number: {}", event.getCurrentNumber());
		// this.repaint();
	}

	@Subscribe
	/**
	 * 
	 * @param event
	 *            an animatedRepresentation has changed, repaint the canvas
	 *
	 */
	public void onMessageEvent(CursorChangeEvent event)
	{
		setCursor(event.getCursor());
	}

	public int getCurrentBackgroundImage()
	{
		return currentBackgroundImage;
	}

	public void setCurrentBackgroundImage(int currentBackgroundImage)
	{
		this.currentBackgroundImage = currentBackgroundImage;
	}

	public int getCurrentForegroundImage()
	{
		return currentForegroundImage;
	}

	public void setCurrentForegroundImage(int currentForegroundImage)
	{
		this.currentForegroundImage = currentForegroundImage;
	}

	@SuppressWarnings("unused")
	private void paintDarkness(Graphics g)
	{
		// identify the black tiles

		int frameTop = Game.getCurrent().getCurrentPlayer().getUIPosition().y - Game.getCurrent().getCurrentMap().getVisibilityRange();
		int frameBottom = Game.getCurrent().getCurrentPlayer().getUIPosition().y + Game.getCurrent().getCurrentMap().getVisibilityRange();
		int frameLeft = Game.getCurrent().getCurrentPlayer().getUIPosition().x - Game.getCurrent().getCurrentMap().getVisibilityRange();
		int frameRight = Game.getCurrent().getCurrentPlayer().getUIPosition().x + Game.getCurrent().getCurrentMap().getVisibilityRange();

		for (Point uiTile : UILense.getCurrent().getEntries())
		{ // logger.info("point p: {}", uiTile.toString());
			if (uiTile.y < frameTop)
			{
				g.drawImage(blackImage, (uiTile.x * tileSize), (uiTile.y * tileSize), this);
			}
			if (uiTile.y > frameBottom)
			{
				g.drawImage(blackImage, (uiTile.x * tileSize), (uiTile.y * tileSize), this);
			}

			if (uiTile.x < frameLeft)
			{
				g.drawImage(blackImage, (uiTile.x * tileSize), (uiTile.y * tileSize), this);
			}
			if (uiTile.x > frameRight)
			{
				g.drawImage(blackImage, (uiTile.x * tileSize), (uiTile.y * tileSize), this);
			}
		}
	}

	private void identifyVisibleTiles(Graphics g)
	{
		for (MapTile tile : Game.getCurrent().getCurrentMap().getTiles())
		{
			Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
			// these are the visible tiles
			if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
			{
				UILense.getCurrent().add(screenPosition);
				UILense.getCurrent().getVisibleMapTiles().add(tile);
			}
		}
	}

	/**
	 * this method paints the background, i.e. the tiles of the maptiles. I should keep screenposition seperately, but the visible tiles number is small enough as not to matter much
	 * 
	 * @param g
	 */
	private void paintBackground(Graphics g)
	{
		int pX = Game.getCurrent().getCurrentPlayer().getUIPosition().x;
		int pY = Game.getCurrent().getCurrentPlayer().getUIPosition().y;

		for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
		{
			Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
			// these are the visible tiles
			if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
			{
				// BufferedImage img = ImageUtils.loadRandomBackgroundImage(tile.getType());
				BufferedImage img = ImageUtils.getTileTypeImages().get(tile.getType()).get(getCurrentBackgroundImage());
				if (img == null)
				{
					logger.error("tile has no image: {}", tile);
				}
				// logger.info("buffered image: {}", img.toString());
				int absX = Math.abs(pX - screenPosition.x);
				int absY = Math.abs(pY - screenPosition.y);
				img = ImageUtils.brightenUpImage(img, absX, absY);
				g.drawImage(img, (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
			}
		}
	}

	/**
	 * so these is the method paints the black tiles at the border where the map ends
	 * 
	 * @param g
	 */
	private void paintBlackTiles(Graphics g)
	{
		for (Point emptyUITile : UILense.getCurrent().identifyEmptyCoordinates())
		{
			// logger.info("point p: {}", p.toString());
			g.drawImage(blackImage, (emptyUITile.x * tileSize), (emptyUITile.y * tileSize), this);
		}
	}

	private void paintNPCs(Graphics g)
	{
		for (AbstractEntity entity : Game.getCurrent().getAnimatedEntities())
		{

			entity.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(entity.getMapPosition()));

			int x = entity.getUIPosition().x;
			int y = entity.getUIPosition().y;

			// small optimization, dont draw at all when negative, double buffering should ignore that but who knows
			if (rangeX.contains(x) && rangeY.contains(y))
			{
				if (tileSize == Game.getCurrent().getImageSize().x & (tileSize == Game.getCurrent().getImageSize().y))
				{
					g.drawImage(entity.getAppearance().getCurrentImage(), tileSize * x, tileSize * y, this);
				}
				else
				{
					if (tileSize / Game.getCurrent().getImageSize().x == 2)
					{
						g.drawImage(entity.getAppearance().getCurrentImage(), ((tileSize * x) + (tileSize / 4)), ((tileSize * y) + (tileSize / 4)), this);
					}
				}
			}
		}
	}

	private void paintItems(Graphics g)
	{
		for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
		{
			if (tile.getInventory().isEmpty())
			{

			}
			else
			{
				Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
				g.drawImage(tile.getInventory().get(0).getItemImage(), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
			}
		}
	}

    public void setDragEnabled (boolean dragEnabled) {
        if (this.dragEnabled != dragEnabled) {
            this.dragEnabled = dragEnabled;
            firePropertyChange("dragEnabled", !dragEnabled, dragEnabled);
        }
    }
	
    
    public boolean isDragEnabled () {
        return dragEnabled;
    }
    
}