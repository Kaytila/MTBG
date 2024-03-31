package net.ck.mtbg.backend.entities.skills;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageManager;

import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class AbstractSpell
{
    protected String name;
    protected boolean adjecient;
    protected boolean immediately;
    protected BufferedImage menuImage;
    protected int costs;
    protected BufferedImage actionImage;
    protected int level;
    protected int id;



    public BufferedImage getMenuImage()
    {
        return ImageManager.getSpellMenuImages().get(id);
    }

    public String toString()
    {
        return getName() + " " + getMenuImage() + " " + getActionImage();
    }
}
