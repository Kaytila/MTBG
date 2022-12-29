package net.ck.game.ui.dialogs;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.items.ArmorPositions;
import net.ck.game.items.WeaponTypes;
import net.ck.game.ui.buttons.CancelButton;
import net.ck.game.ui.buttons.OKButton;
import net.ck.game.ui.components.EQPanel;
import net.ck.game.ui.components.InventoryImagePanel;
import net.ck.game.ui.components.WeaponPanel;
import net.ck.game.ui.dnd.EQPanelTransferHandler;
import net.ck.game.ui.listeners.EQPanelMouseListener;
import net.ck.game.ui.listeners.WindowClosingListener;
import net.ck.util.CodeUtils;
import net.ck.util.ImageUtils;
import net.ck.util.communication.keyboard.WindowClosingAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class EQDialog extends AbstractDialog
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


	public EQDialog(Frame owner, String title, boolean modal)
	{
		setTitle(title);
		this.setBounds(0, 0, GameConfiguration.dialogWidth, GameConfiguration.dialogHeight);
		this.setLayout(null);
		this.setLocationRelativeTo(owner);
		final WindowClosingAction dispatchClosing = new WindowClosingAction(this);
		final WindowClosingListener windowClosingListener = new WindowClosingListener();
		this.addWindowListener(windowClosingListener);
		root = this.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
		cancelButton = new CancelButton();
		okButton = new OKButton();

		okButton.setBounds(GameConfiguration.dialogWidth - 160, GameConfiguration.dialogHeight - 70, 70, 30);
		cancelButton.setBounds(GameConfiguration.dialogWidth - 90, GameConfiguration.dialogHeight - 70, 70, 30);
		this.add(cancelButton);
		this.add(okButton);

		int left = 0;
		int right = 0;
		int spacer = 35;

		/**
		 * Inventory Image Panel
		 */
		InventoryImagePanel inventoryImagePanel = new InventoryImagePanel();
		inventoryImagePanel.setBounds(((this.getWidth() / 2) -(ImageUtils.getInventoryImage().getWidth() / 2)) ,GameConfiguration.elipseSize, ImageUtils.getInventoryImage().getWidth(), ImageUtils.getInventoryImage().getHeight());
		inventoryImagePanel.setVisible(true);
		this.add(inventoryImagePanel);

		/**
		 * HEAD, TORSO, HANDS, WAIST, NECK, FEET, LEGS, FINGER, SHIELD, BACKPACK
		 */
		//top side
		EQPanel headPanel = new EQPanel();
		headPanel.setBounds(((GameConfiguration.dialogWidth /2) - GameConfiguration.elipseSize), 0, GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		headPanel.setArmorPosition(ArmorPositions.HEAD);
		headPanel.setToolTipText(headPanel.getArmorPosition().toString());
		//we do mousemotionlistener in the constructor because lazy.
		headPanel.addMouseListener(new EQPanelMouseListener(headPanel));
		headPanel.setTransferHandler(new EQPanelTransferHandler(headPanel));
		headPanel.setVisible(true);
		this.add(headPanel);

		//left side
		EQPanel neckPanel = new EQPanel();
		neckPanel.setBounds(0, (0 + GameConfiguration.elipseSize + (left * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		neckPanel.setArmorPosition(ArmorPositions.NECK);
		neckPanel.setToolTipText(neckPanel.getArmorPosition().toString());
		neckPanel.addMouseListener(new EQPanelMouseListener(neckPanel));
		neckPanel.setVisible(true);
		this.add(neckPanel);
		left++;

		EQPanel torsoPanel = new EQPanel();
		torsoPanel.setBounds(0, (0 + GameConfiguration.elipseSize + (left * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		torsoPanel.setArmorPosition(ArmorPositions.TORSO);
		torsoPanel.setToolTipText(torsoPanel.getArmorPosition().toString());
		torsoPanel.addMouseListener(new EQPanelMouseListener(torsoPanel));
		torsoPanel.setVisible(true);
		this.add(torsoPanel);
		left++;

		EQPanel shieldPanel = new EQPanel();
		shieldPanel.setBounds(0, (0 + GameConfiguration.elipseSize + (left * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		shieldPanel.setArmorPosition(ArmorPositions.SHIELD);
		shieldPanel.setToolTipText(shieldPanel.getArmorPosition().toString());
		shieldPanel.addMouseListener(new EQPanelMouseListener(shieldPanel));
		shieldPanel.setVisible(true);
		this.add(shieldPanel);
		left++;

		EQPanel fingerPanel = new EQPanel();
		fingerPanel.setBounds(0, (0 +  GameConfiguration.elipseSize + (left * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		fingerPanel.setArmorPosition(ArmorPositions.FINGER);
		fingerPanel.setToolTipText(fingerPanel.getArmorPosition().toString());
		fingerPanel.addMouseListener(new EQPanelMouseListener(fingerPanel));
		fingerPanel.setVisible(true);
		this.add(fingerPanel);

		//right side
		EQPanel backpackPanel = new EQPanel();
		backpackPanel.setBounds(GameConfiguration.dialogWidth - 60, (0 + GameConfiguration.elipseSize + (right * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		backpackPanel.setArmorPosition(ArmorPositions.BACKPACK);
		backpackPanel.setToolTipText(backpackPanel.getArmorPosition().toString());
		backpackPanel.addMouseListener(new EQPanelMouseListener(backpackPanel));
		backpackPanel.setVisible(true);
		this.add(backpackPanel);
		right++;

		EQPanel waistPanel = new EQPanel();
		waistPanel.setBounds(GameConfiguration.dialogWidth - 60, (0 + GameConfiguration.elipseSize + (right * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		waistPanel.setArmorPosition(ArmorPositions.WAIST);
		waistPanel.setToolTipText(waistPanel.getArmorPosition().toString());
		waistPanel.addMouseListener(new EQPanelMouseListener(waistPanel));
		waistPanel.setVisible(true);
		this.add(waistPanel);
		right++;

		EQPanel handPanel = new EQPanel();
		handPanel.setBounds(GameConfiguration.dialogWidth - 60, (0 + GameConfiguration.elipseSize + (right * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		handPanel.setArmorPosition(ArmorPositions.HANDS);
		handPanel.setToolTipText(handPanel.getArmorPosition().toString());
		handPanel.addMouseListener(new EQPanelMouseListener(handPanel));
		handPanel.setVisible(true);
		this.add(handPanel);
		right++;

		EQPanel legsPanel = new EQPanel();
		legsPanel.setBounds(GameConfiguration.dialogWidth - 60, (0 + GameConfiguration.elipseSize + (right * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		legsPanel.setArmorPosition(ArmorPositions.LEGS);
		legsPanel.setToolTipText(legsPanel.getArmorPosition().toString());
		legsPanel.addMouseListener(new EQPanelMouseListener(legsPanel));
		legsPanel.setVisible(true);
		this.add(legsPanel);
		right++;

		WeaponPanel weaponPanel = new WeaponPanel();
		weaponPanel.setBounds(GameConfiguration.dialogWidth - 60, (0 + GameConfiguration.elipseSize + (right * spacer)), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		weaponPanel.setWeaponType(WeaponTypes.MELEE);
		weaponPanel.setToolTipText(weaponPanel.getWeaponType().toString());
		weaponPanel.setVisible(true);
		this.add(weaponPanel);

		//bottom middle
		EQPanel feetPanel = new EQPanel();
		feetPanel.setBounds(((GameConfiguration.dialogWidth /2) - 25), GameConfiguration.dialogHeight - (70 + GameConfiguration.elipseSize), GameConfiguration.elipseSize, GameConfiguration.elipseSize);
		feetPanel.setArmorPosition(ArmorPositions.FEET);
		feetPanel.setToolTipText(feetPanel.getArmorPosition().toString());
		feetPanel.addMouseListener(new EQPanelMouseListener(feetPanel));
		feetPanel.setVisible(true);
		this.add(feetPanel);

		this.setVisible(true);
	}
}
