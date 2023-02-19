package net.ck.game.ui.listeners;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.entities.LifeFormState;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.game.map.MapTile;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.UILense;
import net.ck.util.communication.graphics.AnimatedRepresentationChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AnimationSystemActionListener implements ActionListener
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private final Random rand = new Random();

    @Override
    public void actionPerformed(ActionEvent e)
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
                                p.setCurrentImage(ImageUtils.getBloodstainImage());
                            }
                            //if unconcious, stay unmoving
                            else if (p.getState().equals(LifeFormState.UNCONSCIOUS))
                            {
                                p.setCurrentImage(p.getAnimationImageList().get(0));
                            }
                            else// (p.getState().equals(LifeFormState.ALIVE))
                            {
                                p.setCurrentImage(p.getAnimationImageList().get(rand.nextInt(GameConfiguration.animationCycles)));
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