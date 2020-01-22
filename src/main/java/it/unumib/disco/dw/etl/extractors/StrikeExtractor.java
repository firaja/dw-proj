package it.unumib.disco.dw.etl.extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.sun.istack.internal.NotNull;

import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.model.RawAbstractWeatherDetection;
import it.unumib.disco.dw.etl.model.RawStrike;


public class StrikeExtractor extends AbstractJSONExtractor
{

    private static final Logger LOG = LogManager.getLogger();

    public List<RawStrike> retrieveRealtimeMeasurement()
    {
        LOG.info("Retrieving realtime detections from remote source...");

        return doRetrieve(Config.ExternalSource.Strike.REALTIME);
    }


    private List<RawStrike> doRetrieve(@NotNull String url)
    {
        long total = 0L;

        List<RawStrike> strikeList = new ArrayList<>(100);

        try (JsonParser parser = getParser(url))
        {

            LOG.debug("--- START");

            JsonToken token = parser.nextToken();
            if (token == null)
            {
                LOG.error("No token found");
            }
            if (!JsonToken.START_ARRAY.equals(token))
            {
                LOG.error("Non array token found");
            }


            while (true)
            {

                token = parser.nextToken();
                if (!JsonToken.START_OBJECT.equals(token))
                {
                    break;
                }

                RawStrike strike = parser.readValueAs(RawStrike.class);
                if (strike == null)
                {
                    break;
                }


                total++;

                LOG.trace("Detection {}", strike);

                if (filter(strike))
                {
                    LOG.debug("✔️ Detection {} accepted", strike.getSettore());
                    strikeList.add(strike);
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("❌ Detection {} rejected", strike.getSettore());
                }
            }

        }
        catch (IOException e)
        {
            LOG.error("", e);
        }

        LOG.debug("--- END");


        LOG.info("Retrieved {} detections out of {}", strikeList.size(), total);
        return strikeList;
    }

    private boolean filter(@NotNull RawStrike strike)
    {
        return StringUtils.equalsAnyIgnoreCase(strike.getNome_provincia(), "tutte", "torino");
    }

}
