package net.ck.game.ui.listeners;

import net.ck.game.backend.entities.NPC;
import net.ck.game.ui.dialogs.TalkDialog;
import net.ck.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class InputFieldListener implements ActionListener
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	private JTextField inputField;
	private JTextArea textArea;
	private NPC npc;
	private TalkDialog talkDialog;
	private boolean endDialog;
	
	public InputFieldListener(TalkDialog dialog, JTextField textField, JTextArea textArea, NPC n)
	{
		setInputField(textField);
		setTextArea(textArea);
		setTalkDialog(dialog);
		setNpc(n);
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
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

				//EventBus.getDefault().post(new TalkInputEvent(getInputField().getDocument().getText(0, getInputField().getDocument().getLength())));
				getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "You ask: " + getInputField().getDocument().getText(0, getInputField().getDocument().getLength()) + "?" + "\n", null);				
				getInputField().getDocument().remove(0, getInputField().getDocument().getLength());
				boolean found = false;
				for (String q : getNpc().getMobasks().keySet())
				{
					if (question.equalsIgnoreCase(q))
					{
						found = true;
						getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + getNpc().getMobasks().get(q) + "\n", null);
						if (question.equalsIgnoreCase("bye"))
						{
							endDialog = true;
						}
					}
				}
				
				if (found == false)
				{
					getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + "Hu?" + "\n", null);
				}
				
			}
			catch (BadLocationException e1)
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

	public NPC getNpc()
	{
		return npc;
	}

	public void setNpc(NPC npc)
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
