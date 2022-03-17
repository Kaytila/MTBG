package net.ck.game.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FurnitureItem extends AbstractItem
{
    private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
    private boolean lightSource;
    private int lightRange;

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        if (enclosingClass != null)
        {
            return enclosingClass;
        }
        else
        {
            return getClass();
        }
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
