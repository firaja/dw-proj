package it.unumib.disco.dw.etl;

import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HelbizLoader
{
    private final LocalDatabaseManager lDbManager = LocalDatabaseManager.getInstance();

    private final static Logger LOG = LogManager.getLogger(HelbizLoader.class);

    @PreDestroy
    public void onDestroy()
    {
        lDbManager.close();
    }

    public void loadVehiclesPositions(List<HelbizVehicle> vlst)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        StringBuilder str = new StringBuilder();

        vlst.stream().forEach(v -> {
            if (str.length() != 0)
            {
                str.append(",");
            }

            str.append(String.format(Locale.US, "('%s', '%s', %f, %f, %d, %d, %d, '%s')",
                    v.getId(), v.getGeofence(), v.getLat(), v.getLon(),
                    v.getBatteryLevelInMiles(), Math.round(v.getPower()), v.getRange(),
                    date));
        });

        str.append(";");

        String query = "INSERT INTO vehicles VALUES " + str.toString();
        try
        {
            lDbManager.update(query);
        }
        catch(Exception e)
        {
            LOG.error("An error occurred during load of vehicles on database.");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }
    }
}
