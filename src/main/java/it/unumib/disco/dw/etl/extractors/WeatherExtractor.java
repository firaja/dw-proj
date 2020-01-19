package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.model.RawWeatherSensor;
import it.unumib.disco.dw.etl.model.RawWeatherValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherExtractor extends AbstractJSONExtractor
{

    private static final Logger LOG = LogManager.getLogger();

    private String city="milano";

    private String province = "MI";

    public WeatherExtractor()
    {
        //
    }


    public WeatherExtractor(String province, String city)
    {
        this.city = city;
        this.province = province;
    }


    public List<RawWeatherSensor> retrieveSensors()
    {

        LOG.info("Retrieving sensors from remote source...");

        long total = 0L;

        List<RawWeatherSensor> sensorList = new ArrayList<>(100);

        try (JsonParser parser = getParser(Config.ExternalSource.Weather.SENSORS_DESCRIPTION + "&provincia=" + this.province))
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

                RawWeatherSensor sensor = parser.readValueAs(RawWeatherSensor.class);
                if (sensor == null)
                {
                    break;
                }


                total++;

                if (filter(sensor))
                {
                    LOG.debug("✔️ Sensor {} accepted", sensor.getIdsensore());
                    LOG.trace("Sensor {}", sensor);
                    sensorList.add(sensor);
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("❌ Sensor {} rejected", sensor.getIdsensore());
                }
            }

        }
        catch (IOException e)
        {
            LOG.error("", e);
        }

        LOG.debug("--- END");


        LOG.info("Retrieved {} sensors out of {}", sensorList.size(), total);
        return sensorList;

    }


    public List<RawWeatherValue> retrieveValues(long page)
    {

        LOG.info("Retrieving values from remote source...");

        long total = 0L;

        List<RawWeatherValue> valueList = new ArrayList<>(100);

        try (JsonParser parser = getParser(Config.ExternalSource.Weather.SENSORS_VALUE))
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

                RawWeatherValue value = parser.readValueAs(RawWeatherValue.class);
                if (value == null)
                {
                    LOG.debug("--- END");
                    break;
                }


                total++;

                if (filter(value))
                {
                    LOG.debug("✔️ Value {} accepted", value.getIdsensore());
                    valueList.add(value);
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("❌ Value {} rejected", value.getIdsensore());
                }
            }

        }
        catch (IOException e)
        {
            LOG.error("", e);
        }

        LOG.info("Retrieved {} values out of {}", valueList.size(), total);
        return valueList;

    }


    private boolean filter(RawWeatherSensor sensor)
    {
        return StringUtils.startsWithIgnoreCase(sensor.getNomestazione(), this.city);
    }

    private boolean filter(RawWeatherValue value)
    {
        return StringUtils.equalsAnyIgnoreCase(value.getStato(), "VA", "VV") && StringUtils.equals(value.getIdOperatore(), "1");
    }


}
