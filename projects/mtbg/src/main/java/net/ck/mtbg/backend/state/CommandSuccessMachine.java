package net.ck.mtbg.backend.state;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.AbstractAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.soundeffects.SoundEffects;
import net.ck.mtbg.soundeffects.SoundPlayerNoThread;

/**
 * CommandSuccessMachine just gathers success or failure of the actions and
 * plays sound effects accordingly.
 * need to do NPC action here as well - and player.
 * That is why this is separated.
 */
@Log4j2
public class CommandSuccessMachine
{
    /**
     * Play a sound effect based on success/failure outcome.
     *
     * @param player       sound player instance
     * @param success      true to play success effect, false for failure effect
     * @param successSound sound to play on success
     * @param failureSound sound to play on failure
     */
    private static void playActionSound(SoundPlayerNoThread player, boolean success, SoundEffects successSound, SoundEffects failureSound)
    {
        if (player == null)
        {
            return;
        }
        player.playSoundEffect(success ? successSound : failureSound);
    }

    public static void calculateSoundEffect(AbstractAction action)
    {
        if (action == null)
        {
            return;
        }

        logger.debug("Action success: {}", action.isSuccess());
        if (!GameConfiguration.playSound)
        {
            return;
        }

        var player = NoiseManager.getSoundPlayerNoThread();
        if (player == null)
        {
            logger.warn("Sound player not initialized");
            return;
        }

        switch (action.getType())
        {
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
            case ENTER:
                playActionSound(player, action.isSuccess(), SoundEffects.WALK, SoundEffects.BLOCKED);
                break;
            case ATTACK:
                playActionSound(player, action.isSuccess(), SoundEffects.HIT, SoundEffects.ATTACK);
                break;
            case JIMMY:
            case OPEN:
                playActionSound(player, action.isSuccess(), SoundEffects.HIT, SoundEffects.BLOCKED);
                break;
            default:
                logger.debug("No sound effect mapped for action type: {}", action.getType());
        }
    }

    public static void calculateSoundEffectNPC(AbstractAction action)
    {
        if (action == null)
        {
            return;
        }

        if (!GameConfiguration.playSound)
        {
            return;
        }

        var player = NoiseManager.getSoundPlayerNoThread();
        if (player == null)
        {
            logger.warn("Sound player not initialized");
            return;
        }

        switch (action.getType())
        {
            case ATTACK:
                playActionSound(player, action.isSuccess(), SoundEffects.HIT, SoundEffects.ATTACK);
                break;
            case CAST:
                playActionSound(player, action.isSuccess(), SoundEffects.HIT, SoundEffects.CAST);
                break;
            default:
                logger.debug("No sound effect mapped for NPC action type: {}", action.getType());
        }
    }
}
