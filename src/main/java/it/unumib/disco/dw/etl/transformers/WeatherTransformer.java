package it.unumib.disco.dw.etl.transformers;

import it.unumib.disco.dw.etl.model.SensorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class WeatherTransformer
{

    private static final Logger LOG = LogManager.getLogger();

    public static SensorType toSensorType(String type)
    {
        switch (type)
        {
            case "Temperatura":
                return SensorType.TEMPERATURE;
            case "Umidità Relativa":
                return SensorType.HUMIDITY;
            case "Precipitazione":
                return SensorType.PRECIPITATION;
            default:
                return SensorType.UNKNOWN;
        }
    }



    public static String toUnit(String unit)
    {
        switch (unit)
        {
            case "°":
                return "degree";
            default:
                return unit;
        }
    }


}
