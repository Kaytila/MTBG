package net.ck.mtbg.ui.adapters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.application.port.TimerPort;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public final class TimerAdapter implements TimerPort
{
    private final Timer timer;

    public TimerAdapter(Timer timer)
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
    public boolean isRunning()
    {
        return timer.isRunning();
    }
}
