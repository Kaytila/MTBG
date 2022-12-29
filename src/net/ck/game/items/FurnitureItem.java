package net.ck.game.items;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FurnitureItem extends AbstractItem
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private boolean lightSource;
    private int lightRange;


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
