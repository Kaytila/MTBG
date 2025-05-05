package net.ck.mtbg.ui.components.game;

import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Log4j2
@Getter
@Setter
public class InputField extends JTextField
{
    private ArrayList<String> contents = new ArrayList<>();

    /**
     * create the input field, stupid settings to find it on the screen
     */
    public InputField()
    {
        super();
        Border blackline = BorderFactory.createLineBorder(Color.red);
        this.setBounds(GameConfiguration.UIwidth - 200, 580, 150, GameConfiguration.lineHight);

        this.setVisible(true);
        this.setFont(GameConfiguration.font);
        this.setFocusable(false);
        this.setBorder(blackline);
        //this.setToolTipText(getLogger().getName());
        logger.info("created input field for command history");
    }

    /**
     * add the last command to the command list (not sure whether getting it from the Game.currentPlayer() wouldnt be better)
     */
    public void setText(String t)
    {
        contents.add(t);
        super.setText(t);
    }

    /**
     * retract the last turn, remove the last entry in the command window.
     * <a href="https://stackoverflow.com/questions/687833/how-to-get-the-last-value-of-an-arraylist">https://stackoverflow.com/questions/687833/how-to-get-the-last-value-of-an-arraylist</a>
     */
    public void retractTurn()
    {

        //for (String t : contents)
        //{
        //logger.info("input field contents: {}", t);
        //}

        String lastElement;
        try
        {
            lastElement = Iterables.getLast(contents);
        }
        catch (NoSuchElementException e)
        {
            lastElement = "";
        }
        contents.remove(lastElement);
        try
        {
            lastElement = Iterables.getLast(contents);
        }
        catch (NoSuchElementException e)
        {
            lastElement = "";
        }
        super.setText(lastElement);
    }

}
