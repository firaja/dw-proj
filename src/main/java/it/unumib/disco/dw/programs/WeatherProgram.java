package it.unumib.disco.dw.programs;

import it.unumib.disco.dw.etl.extractors.WeatherExtractor;

public class WeatherProgram
{

    public static final void main(String[] args)
    {
        WeatherExtractor weatherExtractor = new WeatherExtractor("MI", "Milano");
        weatherExtractor.retrieveSensors();

    }


}
