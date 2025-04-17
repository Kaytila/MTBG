package net.ck.mtbg.backend.applications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class Title
{
    /**
     * Singleton
     */
    private static final Title title = new Title();


    /**
     * Singleton access - now I can take away game in a lot of things :D
     */
    public static Title getCurrent()
    {
        return title;
    }
}
