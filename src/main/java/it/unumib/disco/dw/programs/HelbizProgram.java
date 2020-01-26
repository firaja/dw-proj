package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.loaders.HelbizLoader;
import it.unumib.disco.dw.etl.extractors.HelbizScootersLocationsExtractor;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import it.unumib.disco.dw.etl.tasks.HelbizCrawlerDatabaseStatusTimerTask;
import it.unumib.disco.dw.etl.tasks.HelbizExtractorTimerTask;
import it.unumib.disco.dw.etl.transformers.HelbizTransformer;
import it.unumib.disco.dw.etl.utils.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelbizProgram
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOG = LogManager.getLogger(HelbizProgram.class);

    public static void main(String [] args)
    {
        HelbizScootersLocationsExtractor extractor = new HelbizScootersLocationsExtractor();

        if (args.length == 0)
        {
            LOG.info("No option selected. List of available regions will be shown.");
            ConfigLoader config = new ConfigLoader();
            extractor.performLogin(config.getProperty("helbiz.credentials.email"),
                    config.getProperty("helbiz.credentials.password"));

            List<HelbizRegion> regionList = extractor.getRegions();
            extractor.showListOfRegions(regionList);

            Scanner scan = new Scanner(System.in);

            while (true)
            {
                LOG.info("Insert the id of the region for which you would like to perform a test for vehicles' presence:");
                int regionIndex = scan.nextInt();

                List<HelbizVehicle> vehicleLst = extractor.getVehicles(regionList.get(regionIndex));
                extractor.showVehicles(vehicleLst);
            }
        }
        /*
         * Four modes:
         * - crawler: java -jar dw.jar crawler #region_name# #interval_in_minutes#
         * - transformer: java -jar dw.jar transformer #region_name# #start_date_time# #end_date_time#
         * - vehicles profilings: java -jar dw.jar vprofilings #region_name# #start_date_time# #end_date_time#
         * - city profilings: java -jar dw.jar cprofilings #region_name# #start_date_time# #end_date_time#
         * - transform and all profilings: java -jar dw.jar alltrandprof #region_name# #start_date_time# #end_date_time#
         */

        HelbizLoader loader = new HelbizLoader();
        HelbizTransformer transformer = new HelbizTransformer();

        if (args[0].equals("crawler"))
        {
            String regionName = args[1];
            long interval = Long.parseLong(args[2]);

            ConfigLoader config = new ConfigLoader();
            extractor.performLogin(config.getProperty("helbiz.credentials.email"),
                    config.getProperty("helbiz.credentials.password"));
            HelbizExtractorTimerTask task = new HelbizExtractorTimerTask(extractor, regionName);
            HelbizCrawlerDatabaseStatusTimerTask dbTask = new HelbizCrawlerDatabaseStatusTimerTask();

            long delay  = 0L;
            long period = 1000L * 60L * interval;

            ScheduledExecutorService crawler = Executors.newSingleThreadScheduledExecutor();
            crawler.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);

            period = 1000L * 60L * 8;

            ScheduledExecutorService status = Executors.newSingleThreadScheduledExecutor();
            status.scheduleAtFixedRate(dbTask, 60000L, period, TimeUnit.MILLISECONDS);
        }
        else if (args[0].equals("transformer"))
        {
            String regionName = args[1];
            String startDate = args[2];
            String endDate = args[3];

            List<HelbizRegion> regionList = extractor.getRegions();
            Optional<HelbizRegion> region = regionList.stream().filter(r -> r.getName().equals(regionName)).findFirst();
            if (!region.isPresent())
            {
                LOG.error("No region found with name '{}'", regionName);
                LOG.error("Extraction for region {} will be stopped", regionName);
                return;
            }

            Date start = null, end = null;
            try
            {
                start = sdf.parse(startDate);
                end = sdf.parse(endDate);
            }
            catch (ParseException e)
            {
                LOG.error("Error during input parsing. The following exception has been thrown:", e);
                LOG.error("The program will be stopped");
            }

            List<Date> queryTimes = loader.getAllQueryTimes(region.get(), start, end);
            transformer.processListOfProfilings(region.get(), queryTimes);
        }
        else if (args[0].equals("vprofilings"))
        {
            String regionName = args[1];
            String startDate = args[2];
            String endDate = args[3];

            List<HelbizRegion> regionList = extractor.getRegions();
            Optional<HelbizRegion> region = regionList.stream().filter(r -> r.getName().equals(regionName)).findFirst();
            if (!region.isPresent())
            {
                LOG.error("No region found with name '{}'", regionName);
                LOG.error("Extraction for region {} will be stopped", regionName);
                return;
            }

            Date start = null, end = null;
            try
            {
                start = sdf.parse(startDate);
                end = sdf.parse(endDate);
            }
            catch (ParseException e)
            {
                LOG.error("Error during input parsing. The following exception has been thrown:", e);
                LOG.error("The program will be stopped");
            }

            transformer.feedVehicleHourProfilingTable(region.get(), start, end);
        }
        else if (args[0].equals("cprofilings"))
        {
            String regionName = args[1];
            String startDate = args[2];
            String endDate = args[3];

            List<HelbizRegion> regionList = extractor.getRegions();
            Optional<HelbizRegion> region = regionList.stream().filter(r -> r.getName().equals(regionName)).findFirst();
            if (!region.isPresent())
            {
                LOG.error("No region found with name '{}'", regionName);
                LOG.error("Extraction for region {} will be stopped", regionName);
                return;
            }

            Date start = null, end = null;
            try
            {
                start = sdf.parse(startDate);
                end = sdf.parse(endDate);
            }
            catch (ParseException e)
            {
                LOG.error("Error during input parsing. The following exception has been thrown:", e);
                LOG.error("The program will be stopped");
            }

            transformer.feedCityHourProfilingTable(region.get(), start, end);
        }
        else if (args[0].equals("alltrandprof"))
        {
            String regionName = args[1];
            String startDate = args[2];
            String endDate = args[3];

            List<HelbizRegion> regionList = extractor.getRegions();
            Optional<HelbizRegion> region = regionList.stream().filter(r -> r.getName().equals(regionName)).findFirst();
            if (!region.isPresent())
            {
                LOG.error("No region found with name '{}'", regionName);
                LOG.error("Extraction for region {} will be stopped", regionName);
                return;
            }

            Date start = null, end = null;
            try
            {
                start = sdf.parse(startDate);
                end = sdf.parse(endDate);
            }
            catch (ParseException e)
            {
                LOG.error("Error during input parsing. The following exception has been thrown:", e);
                LOG.error("The program will be stopped");
            }

            List<Date> queryTimes = loader.getAllQueryTimes(region.get(), start, end);
            transformer.processListOfProfilings(region.get(), queryTimes);
            queryTimes = null; // free memory used by queryTimes list
            transformer.feedVehicleHourProfilingTable(region.get(), start, end);
            transformer.feedCityHourProfilingTable(region.get(), start, end);
        }
    }
}
