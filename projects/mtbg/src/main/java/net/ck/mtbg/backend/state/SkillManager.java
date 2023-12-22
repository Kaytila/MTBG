package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;

import javax.swing.tree.MutableTreeNode;
import java.util.ArrayList;
import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class SkillManager
{
    @Getter
    @Setter
    private static Hashtable<Integer, AbstractSkill> skillList;


    public static Hashtable<Integer, AbstractSkill> getSkillsByLevelHashTable(int level)
    {
        Hashtable<Integer, AbstractSkill> hashtable = new Hashtable<>();

        for (int i : getSkillList().keySet())
        {
            AbstractSkill skill = getSkillList().get(i);

            if (skill.getLevel() == level)
            {
                hashtable.put(i, skill);

            }
        }

        return hashtable;
    }

    public static ArrayList<MutableTreeNode> getSkillsByLevel(int level)
    {
        ArrayList<MutableTreeNode> nodeArrayList = new ArrayList<>();
        for (int i : getSkillList().keySet())
        {
            AbstractSkill skill = getSkillList().get(i);

            if (skill.getLevel() == level)
            {
                logger.debug("adding skill:{} for level: {}", skill, level);
                nodeArrayList.add(skill);
            }
        }
        return nodeArrayList;
    }
}