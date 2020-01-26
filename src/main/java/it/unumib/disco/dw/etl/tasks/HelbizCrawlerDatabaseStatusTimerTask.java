package it.unumib.disco.dw.etl.tasks;

import it.unumib.disco.dw.etl.loaders.HelbizLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class HelbizCrawlerDatabaseStatusTimerTask extends TimerTask
{
    private final static Logger LOG = LogManager.getLogger(HelbizCrawlerDatabaseStatusTimerTask.class);
    private HelbizLoader loader = new HelbizLoader();

    @Override
    public void run()
    {
        LOG.debug("Size of vehicles table: {} MB", loader.getTableSize("vehicles"));
        LOG.debug("Size of vehicles_profilings table: {} MB", loader.getTableSize("vehicles_profilings"));
        LOG.debug("Size of vehicle_hourly_use table: {} MB", loader.getTableSize("vehicle_hourly_use"));
        LOG.debug("Size of city_hourly_use table: {} MB", loader.getTableSize("city_hourly_use"));
    }
}
