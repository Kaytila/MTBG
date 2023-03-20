package net.ck.mtbg.ui.listeners;

import net.ck.mtbg.backend.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.dialogs.TalkDialog;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputFieldListener implements ActionListener
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private JTextField inputField;
    private JTextArea  textArea;
    private LifeForm   npc;
    private TalkDialog talkDialog;
	private boolean endDialog;

    public InputFieldListener(TalkDialog dialog, JTextField textField, JTextArea textArea, LifeForm n)
    {
        setInputField(textField);
        setTextArea(textArea);
        setTalkDialog(dialog);
        setNpc(n);
    }

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(getInputField()))
		{
			logger.info("enter pressed");
			try
			{
				String question = getInputField().getDocument().getText(0, getInputField().getDocument().getLength());

				if (question.length() == 0 && endDialog == true)
				{
					WindowClosingAction close = new WindowClosingAction(getTalkDialog());
					close.actionPerformed(null);
				}

				getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "You ask: " + question + "?" + "\n", null);
				getInputField().getDocument().remove(0, getInputField().getDocument().getLength());

				String response = Game.getCurrent().getCurrentPlayer().talk(npc, question);
				if (response != null)
				{
					getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + response + "\n", null);
				}

				if (question.equalsIgnoreCase("bye"))
				{
					endDialog = true;
				}

				if (response == null)
				{
					getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + "Hu?" + "\n", null);
				}

			} catch (BadLocationException e1)
			{

				e1.printStackTrace();
			}
		}
	}


	public JTextField getInputField()
	{
		return inputField;
	}

	public void setInputField(JTextField inputField)
	{
		this.inputField = inputField;
	}

	public JTextArea getTextArea()
	{
		return textArea;
	}

	public void setTextArea(JTextArea textArea)
	{
		this.textArea = textArea;
	}

    public LifeForm getNpc()
    {
        return npc;
    }

    public void setNpc(LifeForm npc)
    {
        this.npc = npc;
    }

	public TalkDialog getTalkDialog()
	{
		return talkDialog;
	}

	public void setTalkDialog(TalkDialog talkDialog)
	{
		this.talkDialog = talkDialog;
	}
}
