package net.ck.game.music;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * https://www.baeldung.com/java-play-sound
 * https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
 */
public class MusicPlayerJavaFX
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /* example code
    String audioFilePath = "AudioFileWithMp3Format.mp3";
SoundPlayerUsingJavaFx soundPlayerWithJavaFx = new SoundPlayerUsingJavaFx();
try {
    com.sun.javafx.application.PlatformImpl.startup(() -> {});
    Media media = new Media(
      soundPlayerWithJavaFx.getClass().getClassLoader().getResource(audioFilePath).toExternalForm());
    MediaPlayer mp3Player = new MediaPlayer(media);
    mp3Player.play();
} catch (Exception ex) {
    System.out.println("Error occured during playback process:" + ex.getMessage());
}
     */

    //MediaPlayer mediaPlayer = new MediaPlayer();
}
