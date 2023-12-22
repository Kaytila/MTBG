package net.ck.mtbg.ui.dialogs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.map.Message;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;

import java.awt.*;

/**
 * Factory class to create dialogs - has the added logic that it checks whether there already is a dialog open or
 * whether selectTile is active
 */
@Log4j2
@Getter
@Setter
public class DialogFactory
{
    public static AbstractDialog createDialog(Frame owner, String title, boolean modal, AbstractKeyboardAction action, Message message, LifeForm npc)
    {
        //redundant but just to be sure
        if (UIStateMachine.isDialogOpened() == true)
        {
            return null;
        }

        //redundant but just to be sure
        if (UIStateMachine.isSelectTile() == true)
        {
            return null;
        }


        UIStateMachine.setDialogOpened(true);
        switch (title)
        {
            case "Z-Stats":
            {
                //dialog.addButtons();
                return new StatsDialog(owner, title, modal);
            }

            case "Inventory":
            {
                //dialog.addButtons();
                return new InventoryDialog(owner, title, modal, action);
            }

            case "Equipment":
            {
                return new EQDialog(owner, title, modal);
            }

            case "Spellbook":
            {
                return new SpellDialog(owner, title, modal, action);
            }

            case "Message":
            {
                return new MessageDialog(owner, title, modal, message);
            }

            case "Talk":
            {
                return new TalkDialog(owner, title, modal, npc);
            }

            case "Skilltree":
            {
                return new Skilldialog(owner, title, modal, action);
            }

            default:
            {
                throw new IllegalArgumentException("not expected value during Dialog Creation: " + title);
            }
        }
    }
}
