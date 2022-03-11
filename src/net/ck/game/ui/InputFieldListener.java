package net.ck.game.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.game.backend.entities.NPC;
import net.ck.util.communication.graphics.TalkInputEvent;

public class InputFieldListener implements ActionListener
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private JTextField inputField;
	private JTextArea textArea;
	private NPC npc;
	
	public InputFieldListener(JTextField textField, JTextArea textArea, NPC n)
	{
		setInputField(textField);
		setTextArea(textArea);
		setNpc(n);
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
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
				//EventBus.getDefault().post(new TalkInputEvent(getInputField().getDocument().getText(0, getInputField().getDocument().getLength())));
				getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "You ask: " + getInputField().getDocument().getText(0, getInputField().getDocument().getLength()) + "?" + "\n", null);				
				getInputField().getDocument().remove(0, getInputField().getDocument().getLength());
				boolean found = false;
				for (String q : getNpc().getMobasks().keySet())
				{		
					logger.info("question: {}, q: {}", question, q);
					if (question.equalsIgnoreCase(q))
					{
						found = true;
						getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + getNpc().getMobasks().get(q) + "\n", null);
					}
				}
				
				if (found != false)
				{
					getTextArea().getDocument().insertString(getTextArea().getDocument().getLength(), "NPC says: " + "Hu?" + "\n", null);
				}
				
			}
			catch (BadLocationException e1)
			{
				// TODO Auto-generated catch block
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
}
