package net.ck.mtbg.music;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;


/**
 * https://www.baeldung.com/java-play-sound
 * https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html
 */
public class MusicPlayerJavaFX
{
    static private final Logger logger = LogManager.getLogger(MusicPlayerJavaFX.class);

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
    final CountDownLatch latch = new CountDownLatch(1);
    //static String mediaURI = GameConfiguration.musicPath + File.separator + "WORLD" + File.separator + "Ultima_remix_Traveling.aac";
    Media media;
    MediaPlayer mediaPlayer;

    public MusicPlayerJavaFX() throws InterruptedException, InvocationTargetException
    {
        //GraphicsSystem gs = new GraphicsSystem();
        //gs.startUp();

        Thread t = new Thread("my non EDT thread")
        {
            public void run()
            {
                //my work
                new JFXPanel(); // initializes JavaFX environ
                logger.debug("what the hell");
                latch.countDown();
            }

        };
        t.start();
        latch.await();
        String mediaURI = "file://localhost/Ultima_remix_Traveling.aac";
        //media = new Media(this.getClass().getClassLoader().getResource("Ultima_remix_Traveling.aac").toExternalForm());
        media = new Media(new File("Ultima_remix_Traveling.aac").toURI().toString());

    }

    public void play()
    {
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(1.0);
        mediaPlayer.setAutoPlay(true);
    }
}
