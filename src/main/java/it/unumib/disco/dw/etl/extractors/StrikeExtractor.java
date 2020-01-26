package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.model.RawStrike;
import it.unumib.disco.dw.etl.model.RawStrikeResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StrikeExtractor extends AbstractJSONExtractor
{

    private static final Logger LOG = LogManager.getLogger();

    public List<RawStrike> retrieveAllNationalStrikes()
    {
        LOG.info("Retrieving all national strikes from remote source...");

        return doRetrieve(Config.ExternalSource.Strike.ALL);
    }

    public List<RawStrike> retrieveLastNationalStrikes()
    {
        LOG.info("Retrieving realtime national strikes from remote source...");

        return doRetrieve(Config.ExternalSource.Strike.REALTIME);
    }


    private List<RawStrike> doRetrieve(String url)
    {
        long total = 0L;

        List<RawStrike> strikeList = new ArrayList<>(4500);

        try (JsonParser parser = getParser(url))
        {

            LOG.debug("--- START");

            JsonToken token = parser.nextToken();
            if (token == null)
            {
                LOG.error("No token found");
            }
            if (!JsonToken.START_OBJECT.equals(token))
            {
                LOG.error("Non object token found");
            }

            parser.nextToken(); // help field
            parser.nextToken(); // success field
            JsonToken success = parser.nextValue();
            if (success != null && Boolean.parseBoolean(success.asString()))
            {
                LOG.debug("Call is OK");
            }
            else
            {
                LOG.error("Cannot retrieve national strikes");
            }

            parser.nextToken(); // result field
            RawStrikeResult result = parser.readValueAs(RawStrikeResult.class);

            List<RawStrike> retrievedStrikes = result.getResult().getRecords();

            for (RawStrike retrievedStrike : retrievedStrikes)
            {
                total++;
                if (filter(retrievedStrike))
                {
                    strikeList.add(retrievedStrike);
                    LOG.debug("✔️ Detection {} accepted", retrievedStrike);
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("❌ Detection {} rejected", retrievedStrike);
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

    private boolean filter(RawStrike strike)
    {
        return StringUtils.equalsAnyIgnoreCase(strike.getNome_provincia(), "torino")
                && StringUtils.containsIgnoreCase(strike.getSettore(), "trasporto pubblico");
    }

}
