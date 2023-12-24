package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class SkillXMLReader extends DefaultHandler
{
    private StringBuilder data = null;

    private boolean sp;
    private boolean name;
    private boolean id;
    private Hashtable<Integer, AbstractSkill> skillList;
    private AbstractSkill skill;

    private boolean lvl;


    @Override
    public void startDocument()
    {
        skillList = new Hashtable<>();
    }

    @Override
    public void endDocument()
    {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        if (qName.equalsIgnoreCase("skills"))
        {

        }
        else if (qName.equalsIgnoreCase("skill"))
        {
            sp = true;
            skill = new AbstractSkill();
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
        if (qName.equalsIgnoreCase("skills"))
        {

        }
        else if (qName.equalsIgnoreCase("skill"))
        {
            sp = false;
            skillList.put(skill.getId(), skill);
        }

        else if (qName.equalsIgnoreCase("id"))
        {
            id = false;
            skill.setId(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("name"))
        {
            name = false;
            skill.setName(data.toString());
        }

        else if (qName.equalsIgnoreCase("level"))
        {
            lvl = false;
            skill.setLevel(Integer.parseInt(data.toString()));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }
}
