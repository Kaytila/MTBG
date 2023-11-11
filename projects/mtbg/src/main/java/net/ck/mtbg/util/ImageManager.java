package net.ck.mtbg.util;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.state.SpellManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class ImageManager
{
    private static final Logger logger = LogManager.getLogger(ImageManager.class);
    /**
     * this contains the animation frames per TYPE: WARRIOR, PLAYER, BEGGAR ...
     */
    //
    private static Hashtable<NPCType, BufferedImage[]> lifeformImages = new Hashtable<>(NPCType.values().length);
    /**
     * this contains the images like hit miss, bloodstain, need to keep a list somewhere
     * or rather change the helpers in imageutils accordingly.
     */
    private static Hashtable<ActionStates, BufferedImage> additionalImages = new Hashtable<>(ActionStates.values().length);


    private static Hashtable<ActionStates, Integer> actionImages = new Hashtable<>(ActionStates.values().length);

    private static Hashtable<Integer, BufferedImage> spellMenuImages = new Hashtable<>(SpellManager.getSpellList().size());

    public static Hashtable<Integer, BufferedImage> getSpellMenuImages()
    {
        return spellMenuImages;
    }

    public static void setSpellMenuImages(Hashtable<Integer, BufferedImage> spellMenuImages)
    {
        ImageManager.spellMenuImages = spellMenuImages;
    }


    public static Hashtable<ActionStates, BufferedImage> getAdditionalImages()
    {
        return additionalImages;
    }


    private static Hashtable<AttributeTypes, BufferedImage> attributeImages = new Hashtable<>(AttributeTypes.values().length);


    public static void setAdditionalImages(Hashtable<ActionStates, BufferedImage> additionalImages)
    {
        ImageManager.additionalImages = additionalImages;
    }

    public static Hashtable<NPCType, BufferedImage[]> getLifeformImages()
    {
        return lifeformImages;
    }

    public static void setLifeformImages(Hashtable<NPCType, BufferedImage[]> lifeformImages)
    {
        ImageManager.lifeformImages = lifeformImages;
    }


    public static void initializeAttributeImages()
    {
        for (AttributeTypes type : AttributeTypes.values())
        {
            getAttributeImages().put(type, (Scalr.resize(ImageUtils.loadImage("players" + File.separator + "attributes", type.toString()), Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, GameConfiguration.lineHight, GameConfiguration.lineHight, Scalr.OP_ANTIALIAS)));
            //setImage(Scalr.resize(ImageUtils.loadImage("players" + File.separator + "attributes", getType().toString()), Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, GameConfiguration.lineHight, GameConfiguration.lineHight, Scalr.OP_ANTIALIAS));
        }
    }


    public static void initializeItemImages()
    {
        initializeArmorImages();
        initializeWeaponImages();
        initializeFurnitureItemImages();
    }

    private static void initializeWeaponImages()
    {
    }

    private static void initializeArmorImages()
    {
    }

    private static void initializeFurnitureItemImages()
    {

    }


    public static void loadLifeFormImages()
    {

        for (NPCType type : NPCType.values())
        {
            try
            {
                //logger.info("type: {}", type);
                BufferedImage[] images = new BufferedImage[GameConfiguration.animationCycles + GameConfiguration.numberOfAdditionalImages];
                int i;
                for (i = 0; i < GameConfiguration.animationCycles; i++)
                {
                    BufferedImage image = ImageUtils.makeImageTransparent(GameConfiguration.npcImages + type + File.separator + "image" + i + ".png");
                    images[i] = image;
                }
                images[i] = ImageUtils.loadImage("combat", "hit");
                getActionImages().put(ActionStates.HIT, i);
                i++;
                images[i] = ImageUtils.loadImage("combat", "miss");
                getActionImages().put(ActionStates.MISS, i);
                i++;
                images[i] = ImageUtils.loadImage("combat", "kill");
                getActionImages().put(ActionStates.KILL, i);
                i++;
                images[i] = ImageUtils.loadImage("combat", "heal");
                getActionImages().put(ActionStates.HEAL, i);

                getLifeformImages().put(type, images);
                i = 0;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                logger.debug("missing images for type: {}", type);
            }
        }
    }

    public static void loadSpellMenuImages()
    {

        for (AbstractSpell spell : SpellManager.getSpellList().values())
        {
            logger.info("loading menu image for spell: {}", spell.getName());
            BufferedImage image = ImageUtils.makeImageTransparent(GameConfiguration.spellMenuImages + spell.getName() + ".png");
            getSpellMenuImages().put(spell.getId(), image);
        }
    }


    public static void loadAdditionalImages()
    {

        for (ActionStates state : ActionStates.values())
        {
            switch (state)
            {
                case HIT:
                    getAdditionalImages().put(state, ImageUtils.loadImage("combat", "hit"));
                    break;
                case HEAL:
                    getAdditionalImages().put(state, ImageUtils.loadImage("combat", "heal"));
                    break;
                case MISS:
                    getAdditionalImages().put(state, ImageUtils.loadImage("combat", "miss"));
                    break;
                case KILL:
                    getAdditionalImages().put(state, ImageUtils.loadImage("combat", "kill"));
                    break;
                case POISON:
                    break;
                default:
                    logger.info("interesting");
                    break;
            }
        }
    }


    public static Hashtable<ActionStates, Integer> getActionImages()
    {
        return actionImages;
    }

    public static void setActionImages(Hashtable<ActionStates, Integer> actionImages)
    {
        ImageManager.actionImages = actionImages;
    }

    public static Integer getActionImage(ActionStates state)
    {
        return getActionImages().get(state);
    }

    public static Hashtable<AttributeTypes, BufferedImage> getAttributeImages()
    {
        return attributeImages;
    }

    public static void setAttributeImages(Hashtable<AttributeTypes, BufferedImage> attributeImages)
    {
        ImageManager.attributeImages = attributeImages;
    }
}
