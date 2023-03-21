package net.ck.mtbg.animation.lifeform;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.LifeForm;
import net.ck.mtbg.backend.entities.LifeFormState;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.ImageManager;
import net.ck.mtbg.util.UILense;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Random;
import java.util.TimerTask;

public class AnimationSystemTimerTask extends TimerTask
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private final Random rand = new Random();

    @Override
    public void run()
    {
        if (Game.getCurrent().isRunning() == true)
        {
            // random variant
            //logger.info("running");
            for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
            {
                for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
                {
                    MapTile tile = UILense.getCurrent().mapTiles[row][column];
                    if (tile != null)
                    {
                        LifeForm p = tile.getLifeForm();
                        if (p != null)
                        {
                            //logger.info("p: {}", p);
                            // if dead, stay corpse, or blood stain
                            if (p.getState().equals(LifeFormState.DEAD))
                            {
                                p.setCurrImage(ImageManager.getActionImage(ActionStates.KILL));
                            }
                            //if unconcious, stay unmoving
                            else if (p.getState().equals(LifeFormState.UNCONSCIOUS))
                            {
                                p.setCurrImage(0);
                            }
                            else// (p.getState().equals(LifeFormState.ALIVE))
                            {
                                p.setCurrImage(rand.nextInt(GameConfiguration.animationCycles));
                            }
                        }
                    }
                }
            }
            if (UIStateMachine.isUiOpen())
            {
                EventBus.getDefault().post(new AnimatedRepresentationChanged(null));
            }
        }
    }
}
