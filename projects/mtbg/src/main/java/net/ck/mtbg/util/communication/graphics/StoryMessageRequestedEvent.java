package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.Message;

@Getter
@Setter
@Log4j2
@ToString
public class StoryMessageRequestedEvent extends ChangedEvent
{
    private Message message;
    private MapTile tile;

    public StoryMessageRequestedEvent(Message message, MapTile tile)
    {
        this.message = message;
        this.tile = tile;
    }
}
