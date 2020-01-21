package it.unumib.disco.dw.etl.model;

import java.util.Date;

public class ParsedWeatherDetection
{

    private Double latitude;

    private Double longitude;

    private Date detectionTime;

    private Float rain;

    private Float temperature;

    private Float relativeHumidity;

    private Float wind;

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Date getDetectionTime()
    {
        return detectionTime;
    }

    public void setDetectionTime(Date detectionTime)
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
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", detectionTime=" + detectionTime +
                ", rain=" + rain +
                ", temperature=" + temperature +
                ", relativeHumidity=" + relativeHumidity +
                ", wind=" + wind +
                '}';
    }
}
