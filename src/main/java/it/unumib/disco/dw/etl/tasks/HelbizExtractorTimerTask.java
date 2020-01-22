package it.unumib.disco.dw.etl.tasks;

import it.unumib.disco.dw.etl.extractors.HelbizScootersLocationsExtractor;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

public class HelbizExtractorTimerTask extends TimerTask
{
    private HelbizScootersLocationsExtractor extractor;
    private String regionName;
    private HelbizRegion region;

    private final static Logger LOG = LogManager.getLogger(HelbizExtractorTimerTask.class);

    public HelbizExtractorTimerTask(HelbizScootersLocationsExtractor extractor, String regionName)
    {
        this.extractor = extractor;
        this.regionName = regionName;
    }

    @Override
    public void run()
    {
        if (getRegion() == null)
        {
            List<HelbizRegion> regionList = extractor.getRegions();
            Optional<HelbizRegion> region = regionList.stream().filter(r -> r.getName().equals(regionName)).findFirst();
            if (!region.isPresent())
            {
                LOG.error("No region found with name '{}'", regionName);
                LOG.error("Extraction for region {} will be stopped", regionName);
                return;
            }
            setRegion(region.get());
        }

        List<HelbizVehicle> vehicleLst = extractor.getVehicles(getRegion());
        extractor.loadVehicles(vehicleLst);
    }

    public void setExtractor(HelbizScootersLocationsExtractor extractor)
    {
        this.extractor = extractor;
    }

    public HelbizScootersLocationsExtractor getExtractor()
    {
        return extractor;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public String getRegionName()
    {
        return regionName;
    }

    protected void setRegion(HelbizRegion region)
    {
        this.region = region;
    }

    public HelbizRegion getRegion()
    {
        return region;
    }
}
