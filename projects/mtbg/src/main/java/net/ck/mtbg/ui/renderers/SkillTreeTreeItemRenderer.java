package net.ck.mtbg.ui.renderers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.ui.models.SkillTreeNode;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class SkillTreeTreeItemRenderer extends JLabel implements TreeCellRenderer
{

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        JLabel label = new JLabel();


        if (value instanceof AbstractSkill)
        {
            AbstractSkill skill = (AbstractSkill) value;


            if (selected)
            {
                label.setBackground(Color.BLUE);
                label.setForeground(Color.YELLOW);
            }
            else
            {
                label.setBackground(Color.LIGHT_GRAY);
                label.setForeground(Color.BLACK);
            }


            if (skill.getMenuImage() != null)
            {
                ImageIcon icon = new ImageIcon(skill.getMenuImage());
                if (icon != null)
                {
                    label.setIcon(icon);
                }
            }
            label.setText(((AbstractSkill) value).getName());
        }
        else
        {
            label.setText(((SkillTreeNode) value).getName());
        }

        return label;
    }
}
