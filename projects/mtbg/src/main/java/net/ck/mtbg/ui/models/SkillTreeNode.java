package net.ck.mtbg.ui.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class SkillTreeNode extends DefaultMutableTreeNode
{

    private TreeNode parent;
    private ArrayList<MutableTreeNode> children;
    private String name;
    private BufferedImage menuImage;

   /* @Override
    public TreeNode getChildAt(int childIndex)
    {
        return getChildren().get(childIndex);
    }

    @Override
    public int getChildCount()
    {
        if (getChildren() == null)
        {
            return 0;
        }
        return getChildren().size();
    }

    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node)
    {
        if (getChildren() != null)
        {
            return getChildren().indexOf(node);
        }
        return -1;
    }*/

    @Override
    public boolean getAllowsChildren()
    {
        return true;
    }

    @Override
    public boolean isLeaf()
    {
        return false;
    }


    public void setChildren(ArrayList<MutableTreeNode> nodes)
    {
        this.children = nodes;
        for (MutableTreeNode n : nodes)
        {
            this.add(n);
        }
    }

}
