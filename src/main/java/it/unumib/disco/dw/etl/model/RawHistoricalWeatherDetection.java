package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawHistoricalWeatherDetection extends RawAbstractWeatherDetection
{

    private String date;

    private String temperature_mean;

    private String relative_humidity_mean;

    private String rain;

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTemperature_mean()
    {
        return temperature_mean;
    }

    public void setTemperature_mean(String temperature_mean)
    {
        this.temperature_mean = temperature_mean;
    }

    public String getRelative_humidity_mean()
    {
        return relative_humidity_mean;
    }

    public void setRelative_humidity_mean(String relative_humidity_mean)
    {
        this.relative_humidity_mean = relative_humidity_mean;
    }

    public String getRain()
    {
        return rain;
    }

    public void setRain(String rain)
    {
        this.rain = rain;
    }

    @Override
    public String toString()
    {
        return "RawHistoricalWeatherDetection{" +
                "station=" + getStation() +
                "date='" + date + '\'' +
                ", temperature_mean='" + temperature_mean + '\'' +
                ", relative_humidity_mean='" + relative_humidity_mean + '\'' +
                ", rain='" + rain + '\'' +
                '}';
    }
}
