package net.ck.mtbg.util.xml;

import net.ck.mtbg.items.FurnitureItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;
import java.util.Objects;

/**
 * <items>
 * <item><id>1</id>
 * <name>torch</name>
 * <type>TORCH</type>
 * <value>1</value>
 * <weight>1.0</weight>
 * <capacity>10</capacity>
 * <container>false</container>
 * <image>torch</image>
 * <lightsource>true</lightsource>
 * <lightrange>2</lightrange>
 * </item>
 * </items>
 */
public class FurnitureReader extends DefaultHandler
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    private StringBuilder data = null;

    public Hashtable<Integer, FurnitureItem> getFurnitureList()
    {
        return furnitureList;
    }

    public void setFurnitureList(Hashtable<Integer, FurnitureItem> furnitureList)
    {
        this.furnitureList = furnitureList;
    }

    public FurnitureItem getItem()
    {
        return item;
    }

    public void setItem(FurnitureItem item)
    {
        this.item = item;
    }

    private Hashtable<Integer, FurnitureItem> furnitureList;
    private FurnitureItem item;

    @Override
    public void startDocument()
    {

        if (furnitureList == null)
            furnitureList = new Hashtable<>();
    }

    @Override
    public void endDocument()
    {
       for (FurnitureItem i : furnitureList.values())
       {
           logger.info("furniture id: {}, item: {}", i.getId(), i);
       }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        switch (qName)
        {
            case "name":
                break;
            case "item":
                item = new FurnitureItem();
                break;
            case "items":
                break;
            case "id":
                break;
            case "value":
                break;
            case "weight":
                break;
            case "lightsource":
                break;
            case "lightrange":
                break;
            case "container":
                break;
            case "image":
                break;
             default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        switch (qName)
        {
            case "name":
                item.setName(data.toString());
                break;
            case "item":
              furnitureList.put(item.getId(), item);
                break;
            case "id":
                item.setId(Integer.parseInt(data.toString()));
                break;
            case "value":
                item.setValue(Integer.parseInt(data.toString()));
                break;
            case "weight":
                item.setWeight(Double.parseDouble(data.toString()));
                break;
            case "lightsource":
                item.setLightSource(Boolean.parseBoolean(data.toString()));
                break;
            case "lightrange":
                item.setLightRange(Integer.parseInt(data.toString()));
                break;
            case "container":
                item.setContainer(Boolean.parseBoolean(data.toString()));
                break;
            case "items":
                break;
            case "image":
                //item.setItemImage(ImageUtils.loadImage("furniture", data.toString()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

}
