package net.ck.util;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.NPCTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class ImageManager
{
    private static final Logger logger = LogManager.getLogger(ImageManager.class);
    /**
     * this contains the animation frames per TYPE: WARRIOR, PLAYER, BEGGAR ...
     */
    private static Hashtable<NPCTypes, BufferedImage[]> lifeformImages = new Hashtable<>(NPCTypes.values().length);
    /**
     * this contains the images like hit miss, bloodstain, need to keep a list somewhere
     * or rather change the helpers in imageutils accordingly.
     */
    private static Hashtable<Integer, BufferedImage[]> additionalImages = new Hashtable<>();

    public static Hashtable<Integer, BufferedImage[]> getAdditionalImages()
    {
        return additionalImages;
    }

    public static void setAdditionalImages(Hashtable<Integer, BufferedImage[]> additionalImages)
    {
        ImageManager.additionalImages = additionalImages;
    }

    public static Hashtable<NPCTypes, BufferedImage[]> getLifeformImages()
    {
        return lifeformImages;
    }

    public static void setLifeformImages(Hashtable<NPCTypes, BufferedImage[]> lifeformImages)
    {
        ImageManager.lifeformImages = lifeformImages;
    }

    public static void loadLifeFormImages()
    {

        for (NPCTypes type : NPCTypes.values())
        {
            try
            {
                logger.info("type: {}", type);
                BufferedImage[] images = new BufferedImage[GameConfiguration.animationCycles];
                for (int i = 0; i < GameConfiguration.animationCycles; i++)
                {
                    logger.info("image: {}", i);
                    BufferedImage image = ImageUtils.makeImageTransparent(GameConfiguration.npcImages + type + File.separator + "image" + i + ".png");
                    images[i] = image;
                }
                getLifeformImages().put(type, images);
            } catch (Exception e)
            {
                logger.debug("missing images for type: {}", type);
            }
        }
    }

}
