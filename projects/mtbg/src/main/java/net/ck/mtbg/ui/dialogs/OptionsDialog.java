package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.listeners.OptionsDialogChangeListener;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class OptionsDialog extends AbstractDialog
{
    public OptionsDialog(Frame owner, String title, boolean modal)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 300);
        this.setLayout(null);
        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        this.setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 300);
        panel.setLayout(null);
        this.setContentPane(panel);

        JSlider victoryWait = new JSlider(JSlider.HORIZONTAL,
                1, 15, GameConfiguration.getVictoryWait() / 1000);
        victoryWait.addChangeListener(new OptionsDialogChangeListener());


        victoryWait.setMajorTickSpacing(1);
        victoryWait.setMinorTickSpacing(1);
        victoryWait.setPaintTicks(true);
        victoryWait.setPaintLabels(true);
        victoryWait.setVisible(true);
        victoryWait.setBounds(0, 0, 300, 200);
        this.add(victoryWait);

        addButtons();
        this.setVisible(true);
    }
}
