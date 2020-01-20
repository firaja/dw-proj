package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.WeatherExtractor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherProgram
{

    public static final void main(String[] args) throws Exception
    {
        WeatherExtractor weatherExtractor = new WeatherExtractor();
        weatherExtractor.retrieveHistoricalMeasurement(new SimpleDateFormat("dd/MM/yyyy").parse("04/05/2019"));

    }


}
