package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.state.SkillManager;
import net.ck.mtbg.backend.state.SpellManager;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class ImageManager
{
    /**
     * this contains the animation frames per TYPE: WARRIOR, PLAYER, BEGGAR ...
     */
    @Getter
    @Setter
    private static Hashtable<NPCType, BufferedImage[]> lifeformImages = new Hashtable<>(NPCType.values().length);
    /**
     * this contains the images like hit miss, bloodstain, need to keep a list somewhere
     * or rather change the helpers in imageutils accordingly.
     */
    @Getter
    @Setter
    private static Hashtable<ActionStates, BufferedImage> additionalImages = new Hashtable<>(ActionStates.values().length);

    @Getter
    @Setter
    private static Hashtable<ActionStates, Integer> actionImages = new Hashtable<>(ActionStates.values().length);

    @Getter
    @Setter
    private static Hashtable<Integer, BufferedImage> spellMenuImages = new Hashtable<>(SpellManager.getSpellList().size());

    @Getter
    @Setter
    private static Hashtable<Integer, BufferedImage> skillMenuImages = new Hashtable<>(SkillManager.getSkillList().size());

    @Getter
    @Setter
    private static Hashtable<AttributeTypes, BufferedImage> attributeImages = new Hashtable<>(AttributeTypes.values().length);


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
                    logger.error("interesting");
                    break;
            }
        }
    }

    public static Integer getActionImage(ActionStates state)
    {
        return getActionImages().get(state);
    }


    public static void loadSkillMenuImages()
    {

        for (AbstractSkill skill : SkillManager.getSkillList().values())
        {
            logger.info("loading menu image for skill: {}", skill.getName());
            BufferedImage image = ImageUtils.makeImageTransparent(GameConfiguration.skillMenuImages + skill.getName() + ".png");
            getSkillMenuImages().put(skill.getId(), image);
        }
    }
}
