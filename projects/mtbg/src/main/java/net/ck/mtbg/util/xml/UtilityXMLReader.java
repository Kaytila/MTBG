package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.Utility;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class UtilityXMLReader extends DefaultHandler
{
    private StringBuilder data = null;

    private Hashtable<Integer, Utility> utilityList;

    private Utility utilityItem;

    private boolean item;

    private boolean items;

    private boolean container;

    private boolean capacity;

    private boolean weight;

    private boolean value;

    private boolean type;

    private boolean name;

    private boolean id;

    private boolean image;

    @Override
    public void startDocument()
    {
        utilityList = new Hashtable<Integer, Utility>();
    }

    @Override
    public void endDocument()
    {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        if (qName.equalsIgnoreCase("items"))
        {
            items = true;

        }

        else if (qName.equalsIgnoreCase("item"))
        {
            item = true;
            utilityItem = new Utility();
        }

        else if (qName.equalsIgnoreCase("id"))
        {
            id = true;
        }

        else if (qName.equalsIgnoreCase("name"))
        {
            name = true;
        }

        else if (qName.equalsIgnoreCase("type"))
        {
            type = true;

        }

        else if (qName.equalsIgnoreCase("value"))
        {
            value = true;

        }

        else if (qName.equalsIgnoreCase("weight"))
        {
            weight = true;

        }

        else if (qName.equalsIgnoreCase("capacity"))
        {
            capacity = true;

        }

        else if (qName.equalsIgnoreCase("container"))
        {
            container = true;
        }

        else if (qName.equalsIgnoreCase("image"))
        {
            image = true;
        }
        // create the data container
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if (qName.equalsIgnoreCase("items"))
        {
            items = false;

        }

        else if (qName.equalsIgnoreCase("item"))
        {
            item = false;
            utilityList.put(utilityItem.getId(), utilityItem);
        }

        else if (qName.equalsIgnoreCase("id"))
        {
            id = true;
            utilityItem.setId(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("name"))
        {
            name = false;
            utilityItem.setName(data.toString());
        }

        else if (qName.equalsIgnoreCase("value"))
        {
            value = false;
            utilityItem.setValue(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("weight"))
        {
            weight = false;
            utilityItem.setWeight(Double.parseDouble(data.toString()));
        }

        else if (qName.equalsIgnoreCase("capacity"))
        {
            capacity = false;
            utilityItem.setCapacity(Double.parseDouble(data.toString()));
        }

        else if (qName.equalsIgnoreCase("container"))
        {
            container = false;
            utilityItem.setContainer(Boolean.valueOf(data.toString()));
        }

        else if (qName.equalsIgnoreCase("image"))
        {
            image = false;
            //utilityItem.setItemImage(ImageUtils.loadImage("items", data.toString()));
        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }
}
