package net.ck.mtbg.backend.state;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.soundeffects.SoundEffects;

/**
 * CommandSuccessMachine just gathers success or failure of the actions and
 * plays sound effects accordingly.
 * need to do NPC action here as well - and player.
 * That is why this is separated.
 */
@Log4j2
public class CommandSuccessMachine
{
    public static void calculateSoundEffect(AbstractAction action)
    {
        if (GameConfiguration.playSound == true)
        {
            switch (action.getType())
            {
                case NORTH:
                case EAST:
                case SOUTH:
                case WEST:
                case ENTER:
                    if (action.isSuccess())
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.WALK);
                    }
                    else
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.BLOCKED);
                    }
                    break;
                case ATTACK:
                    if (action.isSuccess())
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.HIT);
                    }
                    else
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.ATTACK);
                    }
                    break;
                default:
                    //logger.info("do nothing");
            }
        }
    }

    public static void calculateSoundEffectNPC(AbstractAction action)
    {
        if (GameConfiguration.playSound == true)
        {
            switch (action.getType())
            {
                case ATTACK:
                    if (action.isSuccess())
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.HIT);
                    }
                    else
                    {
                        NoiseManager.getSoundPlayerNoThread().playSoundEffect(SoundEffects.ATTACK);
                    }
                    break;
                default:
                    //logger.info("do nothing");
            }
        }
    }
}
