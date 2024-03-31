package net.ck.mtbg.backend.entities.skills;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.utils.ImageManager;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class AbstractSkill extends DefaultMutableTreeNode
{
    protected String name;
    protected boolean adjecient;
    protected boolean immediately;
    protected BufferedImage menuImage;
    protected int costs;
    protected BufferedImage actionImage;
    protected int id;
    protected int level;

    private TreeNode parent;

    @Override
    public TreeNode getChildAt(int childIndex)
    {
        return null;
    }

    @Override
    public int getChildCount()
    {
        return 0;
    }

    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node)
    {
        return -1;
    }

    @Override
    public boolean getAllowsChildren()
    {
        return false;
    }

    @Override
    public boolean isLeaf()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "AbstractSkill{" +
                "name='" + name + '\'' +
                ", adjecient=" + adjecient +
                ", immediately=" + immediately +
                ", menuImage=" + getMenuImage() +
                ", costs=" + costs +
                ", actionImage=" + actionImage +
                ", id=" + id +
                ", level=" + level +
                ", parent=" + parent +
                '}';
    }

    public BufferedImage getMenuImage()
    {
        return ImageManager.getSkillMenuImages().get(id);
    }
}

