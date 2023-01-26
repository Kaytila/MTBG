package net.ck.game.ui.components;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.Missile;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.game.Game;
import net.ck.game.items.FurnitureItem;
import net.ck.game.map.MapTile;
import net.ck.game.ui.dnd.JGridCanvasTransferHandler;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;
import net.ck.util.UILense;
import net.ck.util.communication.graphics.*;
import net.ck.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class JGridCanvas extends JComponent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private final Range<Integer> rangeX = Range.between(0, GameConfiguration.numberOfTiles - 1);
    private final Range<Integer> rangeY = Range.between(0, GameConfiguration.numberOfTiles - 1);
    private final BufferedImage blackImage = ImageUtils.createImage((Color.black));
    private int currentBackgroundImage;
    private int currentForegroundImage;
    private boolean dragEnabled;

    private Point highlightPosition;


    private int highlightCount;

    public JGridCanvas()
    {
        EventBus.getDefault().register(this);

        this.setBounds(0, 0, GameConfiguration.tileSize * GameConfiguration.numberOfTiles, GameConfiguration.tileSize * GameConfiguration.numberOfTiles);
        // add plain black border for showing difference
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);
        this.setBackground(Color.green);
        this.setOpaque(false);
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setKeyboardInput();
        setHighlightCount(0);
        //this.setToolTipText(getLogger().getName());

        this.setTransferHandler(new JGridCanvasTransferHandler(this));
    }

    /**
     * convenience method to encapsulate KeyboardInput Map and Action Map definition
     * <a href="https://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed">https://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed</a>
     * <p>
     * Also, for using ALT/CTRL/SHIFT:
     * <a href="https://stackoverflow.com/questions/2419608/java-swing-keystrokes-how-to-make-ctrl-modifier-work">https://stackoverflow.com/questions/2419608/java-swing-keystrokes-how-to-make-ctrl-modifier-work</a>
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

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "look");
        this.getActionMap().put("look", new LookAction());

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK), "options");
        this.getActionMap().put("options", new OptionsAction());
    }

    /**
     * currently everything is drawn after each other and on top of each other
     * so we go through the rows several times
     * I will definitely try out this:
     * https://stackoverflow.com/questions/2318020/merging-two-images
     * to make sure we only draw once - but this means combinging on the fly
     * will need to do a compare between old and new with some profiling
     * i.e. printf timestamp consideration
     *
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g)
    {
        logger.debug("start: painting");


        if (GameConfiguration.drawTileOnce == true)
        {

        }
        else
        {
            // identify which tiles are visible at first!
            // long startTime = System.nanoTime();
            //identifyVisibleTiles();
            //logger.info("OLD run time: {}", System.nanoTime() - startTime);
            //UILense.getCurrent().initialize();
            //startTime = System.nanoTime();
            identifyVisibleTilesNew();
            //logger.info("NEW run time: {}", System.nanoTime() - startTime);
            // this somehow needs to be doable faster
            // draw the background images
            /*paintBackground(g);
            paintBlackTiles(g);
            */
            paintBackgroundNew(g);
            // identify the black tiles


            // iterate over the entities on the map
            // calculate offset to the player.
            //paintNPCs(g);


            // here come the items on the map :D
            paintItems(g);


            paintNPCsNew(g);


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
            paintMissiles(g);
            //paintMissilesFullLineAtOnce(g);

            // take component size and draw lines every $tileSize pixels.
            paintGridLines(g);

            paintHighlighting(g);
        }
        logger.debug("end painting");
    }

    private void paintHighlighting(Graphics g)
    {
        if (getHighlightPosition() != null)
        {
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(getHighlightPosition());
            g.setColor(Color.YELLOW);

            if (getHighlightPosition() == Game.getCurrent().getCurrentPlayer().getMapPosition())
            {
                g.drawRect((screenPosition.x * GameConfiguration.tileSize) + getHighlightCount(), (screenPosition.y * GameConfiguration.tileSize) + getHighlightCount(), GameConfiguration.tileSize - (getHighlightCount() * 2), GameConfiguration.tileSize - (getHighlightCount() * 2));
                //logger.info("highlight count: {}", getHighlightCount());
            }
            else
            {
                g.drawRect((screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), GameConfiguration.tileSize, GameConfiguration.tileSize);
            }
        }
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
                            g.drawImage(ImageUtils.brightenUpImage(ImageUtils.getTileTypeImages().get(tile.getType()).get(getCurrentBackgroundImage()), 1, 1), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                            img = ImageUtils.brightenUpImage(img, 1, 1);
                            g.drawImage(ImageUtils.brightenUpImage(img, 1, 1), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                            //logger.info("drawing here: x: {}  y: {}", (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize));
                        }
                        else
                        {
                            //background
                            g.drawImage(ImageUtils.brightenUpImage(ImageUtils.getTileTypeImages().get(t.getType()).get(getCurrentBackgroundImage()), 1, 1), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);

                            /*if (t.getFurniture() != null)
                            {
                                logger.info("Drawing there2");
                                g.drawImage(ImageUtils.brightenUpImage(t.getFurniture().getItemImage(), 1, 1), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                            }*/
                            if (!(t.getInventory().isEmpty()))
                            {
                                g.drawImage(ImageUtils.brightenUpImage(t.getInventory().get(0).getItemImage(), 1, 1), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
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
                                g.drawImage(ImageUtils.brightenUpImage(n.getAppearance().getCurrentImage(), 1, 1), ((screenPosition.x * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 4)), ((screenPosition.y * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 4)), this);
                            }

                            //PC !!!
                            if (t.getMapPosition().equals(Game.getCurrent().getCurrentPlayer().getMapPosition()))
                            {
                                g.drawImage(ImageUtils.brightenUpImage(Game.getCurrent().getCurrentPlayer().getAppearance().getCurrentImage(), 1, 1), ((screenPosition.x * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 4)), ((screenPosition.y * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 4)), this);
                            }
                        }
                    }
                }
                else
                {
                    //logger.info("no light source");
                    g.drawImage(tile.getFurniture().getItemImage(), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                }
                //TODO think about whether to add weather images to the lightened areas away from player
                //img = ImageUtils.getWeatherTypeImages().get(Game.getCurrent().getCurrentMap().getCurrentWeather().getType()).get(getCurrentForegroundImage());
                //logger.info("buffered image: {}", img.toString());
                //g.drawImage(img, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
            }
        }
    }

    private void paintGridLines(Graphics g)
    {
        int rows = this.getHeight() / GameConfiguration.tileSize;
        int cols = this.getWidth() / GameConfiguration.tileSize;
        int i;
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * GameConfiguration.tileSize, this.getWidth(), i * GameConfiguration.tileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * GameConfiguration.tileSize, 0, i * GameConfiguration.tileSize, this.getHeight());
        }
    }

    /**
     * tile based paints a missile based on the tiles it is crossing,
     * this is the cheap implementation but looks weird as a missile is only
     * drawn once each tile
     *
     * @param g graphics context
     */
    @SuppressWarnings("unused")
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
                g.drawImage(m.getAppearance().getStandardImage(), ((GameConfiguration.tileSize * p.x) + (GameConfiguration.tileSize / 2)), ((GameConfiguration.tileSize * p.y) + (GameConfiguration.tileSize / 2)), this);
                m.setCurrentPosition(p);
            }
        }
    }


    private void paintMissiles(Graphics g)
    {
        if ((Game.getCurrent().getCurrentMap().getMissiles() == null) || (Game.getCurrent().getCurrentMap().getMissiles().size() == 0))
        {
            return;
        }
        Missile m = Game.getCurrent().getCurrentMap().getMissiles().get(0);
        g.drawImage(m.getAppearance().getStandardImage(), m.getCurrentPosition().x, m.getCurrentPosition().y, this);
        //logger.info(" missiles: {}", Game.getCurrent().getCurrentMap().getMissiles());

    }

    /**
     * this method paints a missile in one full line going from source to target
     *
     * @param g graphics context
     */
    @SuppressWarnings("unused")
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
                    g.drawImage(img, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
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
    public synchronized void onMessageEvent(PlayerPositionChanged event)
    {
        //logger.info("player position changed, lets see whether this is also called for NPCs");
        //this.paint();
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
     */
    @Subscribe
    public synchronized void onMessageEvent(AnimatedRepresentationChanged event)
    {
        if (GameConfiguration.useEvents)
        {
            javax.swing.SwingUtilities.invokeLater(() ->
            {
                this.paint();
            });
        }
    }

    @Subscribe
    public synchronized void onMessageEvent(HighlightEvent event)
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            setHighlightPosition(event.getMapPosition());
        });

        if (GameConfiguration.useEvents)
        {
            javax.swing.SwingUtilities.invokeLater(() ->
            {
                this.paint();
            });
        }
    }


    public void paint()
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            this.repaint();
        });
    }

    /**
     * @param event an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
     */
    @Subscribe
    public synchronized void onMessageEvent(MissilePositionChanged event)
    {
        this.paint();
    }


    /**
     * @param event an background image has changed, repaint the canvas
     */
    @Subscribe
    public synchronized void onMessageEvent(BackgroundRepresentationChanged event)
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            setCurrentBackgroundImage(event.getCurrentNumber());/*logger.info(SwingUtilities.isEventDispatchThread());*/
        });
    }


    /**
     * @param event an foreground image has changed, repaint the canvas
     */
    @Subscribe
    public synchronized void onMessageEvent(ForegroundRepresentationChanged event)
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            setCurrentForegroundImage(event.getCurrentNumber());/*logger.info(SwingUtilities.isEventDispatchThread());*/
        });
    }


    /**
     * @param event an animatedRepresentation has changed, repaint the canvas
     */
    @Subscribe
    public synchronized void onMessageEvent(CursorChangeEvent event)
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            setCursor(event.getCursor());
        });
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
                g.drawImage(blackImage, (uiTile.x * GameConfiguration.tileSize), (uiTile.y * GameConfiguration.tileSize), this);
            }
            if (uiTile.y > frameBottom)
            {
                g.drawImage(blackImage, (uiTile.x * GameConfiguration.tileSize), (uiTile.y * GameConfiguration.tileSize), this);
            }

            if (uiTile.x < frameLeft)
            {
                g.drawImage(blackImage, (uiTile.x * GameConfiguration.tileSize), (uiTile.y * GameConfiguration.tileSize), this);
            }
            if (uiTile.x > frameRight)
            {
                g.drawImage(blackImage, (uiTile.x * GameConfiguration.tileSize), (uiTile.y * GameConfiguration.tileSize), this);
            }
        }
    }

    /**
     * identify which tiles of the map are currently visible
     * also set back hidden state cause this is calculated again
     * Currently doing this every paint
     * question is do I need to?
     * Think i only need to do it once something changes.
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

    private void identifyVisibleTilesNew()
    {
        for (Point p : MapUtils.getVisibleTilesAroundPlayer())
        {
            if ((p.x >= 0 && p.y >= 0) && (p.x < Game.getCurrent().getCurrentMap().getSize().x && p.y < Game.getCurrent().getCurrentMap().getSize().y)) {
                MapTile tile = Game.getCurrent().getCurrentMap().mapTiles[p.x][p.y];
                if (tile != null) {
                    Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                    // these are the visible tiles
                    UILense.getCurrent().add(screenPosition);
                    UILense.getCurrent().getVisibleMapTiles().add(tile);
                    tile.setHidden(false);
                }
            }
        }
    }


    /**
     * step two in creating a better draw system
     * there is actually still room for improvement - visible tiles around player cannot change during paint they can only change during
     * advance turn - but this is so fast now that it does not matter due to the better calculation and the better access on tile level
     * take player map position, calculate all visible map positions around.
     * calculate UI screen position, draw either black image, or draw tile image.
     *
     * @param g graphics
     */
    private void paintBackgroundNew(Graphics g)
    {
        int pX = Game.getCurrent().getCurrentPlayer().getUIPosition().x;
        int pY = Game.getCurrent().getCurrentPlayer().getUIPosition().y;
        for (Point p : MapUtils.getVisibleTilesAroundPlayer())
        {
            Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(p);
            if ((p.x < 0) || (p.y < 0)) {

                g.drawImage(blackImage, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
            } else if ((p.x >= Game.getCurrent().getCurrentMap().getSize().x) || (p.y >= Game.getCurrent().getCurrentMap().getSize().y)) {
                g.drawImage(blackImage, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
            } else {
                if (p.x >= (Game.getCurrent().getCurrentMap().getSize().x)) {
                    g.drawImage(blackImage, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                } else if (p.y >= (Game.getCurrent().getCurrentMap().getSize().y)) {
                    g.drawImage(blackImage, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                } else {
                    //logger.debug("point p here: {}", p);
                    MapTile tile = Game.getCurrent().getCurrentMap().mapTiles[p.x][p.y];
                    /**
                     * this is only necessary if a map does not have fully filled out tiles. like testmap :(
                     */
                    if (tile == null) {
                        g.drawImage(blackImage, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                    } else {
                        BufferedImage img = ImageUtils.getTileTypeImages().get(tile.getType()).get(getCurrentBackgroundImage());
                        if (img == null) {
                            logger.error("tile has no image: {}", tile);
                        }
                        // logger.info("buffered image: {}", img.toString());
                        int absX = Math.abs(pX - screenPosition.x);
                        int absY = Math.abs(pY - screenPosition.y);
                        img = ImageUtils.brightenUpImage(img, absX, absY);
                        g.drawImage(img, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
                    }
                }
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
                g.drawImage(img, (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
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
        int n = 0;
        for (Point emptyUITile : UILense.getCurrent().identifyEmptyCoordinates())
        {
            //logger.info("point p: {}", emptyUITile.toString());
            g.drawImage(blackImage, (emptyUITile.x * GameConfiguration.tileSize), (emptyUITile.y * GameConfiguration.tileSize), this);
            g.drawString(String.valueOf(n), (emptyUITile.x * GameConfiguration.tileSize), (emptyUITile.y * GameConfiguration.tileSize));
            n++;
        }
    }

    private void paintNPCs(Graphics g)
    {
        logger.debug("start: paintNPC");
        for (LifeForm entity : Game.getCurrent().getCurrentMap().getLifeForms())
        {
            if (entity.getUIPosition() == null)
            {
                //  logger.debug("need to calculate UI position, something went wrong");
                entity.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(entity.getMapPosition()));
            }
            int x = entity.getUIPosition().x;
            int y = entity.getUIPosition().y;

            // small optimization, do not draw at all when negative, double buffering should ignore that but who knows
            if (rangeX.contains(x) && rangeY.contains(y))
            {
                if (GameConfiguration.tileSize == GameConfiguration.imageSize.x & (GameConfiguration.tileSize == GameConfiguration.imageSize.y))
                {
                    g.drawImage(entity.getAppearance().getCurrentImage(), GameConfiguration.tileSize * x, GameConfiguration.tileSize * y, this);
                }
                else
                {
                    if (GameConfiguration.tileSize / GameConfiguration.imageSize.x == 2)
                    {
                        g.drawImage(entity.getAppearance().getCurrentImage(), ((GameConfiguration.tileSize * x) + (GameConfiguration.tileSize / 4)), ((GameConfiguration.tileSize * y) + (GameConfiguration.tileSize / 4)), this);
                    }
                }
            }
        }
        logger.debug("end: paintNPC");
    }

    private void paintNPCsNew(Graphics g)
    {
        //logger.debug("start: paintNPCNew");
        Rectangle rect = MapUtils.getVisibleRectAroundPlayer();
        for (LifeForm entity : Game.getCurrent().getCurrentMap().getLifeForms())
        {
            if (entity.getUIPosition() == null)
            {
                logger.error("ATTENTION: need to calculate UI position, something went wrong");
                entity.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(entity.getMapPosition()));
            }

            if (rect.contains(entity.getMapPosition()))
            {
                int x = entity.getUIPosition().x;
                int y = entity.getUIPosition().y;
                if (GameConfiguration.tileSize == GameConfiguration.imageSize.x & (GameConfiguration.tileSize == GameConfiguration.imageSize.y))
                {
                    g.drawImage(entity.getAppearance().getCurrentImage(), GameConfiguration.tileSize * x, GameConfiguration.tileSize * y, this);
                }
                else
                {
                    if (GameConfiguration.tileSize / GameConfiguration.imageSize.x == 2)
                    {
                        g.drawImage(entity.getAppearance().getCurrentImage(), ((GameConfiguration.tileSize * x) + (GameConfiguration.tileSize / 4)), ((GameConfiguration.tileSize * y) + (GameConfiguration.tileSize / 4)), this);
                    }
                }
            }
        }
        //logger.debug("end: paintNPCNew");
    }


    private void paintItems(Graphics g)
    {
        for (MapTile tile : UILense.getCurrent().getVisibleMapTiles())
        {
            if ((tile.getInventory().isEmpty() == false) && (tile.getInventory().get(0) != null))
            {
                Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                g.drawImage(tile.getInventory().get(0).getItemImage(), (screenPosition.x * GameConfiguration.tileSize), (screenPosition.y * GameConfiguration.tileSize), this);
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
     *
     * @param g graphics object
     */
    public void paintBorder(Graphics g)
    {
        super.paintBorder(g);
    }

    /**
     * use the Bresenhaim algorithm to calculate LoS
     * should investigate <a href="https://www.redblobgames.com/articles/visibility/">https://www.redblobgames.com/articles/visibility</a>
     *
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
                    //logger.info("what is blocked: {} and hidden: {}", t.toString(), t.isHidden());
                    g.drawImage(blackImage, (p.x * GameConfiguration.tileSize), (p.y * GameConfiguration.tileSize), this);
                }
            }
        }
    }

    public Point getHighlightPosition()
    {
        return highlightPosition;
    }

    public void setHighlightPosition(Point highlightPosition)
    {
        //logger.info("set highlight position: {}", SwingUtilities.isEventDispatchThread());
        this.highlightPosition = highlightPosition;
    }

    public synchronized int getHighlightCount()
    {
        //logger.info("get highlight position: {}", SwingUtilities.isEventDispatchThread());
        return highlightCount;
    }

    public synchronized void setHighlightCount(int highlightCount)
    {
        this.highlightCount = highlightCount;
    }

    public synchronized void increaseHighlightCount()
    {
        //logger.info("increase highlight count: {}", SwingUtilities.isEventDispatchThread());
        if (getHighlightCount() == 0)
        {
            setHighlightCount(2);
        }

        else if (getHighlightCount() == 2)
        {
            setHighlightCount(4);
        }

        else if (getHighlightCount() == 4)
        {
            setHighlightCount(0);
        }
        else
        {
            logger.error("not possible");
        }
        //this.paint();
    }
}