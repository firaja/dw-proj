package it.unumib.disco.dw.etl.transformers;

import it.unumib.disco.dw.etl.model.ParsedWeatherDetection;
import it.unumib.disco.dw.etl.model.RawRealtimeWeatherDetection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherTransformer
{

    private static final Logger LOG = LogManager.getLogger();

    public ParsedWeatherDetection transform(RawRealtimeWeatherDetection rawDetection)
    {
        ParsedWeatherDetection parsedDetection = new ParsedWeatherDetection();
        parsedDetection.setDetectionTime(toDate(rawDetection.getDatetime(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
        parsedDetection.setLatitude(toDouble(rawDetection.getStation().getLat()));
        parsedDetection.setLongitude(toDouble(rawDetection.getStation().getLng()));
        parsedDetection.setRain(toFloat(rawDetection.getRain_rate()));
        parsedDetection.setRelativeHumidity(toFloat(rawDetection.getRelative_humidity()));
        parsedDetection.setTemperature(toFloat(rawDetection.getTemperature()));
        parsedDetection.setWind(toFloat(rawDetection.getWind_strength()));
        return parsedDetection;
    }

    private static Date toDate(String date, String format)
    {
        try
        {
            return new SimpleDateFormat(format, Locale.ITALIAN).parse(date);
        }
        catch (Exception e)
        {
            LOG.error("Cannot parse date {} with format {}", date, format);
            return null;
        }
    }


    private static float toFloat(String str)
    {
        if (str == null)
        {
            return 0f;
        }
        try
        {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException nfe)
        {
            LOG.error("Cannot parse float from string `{}`", str);
            return 0f;
        }
    }

    private static double toDouble(String str)
    {
        if (str == null)
        {
            return 0d;
        }
        try
        {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException nfe)
        {
            LOG.error("Cannot parse double from string `{}`", str);
            return 0d;
        }
    }


}
