package net.ck.mtbg.backend.state;

import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.soundeffects.SoundEffects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CommandSuccessMachine just gathers success or failure of the actions and
 * plays sound effects accordingly.
 * need to do NPC action here as well - and player.
 * That is why this is separated.
 */
public class CommandSuccessMachine
{
    private static final Logger logger = LogManager.getLogger(CommandSuccessMachine.class);


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
