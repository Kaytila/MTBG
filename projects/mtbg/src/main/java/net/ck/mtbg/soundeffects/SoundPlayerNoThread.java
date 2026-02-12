package net.ck.mtbg.soundeffects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.utils.SoundUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Log4j2
@ToString
/**
 * plays the sound effects based on
 * - action
 * - success indicated by commandsuccessmachine
 *
 * <b>known bug:</b>
 * the first sound effect that is not walking will not work properly.
 * if you walk first, then it will work as it should.
 * Something somewhere is off, but based at least on the debug information,
 * it is the right sample that is being played. It just arrives broken.
 */
public class SoundPlayerNoThread
{
    private final Map<SoundEffects, Clip> clipCache = new EnumMap<>(SoundEffects.class);
    AudioInputStream audioInputStream = null;
    private ArrayList<Path> soundEffects;
    private Hashtable<SoundEffects, File> effectsList;
    private Clip currentSound;


    public SoundPlayerNoThread()
    {
        super();
        soundEffects = new ArrayList<>();
        effectsList = new Hashtable<>(SoundEffects.values().length);
        readSoundEffectDirectory(GameConfiguration.soundeffectsPath);
        preloadAllClips();

    }

    private void preloadAllClips()
    {
        for (SoundEffects effect : SoundEffects.values())
        {
            File f = effectsList.get(effect);
            if (f == null)
            {
                logger.warn("No file mapped for effect {}", effect);
                continue;
            }

            try
            {
                Clip clip = Clip clip = loadClipAsPcmWithFade(f, 44100f, 2, 5.0); // 5ms Fade-In
                clipCache.put(effect, clip);
                logger.debug("Preloaded {} from {}", effect, f.getName());
                primeClipSilent(clip);
            }
            catch (Exception e)
            {
                logger.error("Failed to preload {} from {}", effect, f, e);
            }
        }
        // Optional: einmal kurz "anpingen" um Mixer zu initialisieren
        warmUp();
    }


