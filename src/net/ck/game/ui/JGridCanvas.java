package net.ck.game.ui;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.Missile;
import net.ck.game.backend.entities.NPC;
import net.ck.game.items.FurnitureItem;
import net.ck.game.map.MapTile;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.UILense;
import net.ck.util.communication.graphics.*;
import net.ck.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class JGridCanvas extends JComponent
{

    private static final long serialVersionUID = 1L;
    private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
    private final int numberOfTiles = Game.getCurrent().getNumberOfTiles();
    private final Range<Integer> rangeX = Range.between(0, numberOfTiles - 1);
    private final Range<Integer> rangeY = Range.between(0, numberOfTiles - 1);
    private final BufferedImage blackImage = ImageUtils.createImage((Color.black));
    private final int tileSize = Game.getCurrent().getTileSize();
    private int currentBackgroundImage;
    private int currentForegroundImage;
    private boolean dragEnabled;

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
        this.requestFocusInWindow();
        this.setKeyboardInput();
        //this.setToolTipText(getLogger().getName());

        this.setTransferHandler(new JGridCanvasTransferHandler(this));
    }

    public Logger getLogger()
    {
        return logger;
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

    /**
     * convenience method to encapsulate KeyboardInput Map and Action Map definition
     * https://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed
     */
    private void setKeyboardInput()
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

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "move");
        this.getActionMap().put("move", new MoveAction());

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "attack");
        this.getActionMap().put("attack", new AttackAction());

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "search");
        this.getActionMap().put("search", new SearchAction());
    }

    public void paintComponent(Graphics g)
    {
        // logger.info("======================================================================");
        // start by clearing the rectangle completely, lets see how flashy that
        // is - not at all - yet
        emptySlate(g);

        // identify which tiles are visible at first!
        identifyVisibleTiles();

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
        paintDarkness(g);

        //paint furniture
        //also: paint light effects
        paintFurniture(g);

        //paint line of sight or rather, paint black the tiles that are not visible
        //based on the calculations which are not completely fitting but good enough
        paintLoS(g);

        //paint the missiles on the screen
        //paintMissiles(g);
        paintMissilesFullLineAtOnce(g);

        // take component size and draw lines every $tileSize pixels.
        paintGridLines(g);
    }

    private void paintFurniture(Graphics g)
    {
        for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
        {
            if (tile.getFurniture() != null)
            {
                //logger.info("furniture");
                FurnitureItem item = tile.getFurniture();
                Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                BufferedImage img = item.getItemImage();
                if (img == null)
                {
                    logger.error("tile has no image: {}", tile);
                }

                if (item.isLightSource())
                {
                    //identify map tiles within range.
                    int lightrange = item.getLightRange();
                    ArrayList<MapTile> tiles = MapUtils.calculateVisibleTiles(tile, lightrange, Game.getCurrent().getCurrentMap());
                    for (MapTile t : tiles)
                    {
                        screenPosition = MapUtils.calculateUIPositionFromMapOffset(t.getMapPosition());
                        if (t.equals(tile))
                        {
                            g.drawImage(ImageUtils.brightenUpImage(ImageUtils.getTileTypeImages().get(tile.getType()).get(getCurrentBackgroundImage()), 1, 1), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                            img = ImageUtils.brightenUpImage(img, 1, 1);
                            g.drawImage(ImageUtils.brightenUpImage(img, 1, 1), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                        }
                        else
                        {
                            //background
                            g.drawImage(ImageUtils.brightenUpImage(ImageUtils.getTileTypeImages().get(t.getType()).get(getCurrentBackgroundImage()), 1, 1), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);

                            if (t.getFurniture() != null)
                            {
                                g.drawImage(ImageUtils.brightenUpImage(t.getFurniture().getItemImage(), 1, 1), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                            }
                            if (!(t.getInventory().isEmpty()))
                            {
                                g.drawImage(ImageUtils.brightenUpImage(t.getInventory().get(0).getItemImage(), 1, 1), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                            }
                            NPC n = null;
                            boolean filled = false;
                            for (NPC npc : Game.getCurrent().getCurrentMap().getNpcs())
                            {
                                if (t.getMapPosition().equals(npc.getMapPosition()))
                                {
                                    filled = true;
                                    n = npc;
                                    break;
                                }
                            }
                            //there is a npc on the tile
                            if (filled)
                            {
                                g.drawImage(ImageUtils.brightenUpImage(n.getAppearance().getCurrentImage(), 1, 1), ((screenPosition.x * tileSize) + (tileSize / 4)), ((screenPosition.y * tileSize) + (tileSize / 4)), this);
                            }

                            //PC !!!
                            if (t.getMapPosition().equals(Game.getCurrent().getCurrentPlayer().getMapPosition()))
                            {
                                g.drawImage(ImageUtils.brightenUpImage(Game.getCurrent().getCurrentPlayer().getAppearance().getCurrentImage(), 1, 1), ((screenPosition.x * tileSize) + (tileSize / 4)), ((screenPosition.y * tileSize) + (tileSize / 4)), this);
                            }
                        }
                    }
                }
                else
                {
                    //logger.info("no light source");
                    g.drawImage(tile.getFurniture().getItemImage(), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                }
                //TODO think about whether to add weather images to the lightened areas away from player
                //img = ImageUtils.getWeatherTypeImages().get(Game.getCurrent().getCurrentMap().getCurrentWeather().getType()).get(getCurrentForegroundImage());
                //logger.info("buffered image: {}", img.toString());
                g.drawImage(img, (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
            }
        }
    }

    private void paintGridLines(Graphics g)
    {
        int rows = this.getHeight() / tileSize;
        int cols = this.getWidth() / tileSize;
        int i;
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * tileSize, this.getWidth(), i * tileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * tileSize, 0, i * tileSize, this.getHeight());
        }
    }

    private void paintMissilesTileBased(Graphics g)
    {
        for (Missile m : Game.getCurrent().getCurrentMap().getMissiles())
        {
            if (m.getCurrentPosition() == null)
            {
                m.setCurrentPosition(MapUtils.calculateUIPositionFromMapOffset(m.getSourceTile().getMapPosition()));
            }
            int x = m.getCurrentPosition().x;
            int y = m.getCurrentPosition().y;

            ArrayList<Point> line = MapUtils.getLine(m.getCurrentPosition(), MapUtils.calculateUIPositionFromMapOffset(m.getTargetTile().getMapPosition()));
            for (Point p : line)
            {
                logger.info("p:{}", p);
                g.drawImage(m.getAppearance().getStandardImage(), ((tileSize * p.x) + (tileSize / 2)), ((tileSize * p.y) + (tileSize / 2)), this);
                m.setCurrentPosition(p);
            }
        }
    }


    private void paintMissiles(Graphics g)
    {
        if ((Game.getCurrent().getCurrentMap().getMissiles() == null) || (Game.getCurrent().getCurrentMap().getMissiles().size() == 0))
        {
            Game.getCurrent().getMissileTimer().stop();
            return;
        }

        //logger.info(" missiles: {}", Game.getCurrent().getCurrentMap().getMissiles());
        ArrayList<Missile> finishedMissiles = new ArrayList<>();

        for (Missile m : Game.getCurrent().getCurrentMap().getMissiles())
        {
            if (m.getSourceCoordinates() == null)
            {
                logger.error("missile has no source");
                Game.getCurrent().stopGame();
            }
            //logger.info("m: {}", m);
            //logger.info("m image: {}", m.getAppearance().getStandardImage());
            if (m.getLine() == null)
            {
                if (m.getCurrentPosition() == null)
                {
                    m.setCurrentPosition(new Point(m.getSourceCoordinates().x, m.getSourceCoordinates().y));
                }
                m.setLine(MapUtils.getLine(m.getCurrentPosition(), m.getTargetCoordinates()));
            }

            if (m.getLine().size() == 0)
            {
                if (m.isSuccess())
                {
                    m.getAppearance().setStandardImage(ImageUtils.loadImage("combat", "explosion"));
                }
                //logger.info("finished missile");
                m.setFinished(true);
                finishedMissiles.add(m);
            }
            else
            {
                Point p = m.getLine().get(0);
                m.setCurrentPosition(p);

                if (m.getCurrentPosition().equals(m.getTargetCoordinates()))
                {
                    if (m.isSuccess())
                    {
                        m.getAppearance().setStandardImage(ImageUtils.loadImage("combat", "explosion"));
                    }
                    m.setFinished(true);
                    finishedMissiles.add(m);
                }

                //only paint missile every 5 pixels
                for (int i = 0; i <= 5; i++)
                {
                    if (m.getLine().size() > 0)
                    {
                        m.getLine().remove(0);
                    }
                }
                g.drawImage(m.getAppearance().getStandardImage(), p.x, p.y, this);
            }
        }

        if (finishedMissiles.size() > 0)
        {
            //logger.info("finished missiles: {}", finishedMissiles);
            Game.getCurrent().getCurrentMap().getMissiles().removeAll(finishedMissiles);
        }
    }

    private void paintMissilesFullLineAtOnce(Graphics g)
    {
        ArrayList<Missile> finishedMissiles = new ArrayList<>();
        for (Missile m : Game.getCurrent().getCurrentMap().getMissiles())
        {
            if (m.getCurrentPosition() == null)
            {
                m.setCurrentPosition(new Point(m.getSourceCoordinates().x, m.getSourceCoordinates().y));
            }

            ArrayList<Point> line = MapUtils.getLine(m.getCurrentPosition(), m.getTargetCoordinates());
            for (Point p : line)
            {
                //logger.info("p:{}", p );
                g.drawImage(m.getAppearance().getStandardImage(), p.x, p.y, this);
                m.setCurrentPosition(p);
                if (m.getCurrentPosition().equals(m.getTargetCoordinates()))
                {
                    m.setFinished(true);
                    finishedMissiles.add(m);
                }
            }
        }
        Game.getCurrent().getCurrentMap().getMissiles().removeAll(finishedMissiles);
        //Game.getCurrent().getMissileTimer().stop();
    }


    private void paintWeather(Graphics g)
    {
        if (Game.getCurrent().getCurrentMap().getWeather().getType() == WeatherTypes.SUN)
        {
            //logger.info("the weather is shining");
        }
        else if ((Game.getCurrent().getCurrentMap().getWeather().getType() == WeatherTypes.RAIN) || (Game.getCurrent().getCurrentMap().getWeather().getType() == WeatherTypes.HAIL)
                || (Game.getCurrent().getCurrentMap().getWeather().getType() == WeatherTypes.SNOW))
        {
            for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
            {
                Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
                {
                    // BufferedImage img = ImageUtils.loadRandomBackgroundImage(tile.getType());
                    BufferedImage img = ImageUtils.getWeatherTypeImages().get(Game.getCurrent().getCurrentMap().getWeather().getType()).get(getCurrentForegroundImage());
                    // logger.info("buffered image: {}", img.toString());
                    g.drawImage(img, (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
                }
            }
        }
    }

    /**
     * clears th visible rectangle and also clears the lense
     *
     * @param g - graphics context
     */
    private void emptySlate(Graphics g)
    {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        UILense.getCurrent().initialize();
    }

    /**
     * @param event an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
     */
    @Subscribe
    public void onMessageEvent(PlayerPositionChanged event)
    {
        //logger.info("player position changed, lets see whether this is also called for NPCs");
        this.repaint();
    }



    /**
     * @param event an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
     */
    @Subscribe
    public void onMessageEvent(AnimatedRepresentationChanged event)
    {
        this.repaint();
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
     */
    @Subscribe
    public void onMessageEvent(MissilePositionChanged event)
    {
        //logger.info("catching message");
        this.repaint();
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas
     */
    @Subscribe
    public void onMessageEvent(BackgroundRepresentationChanged event)
    {
        setCurrentBackgroundImage(event.getCurrentNumber());
        // logger.info("current background number: {}", getCurrentBackgroundImage());
        // this.repaint();
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas
     */
    @Subscribe
    public void onMessageEvent(ForegroundRepresentationChanged event)
    {
        setCurrentForegroundImage(event.getCurrentNumber());
        // logger.info("current Foreground number: {}", event.getCurrentNumber());
        // this.repaint();
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas
     */
    @Subscribe
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

    /**
     * identify which tiles of the map are currently visible
     * also set back hidden state cause this is calculated again
     *
     */
    private void identifyVisibleTiles()
    {
        for (MapTile tile : Game.getCurrent().getCurrentMap().getTiles())
        {
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
            // these are the visible tiles
            if (rangeX.contains(screenPosition.x) && rangeY.contains(screenPosition.y))
            {
                UILense.getCurrent().add(screenPosition);
                UILense.getCurrent().getVisibleMapTiles().add(tile);
                tile.setHidden(false);
            }
        }
    }

    /**
     * this method paints the background, i.e. the tiles of the maptiles. I should keep screenposition seperately, but the visible tiles number is small enough as not to matter much
     *
     * @param g - Graphics
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
     * so these are the method paints the black tiles at the border where the map ends
     *
     * @param g - graphics g
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
        for (LifeForm entity : Game.getCurrent().getAnimatedEntities())
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
            if (tile.getInventory().isEmpty() == false)
            {
                Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                g.drawImage(tile.getInventory().get(0).getItemImage(), (screenPosition.x * tileSize), (screenPosition.y * tileSize), this);
            }
        }
    }

    public boolean isDragEnabled()
    {
        return dragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled)
    {
        if (this.dragEnabled != dragEnabled)
        {
            this.dragEnabled = dragEnabled;
            firePropertyChange("dragEnabled", !dragEnabled, dragEnabled);
        }
    }

    /**
     * TODO this is where I will paint the borders
     * @param g graphics object
     */
    public void paintBorder(Graphics g)
    {
        super.paintBorder(g);
    }

    /**
     * use the Bresenhaim algorithm to calculate LoS
     * should investigate https://www.redblobgames.com/articles/visibility/
     * @param g standard graphics context
     */
    private void paintLoS(Graphics g)
    {
        for (Point point : UILense.getCurrent().getEntries())
        {
            //logger.info("point: {}", point);
            boolean blocked = false;

            ArrayList<Point> line = MapUtils.getLine(Game.getCurrent().getCurrentPlayer().getUIPosition(), point);

            Point offSet = MapUtils.calculateUIOffsetFromMapPoint();


            for (Point p : line)
            {
                //logger.info("calculated route: {}", p);
               MapTile t = MapUtils.getTileByCoordinates(new Point(p.x - offSet.x, p.y - offSet.y));
               //logger.info("maptile: {}", t);
               if (t == null)
               {
                   continue;
               }

               if (t.isBlocksLOS())
               {
                   blocked = true;
                    continue;
               }
               if (blocked)
               {
                   t.setHidden(true);
                   g.drawImage(blackImage, (p.x * tileSize), (p.y * tileSize), this);
               }
            }
        }
    }

}