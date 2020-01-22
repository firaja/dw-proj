package it.unumib.disco.dw.etl.model;

import java.util.List;

public class HelbizRegion
{
    public class Point
    {
        private double lat;
        private double lon;

        public Point(double lat, double lon)
        {
            this.lat = lat;
            this.lon = lon;
        }

        public Point()
        {

        }

        @Override
        public String toString()
        {
            return "[lat: " + lat + " - lon: " + lon + "]";
        }
    }

    private String name;
    private String startTime;
    private String endTime;
    private String country;
    private Point center;
    private List<List<Double>> bounds;

    public HelbizRegion(String name, String startTime, String endTime, String country, Point center, List<List<Double>> bounds)
    {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.country = country;
        this.center = center;
        this.bounds = bounds;
    }

    public HelbizRegion()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public List<List<Double>> getBounds() {
        return bounds;
    }

    public void setBounds(List<List<Double>> bounds) {
        this.bounds = bounds;
    }
}
