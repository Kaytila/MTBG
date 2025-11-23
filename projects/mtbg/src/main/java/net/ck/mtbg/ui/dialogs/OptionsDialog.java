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
        //why does this need to be there?
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
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        /**
         * victory wait option
         */
        JPanel victoryPanel = new JPanel();
        victoryPanel.setLayout(new BoxLayout(victoryPanel, BoxLayout.X_AXIS));
        JLabel victoryLabel = new JLabel("Victory music time:");
        victoryPanel.add(victoryLabel);
        JSlider victoryWait = new JSlider(JSlider.HORIZONTAL, 1, 10, GameConfiguration.victoryWait / 1000);
        victoryWait.setToolTipText("victory");
        OptionsDialogChangeListener listener = new OptionsDialogChangeListener();
        victoryWait.addChangeListener(listener);
        victoryWait.setMajorTickSpacing(1);
        victoryWait.setMinorTickSpacing(1);
        victoryWait.setPaintTicks(true);
        victoryWait.setPaintLabels(true);
        victoryWait.setVisible(true);
        victoryPanel.add(victoryWait);
        panel.add(victoryPanel);

        /**
         * music volume option
         */

        JPanel volumePanel = new JPanel();
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.X_AXIS));
        JLabel volumeLabel = new JLabel("Volume:");
        volumePanel.add(volumeLabel);
        JSlider volumeWait = new JSlider(JSlider.HORIZONTAL, 0, 10, GameConfiguration.volume);
        volumeWait.setToolTipText("volume");
        volumeWait.addChangeListener(listener);
        volumeWait.setMajorTickSpacing(1);
        volumeWait.setMinorTickSpacing(1);
        volumeWait.setPaintTicks(true);
        volumeWait.setPaintLabels(true);
        volumeWait.setVisible(true);
        volumePanel.add(volumeWait);
        panel.add(volumePanel);


        JPanel loopMusicPanel = new JPanel();
        loopMusicPanel.setLayout(new BoxLayout(loopMusicPanel, BoxLayout.X_AXIS));
        JLabel loopMusicLabel = new JLabel("Loop Music:");
        JCheckBox loopMusicCheckbox = new JCheckBox();
        loopMusicCheckbox.setSelected(GameConfiguration.loopMusic);
        loopMusicCheckbox.addItemListener(listener);
        loopMusicPanel.add(loopMusicLabel);
        loopMusicPanel.add(loopMusicCheckbox);
        panel.add(loopMusicPanel);

        JPanel loopTitleMusicPanel = new JPanel();
        loopTitleMusicPanel.setLayout(new BoxLayout(loopTitleMusicPanel, BoxLayout.X_AXIS));
        JLabel loopTitleMusicLabel = new JLabel("Loop Title Music:");
        JCheckBox loopTitleMusicCheckbox = new JCheckBox();
        loopTitleMusicCheckbox.setSelected(GameConfiguration.loopTitleMusic);
        loopTitleMusicCheckbox.addItemListener(listener);
        loopTitleMusicPanel.add(loopTitleMusicLabel);
        loopTitleMusicPanel.add(loopTitleMusicCheckbox);
        panel.add(loopTitleMusicPanel);

        panel.add(addButtonswithLayout());
        for (Component comp : panel.getComponents())
        {
            ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        this.setVisible(true);
    }
}
