package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.WeatherExtractor;
import it.unumib.disco.dw.etl.model.RawHistoricalWeatherDetection;
import it.unumib.disco.dw.etl.model.RawRealtimeWeatherDetection;
import it.unumib.disco.dw.etl.transformers.WeatherTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherProgram
{

    private static final Logger LOG = LogManager.getLogger();

    public static final void main(String[] args) throws Exception
    {
        WeatherExtractor weatherExtractor = new WeatherExtractor();
        List<RawRealtimeWeatherDetection> rawRTDetections = weatherExtractor.retrieveRealtimeMeasurement();

        List<RawHistoricalWeatherDetection> rawHDetections = weatherExtractor.retrieveHistoricalMeasurement(new Date());

        WeatherTransformer transformer = new WeatherTransformer();
        rawRTDetections.stream().map(transformer::transform).collect(Collectors.toList()).forEach(LOG::info);
        rawHDetections.stream().map(transformer::transform).collect(Collectors.toList()).forEach(LOG::info);
    }


}
