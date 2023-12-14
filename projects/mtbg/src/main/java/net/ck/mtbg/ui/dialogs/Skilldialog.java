package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.listeners.WindowClosingListener;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
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
    public Skilldialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action)
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


        addButtons();
        this.setVisible(true);
    }
}
