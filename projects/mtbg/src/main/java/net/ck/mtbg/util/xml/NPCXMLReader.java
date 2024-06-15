package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.attributes.*;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.queuing.ScheduleActivity;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.util.communication.keyboard.MoveAction;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.util.Hashtable;

/**
 * <npc>
 * <id>1</id>
 * <type>WARRIOR</type>
 * <x>3</x>
 * <y>2</y>
 * <mobasks>
 * <mobask>
 * <question>Hello</question>
 * <answer>Hello, how do you do?</answer>
 * </mobask>
 * <mobask>
 * <question>Name</question>
 * <answer>My name is orc</answer>
 * </mobask>
 * <mobask>
 * <question>job</question>
 * <answer>I am an orc warrior!</answer>
 * </mobask>
 * <mobask>
 * <question>bye</question>
 * <answer>Ort!</answer>
 * </mobask>
 * </mobasks>
 * </npc>
 */
@Getter
@Setter
@Log4j2
public class NPCXMLReader extends DefaultHandler
{

    private Hashtable<Integer, NPC> npcs;
    private StringBuilder data;
    private Hashtable<String, String> mobasks;
    private NPC n;
    private String question;
    private String answer;

    private Attributes attr;
    private boolean sched;
    private Schedule schedule;
    private ScheduleActivity scheduleActivity;
    private GameTime startTime;
    private boolean npc;

    private boolean targetPosition;
    private Point targetPos;

    private boolean mapPosition;
    private Point mapPos;

    @Override
    public void startDocument()
    {
        npcs = new Hashtable<>();

    }

    @Override
    public void endDocument()
    {

    }


    /**
     * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
     */
    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
    {
        switch (qName)
        {
            case "npcs":
                npcs = new Hashtable<>();
                break;
            case "npc":
                npc = true;
                n = new NPC();
                break;
            case "id":
                break;
            case "type":
                break;
            case "x":
                break;
            case "y":
                break;
            case "mobasks":
                mobasks = new Hashtable<>();
                break;
            case "mobask":
                break;
            case "question":
                break;
            case "answer":
                break;
            case "attributes":
                attr = new net.ck.mtbg.backend.entities.attributes.Attributes();
                break;
            case "dexterity":
                break;
            case "strength":
                break;
            case "constitution":
                break;
            case "intelligence":
                break;
            case "mapPosition":
                mapPosition = true;
                mapPos = new Point();
                break;
            case "schedule":
                sched = true;
                schedule = new Schedule(n);
                break;
            case "scheduleActivity":
                scheduleActivity = new ScheduleActivity();
                break;
            case "scheduleActivityString":
                break;
            case "startTime":
                startTime = new GameTime();
                break;
            case "targetPosition":
                targetPosition = true;
                targetPos = new Point();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
        data = new StringBuilder();
    }

    /**
     * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
     */
    @Override
    public void endElement(String uri, String localName, String qName)
    {
        switch (qName)
        {
            case "npcs":
                break;
            case "npc":
                //n.initialize();
                npcs.put(n.getId(), n);
                break;
            case "id":
                n.setId(Integer.parseInt(data.toString()));
                break;
            case "mapPosition":
                logger.debug("map position: {}", mapPos);
                n.setMapPosition(mapPos);
                n.setOriginalMapPosition(new Point(mapPos.x, mapPos.y));
                mapPosition = false;
                break;
            case "type":
                n.setType(NPCType.valueOf(data.toString()));
                logger.info("npc type: {}", n.getType());
                break;
            case "x":
                if (npc)
                {
                    if (mapPosition)
                    {
                        mapPos.x = Integer.parseInt(data.toString());
                    }
                    if (targetPosition)
                    {
                        targetPos.x = Integer.parseInt(data.toString());
                    }
                }
                break;
            case "y":
                if (npc)
                {
                    if (mapPosition)
                    {
                        mapPos.y = Integer.parseInt(data.toString());
                    }
                    if (targetPosition)
                    {
                        targetPos.y = Integer.parseInt(data.toString());
                    }
                }
                /*if (n.getMapPosition() == null)
                {
                    Point pos = new Point();
                    pos.y = Integer.parseInt(data.toString());
                    n.setMapPosition(pos);
                }
                else
                {
                    n.setMapPosition(new Point(n.getMapPosition().x, Integer.parseInt(data.toString())));
                }*/
                break;
            case "question":
                question = (data.toString());
                break;
            case "answer":
                answer = (data.toString());
                break;
            case "mobask":
                mobasks.put(question, answer);
            case "mobasks":
                n.setMobasks(mobasks);
                break;
            case "strength":
                Strength str = new Strength();
                str.setValue(Integer.parseInt(data.toString()));
                break;
            case "dexterity":
                Dexterity dex = new Dexterity();
                dex.setValue(Integer.parseInt(data.toString()));
                break;
            case "constitution":
                Constitution con = new Constitution();
                con.setValue(Integer.parseInt(data.toString()));
                break;
            case "intelligence":
                Intelligence inte = new Intelligence();
                inte.setValue(Integer.parseInt(data.toString()));
                break;
            case "attributes":
                n.setAttributes(attr);
                break;
            case "startTime":
                String[] strings = data.toString().split(":");
                logger.info("strings: {} - {}", strings[0], strings[1]);
                startTime.setCurrentHour(Integer.parseInt(strings[0]));
                startTime.setCurrentMinute(Integer.parseInt(strings[1]));
                scheduleActivity.setStartTime(startTime);
                break;
            case "scheduleActivityString":
                scheduleActivity.setScheduleActivityString(data.toString());
                break;
            case "scheduleActivity":
                schedule.getActivities().add(scheduleActivity);
                break;
            case "schedule":
                schedule.setActive(true);
                n.setSchedule(schedule);
                sched = false;
                break;
            case "targetPosition":
                if (sched)
                {
                    logger.info("setting schedule");
                    scheduleActivity.setTargetLocation(targetPos);
                    MoveAction moveAction = new MoveAction();
                    moveAction.setGetWhere(targetPos);
                    scheduleActivity.setAction(moveAction);
                }
                else
                {
                    n.setOriginalTargetMapPosition(targetPos);
                    n.setTargetMapPosition(targetPos);
                    n.setPatrolling(true);
                    logger.info("targetPosition and patrolling set for id: {}", n.getId());
                }
                targetPosition = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
        data = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        if (data == null)
        {
            data = new StringBuilder();
        }
        else
        {
            data.append(ch, start, length);
        }
    }

}
