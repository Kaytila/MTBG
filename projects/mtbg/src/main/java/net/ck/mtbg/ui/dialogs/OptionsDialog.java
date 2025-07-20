package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.listeners.game.OptionsDialogChangeListener;
import net.ck.mtbg.util.communication.keyboard.framework.WindowClosingAction;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public class OptionsDialog extends AbstractDialog
{
    public OptionsDialog(JFrame owner, String title, boolean modal)
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

        JSlider victoryWait = new JSlider(JSlider.HORIZONTAL, 1, 15, GameConfiguration.victoryWait / 1000);
        OptionsDialogChangeListener listener = new OptionsDialogChangeListener();
        victoryWait.addChangeListener(listener);


        victoryWait.setMajorTickSpacing(1);
        victoryWait.setMinorTickSpacing(1);
        victoryWait.setPaintTicks(true);
        victoryWait.setPaintLabels(true);
        victoryWait.setVisible(true);
        victoryWait.setBounds(0, 0, 300, 200);
        this.add(victoryWait);

        JCheckBox loopMusic = new JCheckBox("Loop Music");
        loopMusic.setBounds(0, 300, 50, 50);
        loopMusic.addItemListener(listener);
        this.add(loopMusic);


        addButtons();
        this.setVisible(true);
    }
}
