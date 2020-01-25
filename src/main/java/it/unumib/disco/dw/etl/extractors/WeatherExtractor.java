package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.model.RawAbstractWeatherDetection;
import it.unumib.disco.dw.etl.model.RawHistoricalWeatherDetection;
import it.unumib.disco.dw.etl.model.RawRealtimeWeatherDetection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherExtractor extends AbstractJSONExtractor
{

    private static final Logger LOG = LogManager.getLogger();


    public List<RawRealtimeWeatherDetection> retrieveRealtimeMeasurement()
    {
        LOG.info("Retrieving realtime detections from remote source...");

        return doRetrieve(Config.ExternalSource.Weather.REALTIME, RawRealtimeWeatherDetection.class);
    }

    public List<RawHistoricalWeatherDetection> retrieveHistoricalMeasurement(Date date)
    {
        LOG.info("Retrieving historical detection of {} from remote source...", new SimpleDateFormat("dd/MM/yyyy").format(date));


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return doRetrieve(String.format(Config.ExternalSource.Weather.HISTORICAL, year, month, day), RawHistoricalWeatherDetection.class);


    }


    private <T extends RawAbstractWeatherDetection> List<T> doRetrieve(String url, Class<T> type)
    {

        long total = 0L;

        List<T> sensorList = new ArrayList<>(100);

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

                T sensor = parser.readValueAs(type);
                if (sensor == null)
                {
                    break;
                }


                total++;

                LOG.trace("Detection {}", sensor);

                if (filter(sensor))
                {
                    LOG.debug("✔️ Detection {} accepted", sensor.getStation().getId());
                    sensorList.add(sensor);
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("❌ Detection {} rejected", sensor.getStation().getId());
                }
            }

        }
        catch (IOException e)
        {
            LOG.error("", e);
        }

        LOG.debug("--- END");


        LOG.info("Retrieved {} detections out of {}", sensorList.size(), total);
        return sensorList;
    }


    private boolean filter(RawAbstractWeatherDetection detection)
    {
        return detection.getStation() != null //
                && StringUtils.equalsIgnoreCase(detection.getStation().getCity(), "torino");
    }


}
