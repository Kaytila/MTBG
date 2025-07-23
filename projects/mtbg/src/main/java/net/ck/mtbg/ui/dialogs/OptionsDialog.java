package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.listeners.game.OptionsDialogChangeListener;
import net.ck.mtbg.util.communication.keyboard.framework.WindowClosingAction;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class OptionsDialog extends AbstractDialog
{
    public OptionsDialog(JFrame owner, String title, boolean modal)
    {
        setTitle(title);
        this.setBounds(0, 0, 300, 500);

        this.setLocationRelativeTo(owner);
        final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
        root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
        this.setUndecorated(true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setContentPane(panel);

        JPanel victoryPanel = new JPanel();
        victoryPanel.setLayout(new BoxLayout(victoryPanel, BoxLayout.X_AXIS));
        JLabel victoryLabel = new JLabel("Victory music time:");
        victoryPanel.add(victoryLabel);
        JSlider victoryWait = new JSlider(JSlider.HORIZONTAL, 1, 15, GameConfiguration.victoryWait / 1000);
        OptionsDialogChangeListener listener = new OptionsDialogChangeListener();
        victoryWait.addChangeListener(listener);
        victoryWait.setMajorTickSpacing(1);
        victoryWait.setMinorTickSpacing(1);
        victoryWait.setPaintTicks(true);
        victoryWait.setPaintLabels(true);
        victoryWait.setVisible(true);
        //victoryWait.setBounds(0, 0, 300, 200);
        victoryPanel.add(victoryWait);
        this.add(victoryPanel);

        JPanel loopMusicPanel = new JPanel();
        loopMusicPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        loopMusicPanel.setLayout(new BoxLayout(loopMusicPanel, BoxLayout.X_AXIS));
        JLabel loopMusicLabel = new JLabel("Loop Music:");
        JCheckBox loopMusicCheckbox = new JCheckBox();
        loopMusicCheckbox.setSelected(GameConfiguration.loopMusic);
        loopMusicCheckbox.addItemListener(listener);
        loopMusicPanel.add(loopMusicLabel);
        loopMusicPanel.add(loopMusicCheckbox);
        this.add(loopMusicPanel);

        JPanel loopTitleMusicPanel = new JPanel();
        loopTitleMusicPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        loopTitleMusicPanel.setLayout(new BoxLayout(loopTitleMusicPanel, BoxLayout.X_AXIS));
        JLabel loopTitleMusicLabel = new JLabel("Loop Title Music:");
        JCheckBox loopTitleMusicCheckbox = new JCheckBox();
        loopTitleMusicCheckbox.setSelected(GameConfiguration.loopTitleMusic);
        loopTitleMusicCheckbox.addItemListener(listener);
        loopTitleMusicPanel.add(loopTitleMusicLabel);
        loopTitleMusicPanel.add(loopTitleMusicCheckbox);
        this.add(loopTitleMusicPanel);


        for (Component comp : panel.getComponents())
        {
            ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        this.add(addButtonswithLayout());
        this.setVisible(true);
    }
}
