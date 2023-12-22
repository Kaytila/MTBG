package net.ck.mtbg.ui.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.SkillManager;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

@Log4j2
@Getter
@Setter
public class SkillTreeDataModel implements TreeModel
{


    SkillTreeNode root;

    //TODO
    //the level nodes are not skills just nodes
    //the skills are children of each node depending on their level
    public SkillTreeDataModel()
    {
        root = new SkillTreeNode();
        root.setParent(null);
        root.setName("root");


        SkillTreeNode level1 = new SkillTreeNode();

        level1.setParent(root);
        level1.setName("Level 1");
        level1.setChildren(SkillManager.getSkillsByLevel(1));

        root.add(level1);

        SkillTreeNode level2 = new SkillTreeNode();

        level2.setParent(level1);
        level2.setName("Level 2");
        level2.setChildren(SkillManager.getSkillsByLevel(2));

        root.add(level2);

        SkillTreeNode level3 = new SkillTreeNode();

        level3.setParent(level2);
        level3.setName("Level 3");
        level3.setChildren(SkillManager.getSkillsByLevel(3));

        root.add(level3);

    }

    @Override
    public TreeNode getRoot()
    {
        if (root != null)
        {
            return root;
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        return ((SkillTreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent)
    {
        return ((DefaultMutableTreeNode) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        return ((SkillTreeNode) parent).getChildren().indexOf(child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {

    }

}
