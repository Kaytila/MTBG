package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.dialogs.Skilldialog;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class SkillTree extends JTree
{
    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Skills");

    public SkillTree(Frame owner, Skilldialog skilldialog, AbstractKeyboardAction action)
    {
        super();
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setVisible(true);
        this.setFont(GameConfiguration.font);
        this.requestFocus();
        this.setRootVisible(false);
        this.expandRow(0);
        this.setVisibleRowCount(-1);
        setBounds(20, 40, 260, 100);
    }

    private void createNodes(DefaultMutableTreeNode top)
    {
        DefaultMutableTreeNode category;
        DefaultMutableTreeNode book;

        category = new DefaultMutableTreeNode("Books for Java Programmers");
        top.add(category);

        //original Tutorial
        book = new DefaultMutableTreeNode(Game.getCurrent().getCurrentPlayer().getSkills().get(0));
        category.add(book);

        book = new DefaultMutableTreeNode(Game.getCurrent().getCurrentPlayer().getSkills().get(1));
        category.add(book);

        book = new DefaultMutableTreeNode(Game.getCurrent().getCurrentPlayer().getSkills().get(2));
        category.add(book);
    }

}
