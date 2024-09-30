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
    /**
     * is the furniture a light source?
     */
    private boolean lightSource;

    /**
     * what range does the light source have?
     */
    private int lightRange;

    /**
     * is it currently burning?
     */
    private boolean burning;

    /**
     * is the furniture openable?
     * cupboards perhaps, or chests
     * not sure whether I dont want to model chests as separate items yet
     */
    private boolean openable;

    public FurnitureItem()
    {
        setFurniture(true);
    }

    public FurnitureItem(FurnitureItem that)
    {
        this(that.isLightSource(), that.isBurning(), that.getLightRange(), that.getName(), that.getId(), that.getImage());
    }

    public FurnitureItem(boolean lightSource, boolean burning, int lightRange, String name, int id, String image)
    {
        this.setLightSource(lightSource);
        this.setBurning(burning);
        this.setLightRange(lightRange);
        this.setId(id);
        this.setName(name);
        this.setFurniture(true);
        this.setImage(image);
    }

    @Override
    public String toString()
    {
        return "FurnitureItem{" +
                "lightSource=" + lightSource +
                ", lightRange=" + lightRange +
                ", burning=" + burning +
                '}';
    }

    @Override
    public BufferedImage getItemImage()
    {
        return ImageUtils.loadImage("furniture", getImage());
    }

}
