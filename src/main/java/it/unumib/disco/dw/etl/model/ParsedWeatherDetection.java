package it.unumib.disco.dw.etl.model;

import java.util.Date;

public class ParsedWeatherDetection
{

    private double latitude;

    private double longitude;

    private Date detectionTime;

    private float rain;

    private float temperature;

    private float relativeHumidity;

    private float wind;

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
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

    public float getRain()
    {
        return rain;
    }

    public void setRain(float rain)
    {
        this.rain = rain;
    }

    public float getTemperature()
    {
        return temperature;
    }

    public void setTemperature(float temperature)
    {
        this.temperature = temperature;
    }

    public float getRelativeHumidity()
    {
        return relativeHumidity;
    }

    public void setRelativeHumidity(float relativeHumidity)
    {
        this.relativeHumidity = relativeHumidity;
    }

    public float getWind()
    {
        return wind;
    }

    public void setWind(float wind)
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
