package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.WeatherExtractor;
import it.unumib.disco.dw.etl.loaders.WeatherLoader;
import it.unumib.disco.dw.etl.model.ParsedWeatherDetection;
import it.unumib.disco.dw.etl.model.RawHistoricalWeatherDetection;
import it.unumib.disco.dw.etl.model.RawRealtimeWeatherDetection;
import it.unumib.disco.dw.etl.transformers.WeatherTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class WeatherProgram
{

    private static final Logger LOG = LogManager.getLogger();

    private static final WeatherExtractor WEATHER_EXTRACTOR = new WeatherExtractor();

    private static final WeatherTransformer WEATHER_TRANSFORMER = new WeatherTransformer();

    private static final WeatherLoader LOADER = new WeatherLoader();

    public static void main(String[] args)
    {

        String argument = args.length == 0 ? "" : args[0];

        switch (argument)
        {
            case "init":
                initialize();
                break;
            case "update":
                update();
                break;
            default:
                initialize();
                update();
        }

    }

    private static void initialize()
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int month = 1; month <= 12; month++)
        {
            executor.execute(new WeatherInitializer(month));
        }
        executor.shutdown();
    }

    private static void update()
    {
        new Thread(new WeatherUpdater()).start();
    }


    static class WeatherInitializer implements Runnable
    {

        private String month;

        public WeatherInitializer(int month)
        {
            LOG.trace("Thread created for month {}", month);
            if (month < 10)
            {
                this.month = "0" + month;
            }
            else
            {
                this.month = String.valueOf(month);
            }
        }

        @Override
        public void run()
        {
            try
            {
                LocalDateTime start = LocalDateTime.ofInstant(new SimpleDateFormat("dd/MM/yyyy").parse("01/" + month + "/2020").toInstant(), ZoneId.systemDefault());
                LocalDateTime end = start.plusMonths(1L);

                for (LocalDateTime date = start; date.isBefore(end) && date.isBefore(LocalDateTime.now()); date = date.plusDays(1))
                {
                    List<RawHistoricalWeatherDetection> rawHDetections = WEATHER_EXTRACTOR.retrieveHistoricalMeasurement(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
                    List<ParsedWeatherDetection> hDetection = rawHDetections.stream().map(WEATHER_TRANSFORMER::transform).collect(Collectors.toList());
                    LOADER.init(hDetection);
                }

            }
            catch (ParseException e)
            {
                LOG.error("", e);
            }
        }
    }


    static class WeatherUpdater implements Runnable
    {
        private static final long SLEEP_TIME = 1000 * 60 * 5L;

        @Override
        public void run()
        {

            while (!Thread.currentThread().isInterrupted())
            {
                List<RawRealtimeWeatherDetection> rawRTDetections = WEATHER_EXTRACTOR.retrieveRealtimeMeasurement();


                List<ParsedWeatherDetection> rtDetection = rawRTDetections.stream().map(WEATHER_TRANSFORMER::transform).collect(Collectors.toList());


                LOADER.update(rtDetection);

                try
                {
                    Thread.sleep(SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    LOG.error("", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
