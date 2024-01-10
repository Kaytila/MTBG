package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class SpellXMLReader extends DefaultHandler
{
    private StringBuilder data = null;
    private boolean sp;
    private boolean name;
    private boolean id;
    private Hashtable<Integer, AbstractSpell> spellList;
    private AbstractSpell spell;

    private boolean lvl;


    @Override
    public void startDocument()
    {
        spellList = new Hashtable<>();
    }

    @Override
    public void endDocument()
    {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        if (qName.equalsIgnoreCase("spells"))
        {

        }
        else if (qName.equalsIgnoreCase("spell"))
        {
            sp = true;
            spell = new AbstractSpell();
        }

        else if (qName.equalsIgnoreCase("id"))
        {
            id = true;
        }

        else if (qName.equalsIgnoreCase("name"))
        {
            name = true;
        }
        else if (qName.equalsIgnoreCase("level"))
        {
            lvl = true;
        }

        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if (qName.equalsIgnoreCase("spells"))
        {

        }
        else if (qName.equalsIgnoreCase("spell"))
        {
            sp = false;
            spellList.put(spell.getId(), spell);
        }

        else if (qName.equalsIgnoreCase("id"))
        {
            id = false;
            spell.setId(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("name"))
        {
            name = false;
            spell.setName(data.toString());
        }

        else if (qName.equalsIgnoreCase("level"))
        {
            lvl = false;
            spell.setLevel(Integer.parseInt(data.toString()));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }
}
