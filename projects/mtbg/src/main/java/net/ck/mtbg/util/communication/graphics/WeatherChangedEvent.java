package net.ck.mtbg.util.communication.graphics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
@ToString
public class WeatherChangedEvent extends ChangedEvent
{
    public final String message;


    public WeatherChangedEvent(String message)
    {
        this.message = message;
    }

}
