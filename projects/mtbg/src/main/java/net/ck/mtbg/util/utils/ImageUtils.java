package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.entities.entities.Player;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.graphics.ImagePair;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.weather.WeatherTypes;

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
@Getter
@Setter
@Log4j2
public class ImageUtils
{
    @Getter
    @Setter
    private static Hashtable<TileTypes, ArrayList<BufferedImage>> tileTypeImages = new Hashtable<>(TileTypes.values().length);

    @Getter
    @Setter
    private static Hashtable<WeatherTypes, ArrayList<BufferedImage>> weatherTypeImages = new Hashtable<>(WeatherTypes.values().length);

    @Setter
    private static BufferedImage inventoryImage;

    /**
     * contains the list of brightened up images
     */
    @Getter
    @Setter
    private static ArrayList<ImagePair> brightenedImages = new ArrayList<>();

    /**
     * Compares two images pixel by pixel.
     * <a href="https://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances">
     * https://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances</a>
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


    public static boolean betterCompareImages(BufferedImage source, BufferedImage target)
    {
        int w1 = source.getWidth();
        int w2 = target.getWidth();
        int h1 = source.getHeight();
        int h2 = target.getHeight();

        if ((w1 != w2) || (h1 != h2))
        {
            return false;
        }
        else
        {
            for (int j = 0; j < h1; j++)
            {
                for (int i = 0; i < w1; i++)
                {
                    //Getting the RGB values of a pixel
                    int pixel1 = source.getRGB(i, j);
                    Color color1 = new Color(pixel1, true);
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    int pixel2 = target.getRGB(i, j);
                    Color color2 = new Color(pixel2, true);
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();
                    //sum of differences of RGB values of the two images
                    int data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                    return data <= 0;
                }
            }
        }
        return false;
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
     * https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
     */
    public static ImageIcon createImageIcon(String path, String description)
    {
        BufferedImage img = null;
        try
        {
            //TODO how to get class loader files
            //logger.debug("path {}", path);
            //InputStream in = ImageUtils.class.getClassLoader().getResourceAsStream("/" + path);
            //img = ImageIO.read(in);
            //byte[] bytes = in.readAllBytes();
            //ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            img = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            //return null;
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
            logger.error("image is not found for fox sake");
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

    public static BufferedImage loadStandardPlayerImage()
    {
        return ImageUtils.makeImageTransparent(GameConfiguration.playerImages + "image0.png");
    }

    public static ArrayList<BufferedImage> loadMovingPlayerImages()
    {
        BufferedImage movingImage;
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < GameConfiguration.animationCycles; i++)
        {
            logger.info("image" + i + ".png");
            movingImage = ImageUtils.makeImageTransparent(GameConfiguration.playerImages + "image" + i + ".png");
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


    public static BufferedImage createImage(Color color, int tileSize)
    {
        BufferedImage img = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_4BYTE_ABGR);

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

        BufferedImage image = ImageManager.getLifeformImages().get(NPCType.PLAYER)[0];

        Point p = new Point(image.getWidth(), image.getHeight());

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
        return ImageUtils.makeImageTransparent(GameConfiguration.npcImages + npc.getType() + File.separator + "image0.png");
    }

    public static ArrayList<BufferedImage> loadMovingPlayerImages(NPC npc)
    {
        BufferedImage movingImage = null;
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < GameConfiguration.animationCycles; i++)
        {
            movingImage = ImageUtils.makeImageTransparent(GameConfiguration.npcImages + npc.getType() + File.separator + "image" + i + ".png");
            images.add(movingImage);
        }
        return images;
    }

    /**
     * create Ocean Images to show off graphics know how.
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
            QuadCurve2D.Double curve = new QuadCurve2D.Double(0, i + 16, 8, i, 16, i + 16);
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
                String filePath = GameConfiguration.miscImages + t + File.separator + j + ".png";
                if (Files.exists(Paths.get(filePath)))
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
            img = ImageIO.read(new File(GameConfiguration.weatherTypesImagesPath + type + ".jpg"));
        }
        catch (IOException e)
        {
            logger.info("image for type {} does not exist, try png: ", type);
            try
            {
                img = ImageIO.read(new File(GameConfiguration.weatherTypesImagesPath + type + ".png"));
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
        String path = GameConfiguration.imagesRootPath + type + File.separator + imageName;
        return makeImageTransparent(path + ".png");
    }

    /**
     * <a href="https://stackoverflow.com/questions/12980780/how-to-change-the-brightness-of-an-image">https://stackoverflow.com/questions/12980780/how-to-change-the-brightness-of-an-image</a>
     * brighten up image
     */
    public static BufferedImage brightenUpImage(BufferedImage image, int x, int y)
    {
        if (GameConfiguration.brightenUpImages == true)
        {

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

            //long start;
            for (ImagePair iP : getBrightenedImages())
            {
                if (iP.getPercentage() == percentage)
                {
                    //if (compareImages(iP.getSourceImage(), image))
                    //if (iP.getSourceImage().equals(image))
                    //start = System.nanoTime();
                    if (compareImages(iP.getSourceImage(), image))
                    {
                        //GameLogs.getRetrieveBrightImages().add(System.nanoTime() - start);
                        //logger.debug("betterCompareImages brighen up image just return image takes: {}", System.nanoTime() - start);
                        return iP.getResultImage();
                    }
                }
            }
            //start = System.nanoTime();
            BufferedImage img = copyImage(image);
            Graphics g = img.getGraphics();

            int brightness = (int) (256 - 256 * percentage);
            if (brightness >= 0)
            {
                g.setColor(new Color(0, 0, 0, brightness));
                g.fillRect(0, 0, img.getWidth(), img.getHeight());
            }
            else
            {
                logger.error("why?");
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, img.getWidth(), img.getHeight());
            }
            getBrightenedImages().add((new ImagePair(percentage, image, img)));
            //logger.debug("brighten up image create image takes: {}", System.nanoTime() - start);
            //GameLogs.getCreateBrightImages().add(System.nanoTime() - start);
            g.dispose();
            return img;
        }
        //just return the image, do nothing
        else
        {
            return image;
        }
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

    public static BufferedImage getInventoryImage()
    {
        if (inventoryImage == null)
        {
            inventoryImage = ImageUtils.loadImage("players", "playerinventory");
        }

        return inventoryImage;
    }
}
