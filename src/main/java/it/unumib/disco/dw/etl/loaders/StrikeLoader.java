package it.unumib.disco.dw.etl.loaders;

import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.ParsedStrike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class StrikeLoader
{

    private static final Logger LOG = LogManager.getLogger();

    private final LocalDatabaseManager lDbManager = LocalDatabaseManager.getInstance();

    private static final String INSERT_STRIKE = "INSERT INTO strikes (`start_time`, `end_time`, `name`, `city`, `area_of_interest`) VALUES ('%s', '%s', '%s', '%s', '%s')";

    public void init(List<ParsedStrike> strikeList)
    {
        for (ParsedStrike strike : strikeList)
        {
            LOG.debug("Writing old strike {}", strike);
            try
            {
                lDbManager.update(String.format(INSERT_STRIKE, strike.getBegin(), strike.getEnd(), strike.getName(), strike.getCity(), strike.getSector()));
            }
            catch (Exception e)
            {
                LOG.warn("Cannot insert detection {} because {}", strike, e.getMessage());
            }
        }
    }

    public void update(List<ParsedStrike> strikeList)
    {

    }


}
