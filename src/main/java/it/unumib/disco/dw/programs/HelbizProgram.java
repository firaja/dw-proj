package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.HelbizScootersLocationsExtractor;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import it.unumib.disco.dw.etl.tasks.HelbizExtractorTimerTask;
import it.unumib.disco.dw.etl.utils.ConfigLoader;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class HelbizProgram
{
    public static void main(String [] args)
    {
        ConfigLoader config = new ConfigLoader();
        HelbizScootersLocationsExtractor extractor = new HelbizScootersLocationsExtractor();
        extractor.performLogin(config.getProperty("helbiz.credentials.email"),
                config.getProperty("helbiz.credentials.password"));

        /*List<HelbizRegion> regionList = extractor.getRegions();
        extractor.showListOfRegions(regionList);
        Scanner scan = new Scanner(System.in);
        int regionIndex = scan.nextInt();
        List<HelbizVehicle> vehicleLst = extractor.getVehicles(regionList.get(regionIndex));
        extractor.showVehicles(vehicleLst);
        extractor.loadVehicles(vehicleLst);*/

        HelbizExtractorTimerTask task1 = new HelbizExtractorTimerTask(extractor, "torino");
        HelbizExtractorTimerTask task2 = new HelbizExtractorTimerTask(extractor, "rome");

        Timer timer1 = new Timer("TimerTorino");
        Timer timer2 = new Timer("TimerRome");

        long delay  = 0L;
        long period = 1000L * 60L * 5L;
        timer1.scheduleAtFixedRate(task1, delay, period);
        timer2.scheduleAtFixedRate(task2, delay, period);
    }
}
