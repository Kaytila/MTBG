package net.ck.mtbg.ui.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class CharacterPortraitColor
{
    private Color color;
    private String description;

    public CharacterPortraitColor()
    {
    }

    public static CharacterPortraitColor getCharacterPortraitColorFromColor(Color color)
    {
        CharacterPortraitColor cPC = new CharacterPortraitColor();
        cPC.setColor(color);
        cPC.setDescription(CharacterPortraitColor.createDescriptionFromColor(color));
        return cPC;
    }

    public static String createDescriptionFromColor(Color color)
    {
        if (color.equals(Color.BLACK))
        {
            return ("black");
        }
        else if (color.equals(Color.YELLOW))
        {
            return ("yellow");
        }
        else if (color.equals(Color.DARK_GRAY))
        {
            return ("grey");
        }
        else if (color.equals(Color.GREEN))
        {
            return ("green");
        }
        else if (color.equals(Color.BLUE))
        {
            return ("blue");
        }
        else if (color.equals(Color.RED))
        {
            return ("red");
        }
        return null;
    }
}
