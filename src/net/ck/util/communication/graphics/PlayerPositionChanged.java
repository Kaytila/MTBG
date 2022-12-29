package net.ck.util.communication.graphics;

import net.ck.game.backend.entities.AbstractEntity;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerPositionChanged extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private AbstractEntity player;

    public PlayerPositionChanged(AbstractEntity p)
    {
        this.player = p;
    }

    public AbstractEntity getPlayer()
    {
        return this.player;
    }

    public void setPlayer(AbstractEntity player)
    {
        this.player = player;
    }
}
