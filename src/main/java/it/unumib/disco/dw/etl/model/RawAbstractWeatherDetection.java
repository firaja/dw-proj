package it.unumib.disco.dw.etl.model;

public abstract class RawAbstractWeatherDetection
{

    private RawWeatherStation station;

    public RawWeatherStation getStation()
    {
        return station;
    }

    public void setStation(RawWeatherStation station)
    {
        this.station = station;
    }

    @Override
    public String toString()
    {
        return "RawAbstractWeatherDetection{" +
                "station=" + station +
                '}';
    }
}
