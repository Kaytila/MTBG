package net.ck.util.xml;

import net.ck.game.items.Armor;
import net.ck.game.items.ArmorPositions;
import net.ck.game.items.ArmorTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

public class ArmorXMLReader extends DefaultHandler
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	private StringBuilder data = null;

	private Hashtable<Integer, Armor> armorList;
	private boolean item;
	private boolean id;
	private boolean armorclass;
	private boolean position;
	private boolean type;

	private Armor armor;
	private boolean weight;
	private boolean value;
	private boolean armorItems;
	private boolean name;

	@Override
	public void startDocument()
	{
		armorList = new Hashtable<Integer, Armor>();
	}

	@Override
	public void endDocument()
	{

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		//logger.info("qName: {}", qName);
		
		if (qName.equalsIgnoreCase("armorItems"))
		{
			armorItems = true;
			
		}
		
		if (qName.equalsIgnoreCase("armorItem"))
		{
			item = true;
			armor = new Armor();
		}

		if (qName.equalsIgnoreCase("id"))
		{
			id = true;

		}

		if (qName.equalsIgnoreCase("type"))
		{
			type = true;

		}

		if (qName.equalsIgnoreCase("position"))
		{
			position = true;

		}

		if (qName.equalsIgnoreCase("armorclass"))
		{
			armorclass = true;

		}

		if (qName.equalsIgnoreCase("value"))
		{
			value = true;

		}

		if (qName.equalsIgnoreCase("weight"))
		{
			weight = true;

		}
		
		if (qName.equalsIgnoreCase("name"))
		{
			name = true;
		}
		
		data = new StringBuilder();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
	{
		if (armorItems)
		{
			armorItems = false;
		}
		
		if (id)
		{
			id = false;
			armor.setId(Integer.parseInt(data.toString()));
		}
		
		if (item)
		{
			item = false;
			armorList.put(armor.getId(), armor);
		}
		else if (type)
		{
			type = false;
			armor.setType(ArmorTypes.valueOf(data.toString()));
		}

		else if (position)
		{
			position = false;
			armor.setPosition(ArmorPositions.valueOf(data.toString()));
		}

		else if (armorclass)
		{
			armorclass = false;
			armor.setArmorClass(Integer.parseInt(data.toString()));
		}

		else if (value)
		{
			value = false;
			armor.setValue(Integer.parseInt(data.toString()));
		}

		else if (weight)
		{
			weight = false;
			armor.setWeight(Double.parseDouble(data.toString()));
		}
		
		else if(name)
		{
			name = false;
			armor.setName(data.toString());
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
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

	public Hashtable<Integer, Armor> getArmorList()
	{
		return armorList;
	}

	
}
