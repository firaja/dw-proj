package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.HelbizScootersLocationsExtractor;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import it.unumib.disco.dw.etl.utils.ConfigLoader;

import java.util.List;
import java.util.Scanner;

public class HelbizProgram
{
    public static void main(String [] args)
    {
        ConfigLoader config = new ConfigLoader();
        HelbizScootersLocationsExtractor extractor = new HelbizScootersLocationsExtractor();
        extractor.performLogin(config.getProperty("helbiz.credentials.email"),
                config.getProperty("helbiz.credentials.password"));

        List<HelbizRegion> regionList = extractor.getRegions();
        extractor.showListOfRegions(regionList);
        Scanner scan = new Scanner(System.in);
        int regionIndex = scan.nextInt();
        List<HelbizVehicle> vehicleLst = extractor.getVehicles(regionList.get(regionIndex));
        extractor.showVehicles(vehicleLst);
    }
}
