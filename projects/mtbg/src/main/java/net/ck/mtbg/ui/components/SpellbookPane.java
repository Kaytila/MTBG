package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.ui.dialogs.AbstractDialog;
import net.ck.mtbg.ui.listeners.SpellBookListener;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SpellbookPane extends JList<AbstractSpell>
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private Point selectionPoint;

	private Frame owner;

	public SpellbookPane(Frame owner, AbstractDialog dialog, AbstractKeyboardAction action)
	{
		super();
		this.setOwner(owner);
		this.setParentDialog(dialog);
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setVisible(true);
		this.setFont(GameConfiguration.font);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation(VERTICAL_WRAP);
		this.requestFocus();
		this.setVisibleRowCount(-1);
		setBounds(20, 40, 260, 100);
		SpellBookListener spellBookListener = new SpellBookListener(this, action);
		this.addMouseListener(spellBookListener);
		this.addMouseMotionListener(spellBookListener);
		this.setModel(Game.getCurrent().getCurrentPlayer().getSpells());
	}

	public Frame getOwner()
	{
		return owner;
	}

	public void setOwner(Frame owner)
	{
		this.owner = owner;
	}

	public AbstractDialog getParentDialog()
	{
		return parentDialog;
	}

	private AbstractDialog parentDialog;

	public void setParentDialog(AbstractDialog parentDialog)
	{
		this.parentDialog = parentDialog;
	}

	public Point getSelectionPoint()
	{
		return selectionPoint;
	}

	public void setSelectionPoint(Point selectionPoint)
	{
		this.selectionPoint = selectionPoint;
	}
}
