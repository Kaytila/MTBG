package net.ck.mtbg.animation.lifeform;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ActionStates;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.util.utils.ImageManager;

import java.util.TimerTask;

@Log4j2
public class HitMissImageTimerTask extends TimerTask
{
    private boolean running;
    @Getter
    @Setter
    private LifeForm lifeForm;

    public HitMissImageTimerTask(LifeForm n)
    {
        setLifeForm(n);
        setRunning(true);
        TimerManager.setHitMissInFlight(true); // Anzeige ist geplant/aktiv
    }

    @Override
    public void run()
    {
        try
        {
            setRunning(false);
            if (getLifeForm().getState().equals(LifeFormState.DEAD))
            {
                getLifeForm().setCurrImage(ImageManager.getActionImage(ActionStates.KILL));
            }
            else
            {
                getLifeForm().setCurrImage(0);
            }

            if (GameConfiguration.useRenderClock)
            {
                Game.getCurrent().getRenderClock().markDirty();
            }
        }
        finally
        {
            TimerManager.setHitMissInFlight(false); // jetzt darf Input wieder durch
        }
    }

    public synchronized boolean isRunning()
    {
        return running;
    }

    public synchronized void setRunning(boolean running)
    {
        this.running = running;
    }
}
