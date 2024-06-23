package net.ck.mtbg.animation.missile;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.communication.graphics.MissilePositionChanged;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;

@Log4j2
@Getter
@Setter
@ToString
public class MissileTimerTask extends TimerTask
{
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private boolean running;


    @Override
    public void run()
    {
        if (Game.getCurrent().isRunning())
        {
            if (Game.getCurrent().getCurrentMap().getMissiles() != null)
            {
                if (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
                {
                    //logger.info("posting message");
                    setRunning(true);
                    //EventBus.getDefault().post(new MissilePositionChanged());
                    //TODO do calculation for missiles here actually instead of in Paint method
                    //Paint method will need to do only the drawing of missile at its correct place
                    //missile will need to know about everything
                    calculateMissile();
                }
                else
                {
                    setRunning(false);
                }
            }
            else
            {
                setRunning(false);
            }
        }
    }


    //TODO properly handle this - nice that we paint 60 frames, but missile will need to appear at least a little bit :D
    private void calculateMissile()
    {
        ArrayList<Missile> finishedMissiles = new ArrayList<>();
        if ((Game.getCurrent().getCurrentMap().getMissiles() == null) || (Game.getCurrent().getCurrentMap().getMissiles().isEmpty()))
        {
            return;
        }
        for (Missile m : Game.getCurrent().getCurrentMap().getMissiles())
        {
            if (m.getSourceCoordinates() == null)
            {
                logger.error("missile has no source");
                Game.getCurrent().stopGame();
            }
            //logger.info("m: {}", m);
            //logger.info("m image: {}", m.getAppearance().getStandardImage());
            if (m.getLine() == null)
            {
                if (m.getCurrentPosition() == null)
                {
                    m.setCurrentPosition(new Point(m.getSourceCoordinates().x, m.getSourceCoordinates().y));
                }
                m.setLine(MapUtils.getLine(m.getCurrentPosition(), m.getTargetCoordinates()));
            }

            if (m.getLine().size() == 0)
            {
                if (m.isSuccess())
                {
                    m.setStandardImage(ImageUtils.loadImage("combat", "explosion"));
                }
                //logger.info("finished missile");
                m.setFinished(true);
                finishedMissiles.add(m);
            }
            else
            {
                Point p = m.getLine().get(0);
                m.setCurrentPosition(p);

                if (m.getCurrentPosition().equals(m.getTargetCoordinates()))
                {
                    if (m.isSuccess())
                    {
                        m.setStandardImage(ImageUtils.loadImage("combat", "explosion"));
                    }
                    m.setFinished(true);
                    finishedMissiles.add(m);
                }

                //only paint missile every configured pixel
                for (int i = 0; i <= (GameConfiguration.skippedPixelsForDrawingMissiles - 1); i++)
                {
                    if (m.getLine().size() > 0)
                    {
                        m.getLine().remove(0);
                    }
                }

            }
        }

        if (finishedMissiles.size() > 0)
        {
            //logger.info("finished missiles: {}", finishedMissiles);
            Game.getCurrent().getCurrentMap().getMissiles().removeAll(finishedMissiles);
        }
        EventBus.getDefault().post(new MissilePositionChanged());
    }

}
