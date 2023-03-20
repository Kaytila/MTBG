package net.ck.mtbg.animation.background;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.graphics.BackgroundRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BackgroundAnimationSystemActionListener implements ActionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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

    public int getCurrentBackgroundImage()
    {
        return currentBackgroundImage;
    }

    public void setCurrentBackgroundImage(int currentBackgroundImage)
    {
        this.currentBackgroundImage = currentBackgroundImage;
    }
}
