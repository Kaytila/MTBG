package net.ck.mtbg.ui.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

@Log4j2
@Getter
@Setter
public class SkillTreeSelectionListener implements TreeSelectionListener
{

    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

        logger.debug("node: {}", node);
        if (node.getChildCount() == 0)
        {
            node.setAllowsChildren(false);
        }


    }
}
