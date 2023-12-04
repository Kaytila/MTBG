package net.ck.mtbg.animation.background;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.BackgroundRepresentationChanged;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

@Log4j2
@Getter
@Setter
public class BackgroundAnimationSystemActionListener implements ActionListener
{
    private int currentBackgroundImage;

    private final Random rand = new Random();

    public BackgroundAnimationSystemActionListener()
    {
        setCurrentBackgroundImage(0);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Game.getCurrent().isRunning() == true)
        {
            if (GameConfiguration.animated == true)
            {
                setCurrentBackgroundImage((rand.nextInt(GameConfiguration.animationCycles)));
            }
            else
            {
                setCurrentBackgroundImage(0);
            }

            if (UIStateMachine.isUiOpen())
            {
                EventBus.getDefault().post(new BackgroundRepresentationChanged(getCurrentBackgroundImage()));
            }
        }
    }
}
