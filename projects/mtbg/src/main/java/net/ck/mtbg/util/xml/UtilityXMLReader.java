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
        switch (qName)
        {
            case "items":
                break;
            case "item":
                utilityItem = new Utility();
                break;
            case "id":
                break;
            case "name":
                break;
            case "type":
                break;
            case "value":
                break;
            case "weight":
                break;
            case "capacity":
                break;
            case "container":
                break;
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        switch (qName)
        {
            case "items":
                break;

            case "item":
                utilityList.put(utilityItem.getId(), utilityItem);
                break;

            case "id":
                utilityItem.setId(Integer.parseInt(data.toString()));
                break;

            case "name":
                utilityItem.setName(data.toString());
                break;

            case "value":
                utilityItem.setValue(Integer.parseInt(data.toString()));
                break;

            case "weight":
                utilityItem.setWeight(Double.parseDouble(data.toString()));
                break;

            case "capacity":
                utilityItem.setCapacity(Double.parseDouble(data.toString()));
                break;

            case "container":
                utilityItem.setContainer(Boolean.valueOf(data.toString()));
                break;

            case "image":
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
}
