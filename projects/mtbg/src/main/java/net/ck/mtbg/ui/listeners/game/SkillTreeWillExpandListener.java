package net.ck.mtbg.ui.listeners.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

@Log4j2
@Getter
@Setter
public class SkillTreeWillExpandListener implements TreeWillExpandListener
{

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
    {
        TreePath path = event.getPath();

    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
    {

    }
}
