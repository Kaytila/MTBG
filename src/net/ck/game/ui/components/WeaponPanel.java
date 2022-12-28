package net.ck.game.ui.components;

import net.ck.game.items.WeaponTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Objects;

public class WeaponPanel extends JComponent implements WeaponPanelWeaponType
{

    private final Logger logger = LogManager.getLogger(getRealClass());
    private WeaponTypes weaponType;

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }

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
