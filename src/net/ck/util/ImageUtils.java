package net.ck.util;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.entities.Player;
import net.ck.game.backend.game.Game;
import net.ck.game.graphics.TileTypes;
import net.ck.game.map.MapTile;
import net.ck.game.weather.WeatherTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Random;

/**
 * Util class for images, what a useful comment I write
 *
 * @author Claus
 */
public class ImageUtils
{
    private static final Logger logger = LogManager.getLogger(ImageUtils.class);





    private static Hashtable<TileTypes, ArrayList<BufferedImage>> tileTypeImages = new Hashtable<>();
    private static Hashtable<WeatherTypes, ArrayList<BufferedImage>> weatherTypeImages = new Hashtable<>();
    private static BufferedImage bloodstainImage;
    private static BufferedImage healImage;
    private static BufferedImage hitImage;

    private static BufferedImage missImage;

    private static BufferedImage inventoryImage;

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB)
    {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight())
        {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @param fileName contains the file name
     * @return BufferedImage with white turned into transparent
     * <p>
     * <a href="https://stackoverflow.com/questions/14097386/how-to-make-drawn-images-transparent-in-java">https://stackoverflow.com/questions/14097386/how-to-make-drawn-images-transparent-in-java</a>
     */
    public static BufferedImage makeImageTransparent(String fileName)
    {
        // logger.info("filename: {}", fileName);
        //  (Files.exists(Paths.get(fileName)))
        //{
        ImageIcon originalIcon = createImageIcon(fileName, "");

        ImageFilter filter = new RGBImageFilter()
        {
            final int transparentColor = Color.white.getRGB() | 0xFF000000;

            public int filterRGB(int x, int y, int rgb)
            {
                if ((rgb | 0xFF000000) == transparentColor)
                {
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    return rgb;
                }
            }
        };

        ImageProducer filteredImgProd = new FilteredImageSource(originalIcon.getImage().getSource(), filter);
        Image transparentImage = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
        return toBufferedImage(transparentImage);
    }

    /**
     * @return BufferedImage with white turned into transparent
     * <p>
     * <a href="https://stackoverflow.com/questions/14097386/how-to-make-drawn-images-transparent-in-java">https://stackoverflow.com/questions/14097386/how-to-make-drawn-images-transparent-in-java</a>
     */
    public static BufferedImage makeImageTransparent(ImageIcon originalIcon)
    {
        ImageFilter filter = new RGBImageFilter()
        {
            final int transparentColor = Color.white.getRGB() | 0xFF000000;

            public int filterRGB(int x, int y, int rgb)
            {
                if ((rgb | 0xFF000000) == transparentColor)
                {
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    return rgb;
                }
            }
        };

        ImageProducer filteredImgProd = new FilteredImageSource(originalIcon.getImage().getSource(), filter);
        Image transparentImage = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
        return toBufferedImage(transparentImage);
    }

    /**
     * @param path        path to the image file to create an icon for
     * @param description - no description;
     * @return ImageIcon which is then used to take away the white.
     * <p>
     * <a href="https://stackoverflow.com/questions/29135718/substitute-imageicon-with-imageio-to-load-images">https://stackoverflow.com/questions/29135718/substitute-imageicon-with-imageio-to-load-images</a>
     */
    public static ImageIcon createImageIcon(String path, String description)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            // logger.error("error: {}", e);
        }
        ImageIcon icon;
        icon = new ImageIcon(Objects.requireNonNull(img));
		icon.setDescription(description);
        return icon;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage <a href="https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage">https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage</a>
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img == null)
        {
            logger.error("image is not found for foxsake");
            System.exit(-1);
        }
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    public static BufferedImage loadStandardPlayerImage(Player player)
    {
        BufferedImage standardImage = null;

        try
        {
            // logger.info("standard image: {}", getImagerootpath() + player.getNumber() + "/image1.png");
            standardImage = ImageUtils.makeImageTransparent(GameConfiguration.playerImages + player.getNumber() + File.separator + "image1.png");
        }
        catch (java.security.AccessControlException e2)
        {
            logger.error("caught it");
        }
        return standardImage;
    }

    public static ArrayList<BufferedImage> loadMovingPlayerImages(Player player)
    {
        BufferedImage movingImage = null;
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < GameConfiguration.animationCycles; i++)
        {
            try
            {
                // logger.info("images: {} ", getImagerootpath() + player.getNumber() + "/image" + i + ".png");
                movingImage = ImageUtils.makeImageTransparent(GameConfiguration.playerImages + player.getNumber() + File.separator + "image" + i + ".png");
            }
            catch (java.security.AccessControlException e2)
            {
                logger.error("caught it");
            }

            images.add(movingImage);
        }

        return images;
    }

    /**
     * @param color - creates an image in the desired size (see game) with the desired color
     * @return new Image in tile size with the color in parameter color used to draw the empty tiles at the edge, used later for LoS
     */
    public static BufferedImage createImage(Color color)
    {
        BufferedImage img = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_4BYTE_ABGR);

        for (int px1 = 0; px1 < (img.getWidth()); px1++)
        {
            for (int px2 = 0; px2 < (img.getHeight()); px2++)
            {
                img.setRGB(px1, px2, color.getRGB());
            }
        }
        return img;
    }

    /**
     * @param color return new Image in tile size with the color in parameter color used to draw the empty tiles at the edge, used later for LoS
     */
    public static void createImage(Color color, Point size, String target)
    {
        BufferedImage img = new BufferedImage(size.x, size.y, BufferedImage.TYPE_4BYTE_ABGR);

        for (int px1 = 0; px1 < (img.getWidth()); px1++)
        {
            for (int px2 = 0; px2 < (img.getHeight()); px2++)
            {
                img.setRGB(px1, px2, color.getRGB());
            }
        }

        File newFile = new File(target);
        newFile.getParentFile().mkdirs();
        try
        {
            // logger.info("writing image: {}, filePath: {}", img.toString(), filePath);
            ImageIO.write(img, "png", newFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param tile mapfile you want to create
     * @return public enum TileTypes { DESERT, GRASS, HILL, MOUNTAIN, OCEAN, RIVEREE, RIVEREN, RIVERES, RIVERNE, RIVERNS, RIVERNW, RIVERSE, RIVERSS, RIVERSW, RIVERWN, RIVERWS, RIVERWW, SWAMP
     * <p>
     * }
     **/
    public static Color convertTileTypeToColor(MapTile tile)
    {

        switch (tile.getType())
        {
            case GRASS:
                return Color.green;
            // break;
            case OCEAN:
                return Color.blue;
            // break;
            default:
                return Color.gray;
            // break;
        }
    }


    /**
     *
     */
    public static void checkImageSize(Player player)
    {
        // first player
        Point p = new Point(player.getAppearance().getStandardImage().getWidth(), player.getAppearance().getStandardImage().getHeight());

            // check if the dimensions match, first x
            if (GameConfiguration.imageSize.x == p.x)
            {
                // then y
                if (GameConfiguration.imageSize.y == p.y)
                {
                    logger.info("sizes add up");
                }
                else
                {
                    logger.error("standard image does not match in height");
                }
            }
            else
            {
                logger.error("standard image does not match in width");
            }

    }

    public static BufferedImage loadStandardPlayerImage(NPC npc)
    {
        BufferedImage standardImage = null;

        try
        {
            // logger.info("standard image: {}", "graphics" + "/" + "npc" + "/" + "warrior" + "/image1.png");
            standardImage = ImageUtils.makeImageTransparent("graphics" + File.separator + "npc" + File.separator + npc.getType() + File.separator + "image1.png");
        }
        catch (java.security.AccessControlException e2)
        {
            logger.error("caught it");
        }
        return standardImage;
    }

    public static ArrayList<BufferedImage> loadMovingPlayerImages(NPC npc)
    {
        BufferedImage movingImage = null;
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < GameConfiguration.animationCycles; i++)
        {
            try
            {
                // logger.info("images: {} ", "graphics" + "/" + "npc" + "/" + "warrior" + "/image1.png");
                movingImage = ImageUtils.makeImageTransparent("graphics" + File.separator + "npc" + File.separator + npc.getType() + File.separator + "image" + i + ".png");
            }
            catch (java.security.AccessControlException e2)
            {
                logger.error("caught it");
            }

            images.add(movingImage);
        }

        return images;
    }

    /**
     * return bufferedimage but i do not know whether i need this, probably not
     */
    public static void createOceanImages()
    {
        BufferedImage img = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i <= GameConfiguration.animationCycles; i++)
        {
            for (int px1 = 0; px1 < (img.getWidth()); px1++)
            {
                for (int px2 = 0; px2 < (img.getHeight()); px2++)
                {
                    img.setRGB(px1, px2, Color.blue.getRGB());
                }
            }
            Graphics2D gr = img.createGraphics();
            QuadCurve2D.Double curve = new QuadCurve2D.Double(0, i + 16, 8, i + 0, 16, i + 16);
            gr.draw(curve);
            QuadCurve2D.Double curve2 = new QuadCurve2D.Double(16, i + 16, 24, 32, 32, i + 16);
            gr.draw(curve2);
            gr.dispose();
            String filePath = GameConfiguration.miscImages + File.separator + TileTypes.OCEAN + File.separator + i + ".png";
            File newFile = new File(filePath);
            newFile.getParentFile().mkdirs();
            try
            {
                // logger.info("writing image: {}, filePath: {}", img.toString(), filePath);
                ImageIO.write(img, "png", newFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage loadRandomBackgroundImage(TileTypes tileType)
    {
        Random rand = new Random();
        int i = rand.nextInt(GameConfiguration.animationCycles);
        String filePath = GameConfiguration.miscImages + tileType + File.separator + i + ".png";

        try
        {
            return (ImageIO.read(new File(filePath)));
        }
        catch (IOException e)
        {
            logger.error("cannot read file: {}", filePath);
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;
    }

    public static Hashtable<TileTypes, ArrayList<BufferedImage>> getTileTypeImages()
    {
        return tileTypeImages;
    }

    public static void setTileTypeImages(Hashtable<TileTypes, ArrayList<BufferedImage>> tileTypeImagess)
    {
        tileTypeImages = tileTypeImagess;
    }

    public static void initializeBackgroundImages()
    {
        logger.info("initializing background images");
        for (TileTypes t : TileTypes.values())
        {
            ArrayList<BufferedImage> list = new ArrayList<>();
            for (int i = 0; i <= GameConfiguration.animationCycles; i++)
            {
                list.add(i, null);
            }

            int j;
            for (j = 0; j <= GameConfiguration.animationCycles; j++)
            {
                String filePath =GameConfiguration.miscImages + t + File.separator + j + ".png";
				if(Files.exists(Paths.get(filePath)))
				{
					try
					{
						list.add(j, ImageIO.read(new File(filePath)));
					}
					catch (IOException e)
					{
						logger.error("cannot read file: {}", filePath);
						//e.printStackTrace();
					}
				}
            }
            tileTypeImages.put(t, list);
        }
        logger.info("done initializing background images");
    }

    public static void listBackGroundImages()
    {
        for (TileTypes t : TileTypes.values())
        {
            logger.info("tile type: {}", t.toString());
            int i = 0;
            for (BufferedImage img : tileTypeImages.get(t))
            {
                if (img != null)
                {
                    logger.info("image: {}", img.toString());
                }
                else
                {
                    logger.error("image number {} missing: ", i);
                }
                i++;
            }
        }
    }

    public static BufferedImage loadWeatherImage(WeatherTypes type)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File("graphics" + File.separator + "weathertypes" + File.separator + type + ".jpg"));
        }
        catch (IOException e)
        {
            logger.info("image for type {} does not exist, try png: ", type);
            try
            {
                img = ImageIO.read(new File("graphics" + File.separator + "weathertypes" + File.separator + type + ".png"));
            }
            catch (IOException e1)
            {
                logger.info("image for type {} does not exist either for png: ", type);
            }
        }
        return img;
    }

    public static void initializeForegroundImages()
    {
        logger.info("initializing foreground images");
        for (WeatherTypes t : WeatherTypes.values())
        {
            ArrayList<BufferedImage> list = new ArrayList<>();
            for (int i = 0; i <= GameConfiguration.animationCycles; i++)
            {
                list.add(i, null);
            }

            int j;
            for (j = 0; j <= GameConfiguration.animationCycles; j++)
            {
                BufferedImage img;
                String filePath = GameConfiguration.weatherImagesPath + t + File.separator + j + ".png";
                try
                {
                    if (Files.exists(Paths.get(filePath)))
                    {
                        img = ImageUtils.makeImageTransparent(filePath);
                        list.add(j, img);
                    }
                }
                catch (Exception e)
                {
                    // logger.error("image {} does not exist", img);
                }

            }
            weatherTypeImages.put(t, list);
        }
        logger.info("done initializing background images");
    }

    public static Hashtable<WeatherTypes, ArrayList<BufferedImage>> getWeatherTypeImages()
    {
        return weatherTypeImages;
    }

    public static void setWeatherTypeImages(Hashtable<WeatherTypes, ArrayList<BufferedImage>> weatherTypeImages)
    {
        ImageUtils.weatherTypeImages = weatherTypeImages;
    }

    public static void createWeatherTypesImages(WeatherTypes type)
    {
        BufferedImage img = new BufferedImage(GameConfiguration.tileSize, GameConfiguration.tileSize, BufferedImage.TYPE_4BYTE_ABGR);
        Color color = null;

        int spaceBetweenColumns = img.getWidth() / 3;
        int spaceBetweenRows = img.getHeight() / GameConfiguration.animationCycles;

        logger.info("columns: {}, rows: {}", spaceBetweenColumns, spaceBetweenRows);
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            columns.add(4 + i * spaceBetweenColumns);
            logger.info("column to add: {}", 4 + i * spaceBetweenColumns);
        }

        for (int i = 0; i <= GameConfiguration.animationCycles; i++)
        {
            rows.add(4 + i * spaceBetweenRows);
            logger.info("row to add: {}", 4 + i * spaceBetweenRows);
        }

        switch (type)
        {
            case HAIL:
                color = Color.DARK_GRAY;
                break;
            case RAIN:
                color = Color.blue;
                break;
            case SNOW:
                color = Color.lightGray;
                break;
            default:
                break;
        }

        for (int i = 0; i <= GameConfiguration.animationCycles; i++)
        {
            // make image white;
            for (int px1 = 0; px1 < (img.getWidth()); px1++)
            {
                for (int px2 = 0; px2 < (img.getHeight()); px2++)
                {
                    img.setRGB(px1, px2, Color.white.getRGB());
                }
            }

            for (int px1 = 0; px1 < (img.getWidth()); px1++)
            {
                for (int c : columns)
                {
                    if (px1 == c)
                    {
                        for (int px2 = 0; px2 < (img.getHeight()); px2++)
                        {
                            if (px2 == rows.get(i))
                            {
                                img.setRGB(px1, px2, Objects.requireNonNull(color).getRGB());
                            }

                        }
                    }
                }

            }

            String filePath = GameConfiguration.weatherImagesPath + type + File.separator + i + ".png";
            File newFile = new File(filePath);
            newFile.getParentFile().mkdirs();
            try
            {
                // logger.info("writing image: {}, filePath: {}", img.toString(), filePath);
                ImageIO.write(img, "png", newFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage loadImage(String type, String imageName)
    {
        String path = GameConfiguration.imagesRootPath  + type + File.separator + imageName;
        logger.debug("loading image from: {}", path);
        return makeImageTransparent(path + ".png");
    }

    /**
     * <a href="https://stackoverflow.com/questions/12980780/how-to-change-the-brightness-of-an-image">https://stackoverflow.com/questions/12980780/how-to-change-the-brightness-of-an-image</a>
     * brighten up image
     */
    public static BufferedImage brightenUpImage(BufferedImage image, int x, int y)
    {
        BufferedImage img = copyImage(image);
        Graphics g = img.getGraphics();
        float percentage = 1.0f;
        int max = Math.max(x, y);
        int half = Math.floorDiv(GameConfiguration.numberOfTiles, 2);

        if (max <= 2)
        {
            percentage = 1.0f;
        }
        else if (max < half)
        {
            percentage = 0.8f;
        }
        else if (max == half)
        {
            percentage = 0.6f;
        }
        // float percentage2 = 1.0f * (1.0f - 100 / (Math.max(x,y)) ;// / (Math.max(x,y)); // 50% bright - change this (or set dynamically) as you feel fit

        int brightness = (int) (256 - 256 * percentage);
        if (brightness >= 0)
        {
            g.setColor(new Color(0, 0, 0, brightness));
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
        }
        else
        {
            logger.info("why?");
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
        }
        return img;
    }

    /**
     * Produces a copy of the supplied image
     *
     * @param image The original image
     * @return The new BufferedImage
     */
    public static BufferedImage copyImage(BufferedImage image)
    {
        return scaledImage(image, image.getWidth(), image.getHeight());
    }

    /**
     * Produces a resized image that is of the given dimensions
     *
     * @param image  The original image
     * @param width  The desired width
     * @param height The desired height
     * @return The new BufferedImage
     */
    public static BufferedImage scaledImage(BufferedImage image, int width, int height)
    {
        BufferedImage newImage = createCompatibleImage(width, height);
        Graphics graphics = newImage.createGraphics();

        graphics.drawImage(image, 0, 0, width, height, null);

        graphics.dispose();
        return newImage;
    }

    /**
     * Creates an image compatible with the current display
     *
     * @return A BufferedImage with the appropriate color model
     */
    public static BufferedImage createCompatibleImage(int width, int height)
    {
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public static BufferedImage getHitImage()
    {
        if (hitImage == null)
        {
            hitImage = ImageUtils.loadImage("combat", "explosion");
        }

        return hitImage;
    }


    public static BufferedImage getBloodstainImage()
    {
        if (bloodstainImage == null)
        {
            bloodstainImage = ImageUtils.loadImage("combat", "bloodstain");
        }

        return bloodstainImage;
    }

    public static BufferedImage getHealImage()
    {
        if (healImage == null)
        {
            healImage = ImageUtils.loadImage("combat", "heal");
        }

        return healImage;
    }

    public static BufferedImage getMissImage()
    {
        if (missImage == null)
        {
            missImage = ImageUtils.loadImage("combat", "miss");
        }

        return missImage;
    }


    public static BufferedImage getInventoryImage()
    {
        if (inventoryImage == null)
        {
            inventoryImage = ImageUtils.loadImage("players", "playerinventory");
        }

        return inventoryImage;
    }

}
