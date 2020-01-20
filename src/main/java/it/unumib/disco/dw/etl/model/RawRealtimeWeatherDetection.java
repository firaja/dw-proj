package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawRealtimeWeatherDetection extends RawAbstractWeatherDetection
{

    private String datetime;

    private String temperature;


    private String relative_humidity;


    private String rain_rate;


    private String wind_strength;

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    public String getTemperature()
    {
        return temperature;
    }

    public void setTemperature(String temperature)
    {
        this.temperature = temperature;
    }

    public String getRelative_humidity()
    {
        return relative_humidity;
    }

    public void setRelative_humidity(String relative_humidity)
    {
        this.relative_humidity = relative_humidity;
    }

    public String getRain_rate()
    {
        return rain_rate;
    }

    public void setRain_rate(String rain_rate)
    {
        this.rain_rate = rain_rate;
    }

    public String getWind_strength()
    {
        return wind_strength;
    }

    public void setWind_strength(String wind_strength)
    {
        this.wind_strength = wind_strength;
    }

    @Override
    public String toString()
    {
        return "RawWeatherDetection{" +
                "station=" + getStation() +
                ", datetime='" + datetime + '\'' +
                ", temperature='" + temperature + '\'' +
                ", relative_humidity='" + relative_humidity + '\'' +
                ", rain_rate='" + rain_rate + '\'' +
                ", wind_strength='" + wind_strength + '\'' +
                '}';
    }
}
