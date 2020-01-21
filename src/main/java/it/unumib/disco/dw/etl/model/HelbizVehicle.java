package it.unumib.disco.dw.etl.model;

public class HelbizVehicle
{
    private String id;
    private double lat;
    private double lon;
    private int batteryLevelInMiles;
    private int range;
    private int power;
    private String geofence;

    public HelbizVehicle(String id, double lat, double lon, int batteryLevelInMiles, int range, int power, String geofence)
    {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.batteryLevelInMiles = batteryLevelInMiles;
        this.range = range;
        this.power = power;
        this.geofence = geofence;
    }

    public HelbizVehicle()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getBatteryLevelInMiles() {
        return batteryLevelInMiles;
    }

    public void setBatteryLevelInMiles(int batteryLevelInMiles) {
        this.batteryLevelInMiles = batteryLevelInMiles;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getGeofence() {
        return geofence;
    }

    public void setGeofence(String geofence) {
        this.geofence = geofence;
    }
}
