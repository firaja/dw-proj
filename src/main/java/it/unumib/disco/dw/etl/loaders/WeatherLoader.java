package it.unumib.disco.dw.etl.loaders;

import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.ParsedWeatherDetection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeatherLoader
{

    private static final Logger LOG = LogManager.getLogger();

    private final LocalDatabaseManager lDbManager = LocalDatabaseManager.getInstance();

    private static final String SELECT_RECENT = "SELECT COUNT(*) FROM weather_detections WHERE `detection_time` >= '%s' AND `city` = '%s'";

    private static final String INSERT_DETECTION = "INSERT INTO weather_detections (`id`, `city`, `detection_time`, `rain`, `relative_humidity`, `wind`, `temperature`) VALUES (%d, '%s', '%s', %f, %f ,%f, %f)";


    public void update(List<ParsedWeatherDetection> detections)
    {

        for (ParsedWeatherDetection detection : detections)
        {
            try
            {
                Map.Entry<Statement, ResultSet> entry = lDbManager.query(String.format(SELECT_RECENT, detection.getDetectionTime(), detection.getCity()));
                ResultSet resultSet = entry.getValue();
                resultSet.next();

                if (entry.getValue().getInt(1) != 0)
                {
                    continue;
                }
            }
            catch (SQLException e)
            {
                LOG.error("", e);
            }
            LOG.debug("Writing new detection {}", detection);
            lDbManager.update(String.format(Locale.US, INSERT_DETECTION, detection.getId(), detection.getCity(), detection.getDetectionTime(), detection.getRain(), detection.getRelativeHumidity(), detection.getWind(), detection.getTemperature()));

        }


    }



    public void init(List<ParsedWeatherDetection> detections)
    {
        for (ParsedWeatherDetection detection : detections)
        {
            LOG.debug("Writing old detection {}", detection);
            try
            {
                lDbManager.update(String.format(Locale.US, INSERT_DETECTION, detection.getId(), detection.getCity(), detection.getDetectionTime(), detection.getRain(), detection.getRelativeHumidity(), detection.getWind(), detection.getTemperature()));
            }
            catch (Exception e)
            {
                LOG.warn("Cannot insert detection {} because {}", detection, e.getMessage());
            }

        }

    }


}
