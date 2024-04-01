package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageUtils;

import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class FurnitureItem extends AbstractItem
{
    private boolean lightSource;
    private int lightRange;
    private boolean burning;

    public FurnitureItem()
    {
        setFurniture(true);
    }

    public FurnitureItem(FurnitureItem that)
    {
        this(that.isLightSource(), that.isBurning(), that.getLightRange(), that.getName(), that.getId());
    }

    public FurnitureItem(boolean lightSource, boolean burning, int lightRange, String name, int id)
    {
        this.setLightSource(lightSource);
        this.setBurning(burning);
        this.setLightRange(lightRange);
        this.setId(id);
        this.setName(name);
        this.setFurniture(true);
    }

    @Override
    public BufferedImage getItemImage()
    {
        return ImageUtils.loadImage("furniture", getName());
    }

}
