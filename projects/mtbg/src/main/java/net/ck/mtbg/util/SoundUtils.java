package net.ck.mtbg.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import java.nio.file.Path;

public class SoundUtils
{
    private static final Logger logger = LogManager.getLogger(SoundUtils.class);
    private static TikaConfig tika;

    static
    {
        try
        {
            tika = new TikaConfig();
        }
        catch (Exception e)
        {
            logger.error("error setting up Tika");
        }
    }




    /**
     * <a href="https://stackoverflow.com/questions/13209644/java-detecting-an-audio-file-mp3">how to detect audio file</a>
     * <a href="https://tika.apache.org/2.4.1/detection.html">detection intro at tika project</a>
     * @param entry - path to a file
     * @return media type like audio/vnd.wave
     */
    public static MediaType detectFileType(Path entry)
    {
        MediaType mimetype = null;
        try
        {
            Metadata metadata = new Metadata();
            mimetype = tika.getDetector().detect(TikaInputStream.get(entry, metadata), metadata);
            //logger.debug("mime type: {}", mimetype);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mimetype;
    }

}
