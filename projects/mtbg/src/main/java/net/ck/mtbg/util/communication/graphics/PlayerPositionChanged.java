package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.AbstractEntity;

@Getter
@Setter
@Log4j2
@ToString
public class PlayerPositionChanged extends ChangedEvent
{

    private AbstractEntity player;

    public PlayerPositionChanged(AbstractEntity p)
    {
        this.player = p;
    }
}
