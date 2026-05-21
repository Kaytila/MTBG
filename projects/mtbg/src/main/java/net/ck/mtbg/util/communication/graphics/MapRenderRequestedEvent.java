package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
@ToString
public class MapRenderRequestedEvent extends ChangedEvent
{
    private boolean fullRepaint;
    private String reason;

    public MapRenderRequestedEvent(boolean fullRepaint, String reason)
    {
        this.fullRepaint = fullRepaint;
        this.reason = reason;
    }
}
