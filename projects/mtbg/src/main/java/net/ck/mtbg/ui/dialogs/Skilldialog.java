package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.game.SkillTree;
import net.ck.mtbg.ui.listeners.game.*;
import net.ck.mtbg.ui.models.game.SkillTreeDataModel;
import net.ck.mtbg.ui.renderers.game.SkillTreeTreeItemRenderer;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

@Log4j2
@Getter
@Setter
/**
 * Skilldialog - shows the skill tree, however that will look like in Detail.
 * Will be a tree, wondering what kind of control to use for that. hyperbolic one? just a manually drawn one?
 */
public class Skilldialog extends AbstractDialog
{
    public Skilldialog(JFrame owner, String title, boolean modal, AbstractKeyboardAction action)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(new GridLayout(1, 0));
        this.setLocationRelativeTo(owner);
        this.setModal(modal);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);

        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);

        final WindowClosingListener windowClosingListener = new WindowClosingListener();
        this.addWindowListener(windowClosingListener);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 300);
        panel.setLayout(null);
        this.setContentPane(panel);
        this.setUndecorated(true);

        final SkillTree skillTree = new SkillTree(owner, this, action);
        final SkillTreeDataModel skillTreeDataModel = new SkillTreeDataModel();


        skillTree.setEditable(false);
        skillTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        skillTree.setShowsRootHandles(true);

        skillTree.setModel(skillTreeDataModel);
        skillTree.setCellRenderer(new SkillTreeTreeItemRenderer());
        skillTree.addTreeExpansionListener(new SkillTreeExpansionListener());
        skillTree.addTreeSelectionListener(new SkillTreeSelectionListener());
        skillTree.addTreeWillExpandListener(new SkillTreeWillExpandListener());
        skillTree.addMouseListener(new SkillTreeMouseListener());
        skillTree.setVisible(true);
        panel.add(skillTree);
        addButtons();
        this.setVisible(true);
    }
}
