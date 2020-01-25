package it.unumib.disco.dw.etl.model;

import java.time.LocalDateTime;

public class ParsedWeatherDetection
{
    private long id;

    private String city;

    private LocalDateTime detectionTime;

    private Float rain;

    private Float temperature;

    private Float relativeHumidity;

    private Float wind;

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public LocalDateTime getDetectionTime()
    {
        return detectionTime;
    }

    public void setDetectionTime(LocalDateTime detectionTime)
    {
        this.detectionTime = detectionTime;
    }

    public Float getRain()
    {
        return rain;
    }

    public void setRain(Float rain)
    {
        this.rain = rain;
    }

    public Float getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Float temperature)
    {
        this.temperature = temperature;
    }

    public Float getRelativeHumidity()
    {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Float relativeHumidity)
    {
        this.relativeHumidity = relativeHumidity;
    }

    public Float getWind()
    {
        return wind;
    }

    public void setWind(Float wind)
    {
        this.wind = wind;
    }

    @Override
    public String toString()
    {
        return "ParsedWeatherDetection{" +
                "id=" + id +
                "city=" + city +
                ", detectionTime=" + detectionTime +
                ", rain=" + rain +
                ", temperature=" + temperature +
                ", relativeHumidity=" + relativeHumidity +
                ", wind=" + wind +
                '}';
    }
}