    private void readSoundEffectDirectory(String soundBasePath)
    {
        try (var paths = Files.list(Paths.get(soundBasePath)))
        {

            for (Path p : paths.collect(Collectors.toList()))
            {

                File f = p.toFile();

                if (!f.isFile())
                {
                    continue;
                }
                if (SoundUtils.detectFileType(p).getBaseType().toString().contains("audio"))
                {

                    String name = f.getName();

                    int dot = name.lastIndexOf('.');

                    String shortName = (dot > 0) ? name.substring(0, dot) : name;
                    for (SoundEffects ef : SoundEffects.values())
                    {

                        if (ef.name().equalsIgnoreCase(shortName))
                        {
                            effectsList.put(ef, f);
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        for (SoundEffects ef : effectsList.keySet())
        {
            logger.debug("key: {}, value: {}", ef, effectsList.get(ef));
        }
    }


    public synchronized void playSoundEffect(SoundEffects type)
    {

        Clip clip = clipCache.get(type);


        if (clip == null)
        {

            logger.warn("Clip not preloaded for {}", type);

            return;

        }


        // Vorherigen Sound sauber stoppen (reduziert Übergangs-Clicks)

        if (currentSound != null)
        {

            try
            {

                currentSound.stop();

                currentSound.flush();

                currentSound.setFramePosition(0);

            }
            catch (Exception ignored)
            {
            }

        }


        currentSound = clip;


        // Ziel-Clip sauber resetten

        clip.stop();

        clip.flush();                 // <-- wichtig gegen Knackser/Restbuffer

        clip.setFramePosition(0);

        clip.start();

    }

    /**
     * Lädt Datei, konvertiert robust zu PCM 16-bit und öffnet den Clip
     */

    private Clip loadClipAsPcm(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {

        try (AudioInputStream aisRaw = AudioSystem.getAudioInputStream(file))
        {

            AudioFormat base = aisRaw.getFormat();


            AudioFormat pcm = new AudioFormat(

                    AudioFormat.Encoding.PCM_SIGNED,

                    base.getSampleRate(),

                    16,

                    base.getChannels(),

                    base.getChannels() * 2,

                    base.getSampleRate(),

                    false

            );


            try (AudioInputStream aisPcm = AudioSystem.getAudioInputStream(pcm, aisRaw))
            {

                Clip clip = AudioSystem.getClip();

                clip.open(aisPcm); // liest Daten komplett in den Speicher (bei Clip üblich)

                return clip;

            }

        }

    }


    public void warmUp()
    {
        try
        {
            Clip c = AudioSystem.getClip();
            c.open(new AudioFormat(44100, 16, 2, true, false), new byte[4], 0, 4);
            c.start();
            c.stop();
            c.close();
            logger.debug("Audio system warmed up");
        }
        catch (Exception e)
        {
            logger.warn("Warm-up failed", e);
        }
    }

    /**
     * Beim Shutdown aufrufen!
     */

    public void shutdown()
    {

        for (Clip c : clipCache.values())
        {

            try
            {
                c.stop();
            }
            catch (Exception ignored)
            {
            }

            try
            {
                c.close();
            }
            catch (Exception ignored)
            {
            }

        }

        clipCache.clear();

    }

    private void primeClipSilent(Clip clip)
    {

        FloatControl gain = null;
        float old = 0f;
        try
        {

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {

                gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                old = gain.getValue();
                gain.setValue(gain.getMinimum()); // praktisch stumm
            }
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
            clip.start();
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
        }
        catch (Exception e)
        {

            logger.debug("Silent prime failed: {}", e.toString());

        }
        finally
        {

            if (gain != null)
            {

                try
                {
                    gain.setValue(old);
                }
                catch (Exception ignored)
                {
                }

            }

        }

    }

    private Clip loadClipAsPcmWithFade(File file, float targetSampleRate, int targetChannels, double fadeInMs)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        try (AudioInputStream aisRaw = AudioSystem.getAudioInputStream(file)) {

            // Einheitliches Zielformat für ALLE Clips -> weniger Knacksen durch Umschalten
            AudioFormat target = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    targetSampleRate,
                    16,
                    targetChannels,
                    targetChannels * 2,         // 16-bit -> 2 bytes pro channel
                    targetSampleRate,
                    false                       // little endian
            );

            try (AudioInputStream aisPcm = AudioSystem.getAudioInputStream(target, aisRaw)) {

                byte[] audioBytes = readAllBytes(aisPcm);

                // Fade-In anwenden (z.B. 5ms)
                applyFadeIn16bitLE(audioBytes, target, fadeInMs);

                Clip clip = AudioSystem.getClip();
                clip.open(target, audioBytes, 0, audioBytes.length);
                return clip;
            }
        }
    }

    private static byte[] readAllBytes(AudioInputStream ais) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = ais.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * Linear Fade-In auf 16-bit PCM signed little-endian.
     * Wir skalieren die ersten fadeSamplesFrames Frames von 0..1.
     */
    private static void applyFadeIn16bitLE(byte[] data, AudioFormat fmt, double fadeMs) {
        if (fadeMs <= 0) return;
        if (fmt.getSampleSizeInBits() != 16) return;
        if (fmt.isBigEndian()) return;
        if (!AudioFormat.Encoding.PCM_SIGNED.equals(fmt.getEncoding())) return;

        int frameSize = fmt.getFrameSize(); // bytes per frame (channels*2)
        float sr = fmt.getSampleRate();
        int fadeFrames = (int) Math.max(1, Math.round((fadeMs / 1000.0) * sr));

        int maxFrames = data.length / frameSize;
        fadeFrames = Math.min(fadeFrames, maxFrames);

        // Für jeden Frame im Fade-Bereich: alle Channels skalieren
        for (int i = 0; i < fadeFrames; i++) {
            double gain = (double) i / (double) fadeFrames; // 0..(fast)1
            int frameOffset = i * frameSize;

            // jedes 16-bit sample im Frame bearbeiten
            for (int b = 0; b < frameSize; b += 2) {
                int lo = data[frameOffset + b] & 0xFF;
                int hi = data[frameOffset + b + 1]; // signed
                short sample = (short) ((hi << 8) | lo);

                int scaled = (int) Math.round(sample * gain);
                if (scaled > Short.MAX_VALUE) scaled = Short.MAX_VALUE;
                if (scaled < Short.MIN_VALUE) scaled = Short.MIN_VALUE;

                data[frameOffset + b]     = (byte) (scaled & 0xFF);
                data[frameOffset + b + 1] = (byte) ((scaled >> 8) & 0xFF);
            }
        }
    }


}
