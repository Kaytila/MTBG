package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;


@Getter
@Setter
@Log4j2
@ToString
public class AnimatedRepresentationChanged extends ChangedEvent
{

    private LifeForm player;

    public AnimatedRepresentationChanged(LifeForm p)
    {
        this.player = p;
    }
}
