package net.ck.mtbg.ui.models.charactereditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.components.charactereditor.CharacterCanvas;
import net.ck.mtbg.ui.components.charactereditor.CharacterTinyCanvas;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class CharacterPortraitModel
{
    private String gender = "male";
    private CharacterPortraitColor eyeColor = CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLACK);
    private CharacterPortraitColor hairColor = CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLACK);
    private CharacterPortraitColor skinColor = CharacterPortraitColor.getCharacterPortraitColorFromColor(Color.BLACK);
    private CharacterCanvas characterCanvas;
    private CharacterTinyCanvas characterTinyCanvas;

    public void updateUI()
    {
        characterCanvas.paint();
        characterTinyCanvas.paint();
    }

}
