package net.ck.mtbg.ui.buttons.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.ui.WindowBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
@Log4j2
public class TitleScreenButton extends AbstractFancyButton
{

    public TitleScreenButton(int x, int y, String label)
    {
        super();
        this.label = label;
        setIcon(null);
        setBounds(x, y, GameConfiguration.preferredTitleButtonSize.width, GameConfiguration.preferredTitleButtonSize.height);
        setPreferredSize(GameConfiguration.preferredTitleButtonSize);
        setMinimumSize(GameConfiguration.preferredTitleButtonSize);
        setMaximumSize(GameConfiguration.preferredTitleButtonSize);
        this.setActionCommand(label);
        this.setAlignmentX(CENTER_ALIGNMENT);
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getActionCommand().equalsIgnoreCase("New Game"))
                {
                    logger.info("start game");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openGameUI();
                }

                if (e.getActionCommand().equalsIgnoreCase("Character Editor"))
                {
                    logger.info("start character editor");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openCharacterEditor();
                }

                if (e.getActionCommand().equalsIgnoreCase("Map Editor"))
                {
                    logger.info("start map editor");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openMapEditor();
                }
            }
        });
    }
}
