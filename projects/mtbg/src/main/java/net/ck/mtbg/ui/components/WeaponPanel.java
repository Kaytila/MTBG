package net.ck.mtbg.ui.components;

import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class WeaponPanel extends JComponent implements WeaponPanelWeaponType
{

    private final Logger      logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private       WeaponTypes weaponType;

    @Override
    public WeaponTypes getWeaponType()
    {
        return weaponType;
    }

    @Override
    public void setWeaponType(WeaponTypes weaponType)
    {
        this.weaponType = weaponType;
    }
}
