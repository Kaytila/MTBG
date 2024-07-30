package net.ck.mtbg.music;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import org.greenrobot.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j2
@Getter
@Setter
public class MusicTimerActionListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (GameConfiguration.debugTimers == true)
        {
            logger.debug("firing game state change for music");
        }
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire new game state");
        }
        EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
    }
}
