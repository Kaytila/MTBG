package net.ck.mtbg.ui.adapters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.application.port.GameEventPublisher;
import org.greenrobot.eventbus.EventBus;

@Log4j2
@Getter
@Setter
public final class GameEventBusPublisher implements GameEventPublisher
{
    @Override
    public void publish(Object event)
    {
        EventBus.getDefault().post(event);
    }
}
