package net.ck.util.xml;

import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.ck.game.items.ArmorTypes;
import net.ck.game.items.Utility;
import net.ck.util.ImageUtils;

public class UtilityXMLReader extends DefaultHandler
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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

	
	
	public Hashtable<Integer, Utility> getUtilityList()
	{
		return utilityList;
	}

	public void setUtilityList(Hashtable<Integer, Utility> utilityList)
	{
		this.utilityList = utilityList;
	}

	@Override
	public void startDocument() throws SAXException
	{
		utilityList = new Hashtable<Integer, Utility>();
	}

	@Override
	public void endDocument() throws SAXException
	{

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
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
	public void endElement(String uri, String localName, String qName) throws SAXException
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
		
		else if (qName.equalsIgnoreCase("type"))
		{
			type = false;
			utilityItem.setType(ArmorTypes.valueOf(data.toString()));
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
			utilityItem.setItemImage(ImageUtils.loadImage("items", data.toString()));		
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		data.append(new String(ch, start, length));
	}

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

	public Logger getLogger()
	{
		return logger;
	}

}
