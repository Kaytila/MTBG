package net.ck.mtbg.util.communication.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.graphics.ChangedEvent;

@Log4j2
@Getter
@Setter
public class GameTimeChanged extends ChangedEvent
{
    final GameTimeChangeType type;

    public GameTimeChanged(GameTimeChangeType typ)
    {
        super();
        this.type = typ;
    }
}
