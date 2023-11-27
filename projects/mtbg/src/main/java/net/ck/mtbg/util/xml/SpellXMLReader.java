package net.ck.mtbg.util.xml;

import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

public class SpellXMLReader extends DefaultHandler
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private StringBuilder data = null;
    private boolean sp;
    private boolean name;
    private boolean id;
    private Hashtable<Integer, AbstractSpell> spellList;
    private AbstractSpell spell;

    private boolean lvl;


    public Hashtable<Integer, AbstractSpell> getSpellList()
    {
        return spellList;
    }

    public void setSpellList(Hashtable<Integer, AbstractSpell> spellList)
    {
        this.spellList = spellList;
    }

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
        else if (qName.equalsIgnoreCase("name"))
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
