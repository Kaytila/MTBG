package net.ck.mtbg.ui.models;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.CharacterCanvas;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class CharacterPortraitModel
{
    private String gender = "male";
    private Color eyeColor = Color.BLACK;
    private Color hairColor = Color.BLACK;
    private Color skinColor = Color.BLACK;
    private CharacterCanvas characterCanvas;

    public void updateUI()
    {
        characterCanvas.paint();
    }

}
