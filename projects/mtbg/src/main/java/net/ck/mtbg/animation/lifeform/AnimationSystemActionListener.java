package net.ck.mtbg.animation.lifeform;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.AnimatedRepresentationChanged;
import net.ck.mtbg.util.utils.ImageManager;
import net.ck.mtbg.util.utils.UILense;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

@Log4j2
@Getter
@Setter
public class AnimationSystemActionListener implements ActionListener
{
    private final Random rand = new Random();

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Game.getCurrent().isRunning() == true)
        {
            if ((UIStateMachine.getUiState().equals(UIState.ACTIVATED)) || (UIStateMachine.getUiState().equals(UIState.OPENED)))
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
                                    p.setCurrImage(getRand().nextInt(GameConfiguration.animationCycles));
                                }
                            }
                        }
                    }
                }
            }

            if ((UIStateMachine.getUiState().equals(UIState.ACTIVATED)) || (UIStateMachine.getUiState().equals(UIState.OPENED)))
            {
                EventBus.getDefault().post(new AnimatedRepresentationChanged(null));
            }
        }
    }
}