package net.ck.mtbg.items;

import net.ck.mtbg.util.utils.CodeUtils;
import net.ck.mtbg.util.utils.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class FurnitureItem extends AbstractItem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private boolean lightSource;
    private int lightRange;

    @Override
    public BufferedImage getItemImage()
    {
        return ImageUtils.loadImage("furniture", getName());
    }

    public boolean isLightSource()
    {
        return lightSource;
    }

    public void setLightSource(boolean lightSource)
    {
        this.lightSource = lightSource;
    }

    public int getLightRange()
    {
        return lightRange;
    }

    public void setLightRange(int lightRange)
    {
        this.lightRange = lightRange;
    }
}
