package it.unumib.disco.dw.etl.transformers;

import it.unumib.disco.dw.etl.model.ParsedWeatherDetection;
import it.unumib.disco.dw.etl.model.RawHistoricalWeatherDetection;
import it.unumib.disco.dw.etl.model.RawRealtimeWeatherDetection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;


public class WeatherTransformer
{

    private static final Logger LOG = LogManager.getLogger();

    public ParsedWeatherDetection transform(RawRealtimeWeatherDetection rawDetection)
    {
        ParsedWeatherDetection parsedDetection = new ParsedWeatherDetection();
        parsedDetection.setId(toLong(rawDetection.getStation().getId()));
        parsedDetection.setDetectionTime(toDate(rawDetection.getDatetime(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
        parsedDetection.setCity(rawDetection.getStation().getCity());
        parsedDetection.setRain(toFloat(rawDetection.getRain_rate()));
        parsedDetection.setRelativeHumidity(toFloat(rawDetection.getRelative_humidity()));
        parsedDetection.setTemperature(toFloat(rawDetection.getTemperature()));
        parsedDetection.setWind(toFloat(rawDetection.getWind_strength()));
        return parsedDetection;
    }

    public ParsedWeatherDetection transform(RawHistoricalWeatherDetection rawDetection)
    {
        ParsedWeatherDetection parsedDetection = new ParsedWeatherDetection();
        parsedDetection.setId(toLong(rawDetection.getStation().getId()));
        parsedDetection.setDetectionTime(toDate(rawDetection.getDate(), "yyyy-MM-dd"));
        parsedDetection.setCity(rawDetection.getStation().getCity());
        parsedDetection.setRain(toFloat(rawDetection.getRain()));
        parsedDetection.setRelativeHumidity(toFloat(rawDetection.getRelative_humidity_mean()));
        parsedDetection.setTemperature(toFloat(rawDetection.getTemperature_mean()));
        parsedDetection.setWind(toFloat(null));
        return parsedDetection;
    }

    private static LocalDateTime toDate(String date, String format)
    {
        try
        {

            Date d = new SimpleDateFormat(format, Locale.ITALIAN).parse(date);
            return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        }
        catch (Exception e)
        {
            LOG.error("Cannot parse date `{}` with format `{}`.", date, format);
            return null;
        }
    }

    private static Float toFloat(String str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException nfe)
        {
            LOG.error("Cannot parse float from string `{}`.", str);
            return 0f;
        }
    }

    private static Long toLong(String str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return Long.parseLong(str);
        }
        catch (NumberFormatException nfe)
        {
            LOG.error("Cannot parse long from string `{}`.", str);
            return 0L;
        }
    }

    private static Double toDouble(String str)
    {
        if (str == null)
        {
            return null;
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
