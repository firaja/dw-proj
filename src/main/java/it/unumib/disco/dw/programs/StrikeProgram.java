package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.StrikeExtractor;
import it.unumib.disco.dw.etl.loaders.StrikeLoader;
import it.unumib.disco.dw.etl.model.ParsedStrike;
import it.unumib.disco.dw.etl.model.RawStrike;
import it.unumib.disco.dw.etl.transformers.StrikeTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class StrikeProgram
{

    private static final Logger LOG = LogManager.getLogger();

    private static final StrikeExtractor STRIKE_EXTRACTOR = new StrikeExtractor();

    private static final StrikeTransformer STRIKE_TRANSFORMER = new StrikeTransformer();

    private static final StrikeLoader LOADER = new StrikeLoader();

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
        new Thread(new StrikeInitializer()).start();
    }

    private static void update()
    {
        new Thread(new StrikeUpdater()).start();
    }


    static class StrikeInitializer implements Runnable
    {

        @Override
        public void run()
        {
            List<RawStrike> allStrikes = STRIKE_EXTRACTOR.retrieveAllNationalStrikes();
            List<ParsedStrike> strikes = allStrikes.stream().map(STRIKE_TRANSFORMER::transform).collect(Collectors.toList());
            LOADER.init(strikes);
        }
    }


    static class StrikeUpdater implements Runnable
    {
        private static final long SLEEP_TIME = 1000 * 60 * 5L;

        @Override
        public void run()
        {

            while (!Thread.currentThread().isInterrupted())
            {
                List<RawStrike> lastStrikes = STRIKE_EXTRACTOR.retrieveLastNationalStrikes();


                List<ParsedStrike> strikes = lastStrikes.stream().map(STRIKE_TRANSFORMER::transform).collect(Collectors.toList());


                LOADER.update(strikes);

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
