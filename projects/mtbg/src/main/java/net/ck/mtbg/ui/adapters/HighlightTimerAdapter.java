package net.ck.mtbg.ui.adapters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.application.port.HighlightPort;
import net.ck.mtbg.ui.highlighting.HighlightTimer;

@Log4j2
@Getter
@Setter
public final class HighlightTimerAdapter implements HighlightPort
{
    private final HighlightTimer timer;

    public HighlightTimerAdapter(HighlightTimer timer)
    {
        this.timer = timer;
    }

    @Override
    public void start()
    {
        timer.start();
    }

    @Override
    public void stop()
    {
        timer.stop();
    }

    @Override
    public void pause()
    {
        timer.setPaused(true);
        timer.stop();
    }

    @Override
    public void resume()
    {
        timer.setPaused(false);
        timer.start();
    }

    @Override
    public boolean isRunning()
    {
        return timer.isRunning();
    }
}