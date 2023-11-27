package net.ck.mtbg.backend.entities.skills;

import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class AbstractSpell
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    protected String name;
    protected boolean adjecient;
    protected boolean immediately;
    protected BufferedImage menuImage;
    protected int costs;
    protected BufferedImage actionImage;

    protected int level;

    public int getLevel()
    {
        //logger.info("spell level: {}", level);
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    protected int id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isAdjecient()
    {
        return adjecient;
    }

    public void setAdjecient(boolean adjecient)
    {
        this.adjecient = adjecient;
    }

    public boolean isImmediately()
    {
        return immediately;
    }

    public void setImmediately(boolean immediately)
    {
        this.immediately = immediately;
    }

    public BufferedImage getMenuImage()
    {
        return ImageManager.getSpellMenuImages().get(id);
    }

    public void setMenuImage(BufferedImage menuImage)
    {
        this.menuImage = menuImage;
    }

    public int getCosts()
    {
        return costs;
    }

    public void setCosts(int costs)
    {
        this.costs = costs;
    }

    public BufferedImage getActionImage()
    {
        return actionImage;
    }

    public void setActionImage(BufferedImage actionImage)
    {
        this.actionImage = actionImage;
    }

    public String toString()
    {
        return getName() + " " + getMenuImage() + " " + getActionImage();
    }
}
