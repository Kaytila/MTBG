package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.items.WeaponTypes;

import javax.swing.*;

@Log4j2
@Getter
@Setter
public class WeaponPanel extends JComponent implements WeaponPanelWeaponType
{
    private WeaponTypes weaponType;

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
