package net.ck.mtbg.util.xml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class MapJSONReader
{
    public MapJSONReader()
    {
        // Builder-style since 2.10:
        JsonFactory factory = JsonFactory.builder()
                //configure, if necessary:
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .build();
    }
}
