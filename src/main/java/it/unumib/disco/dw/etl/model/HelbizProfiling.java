package it.unumib.disco.dw.etl.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelbizProfiling
{
    private String vehicleId;
    private String cityId;
    private Date startTime;
    private Date endTime;
    private boolean positionChanged;
    private boolean newVehicle;
    private int travelledDistance;

    public HelbizProfiling(String vehicleId, String cityId, Date startTime, Date endTime, boolean positionChanged, boolean newVehicle, int travelledDistance)
    {
        this.vehicleId = vehicleId;
        this.cityId = cityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.positionChanged = positionChanged;
        this.newVehicle = newVehicle;
        this.travelledDistance = travelledDistance;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isPositionChanged() {
        return positionChanged;
    }

    public void setPositionChanged(boolean positionChanged) {
        this.positionChanged = positionChanged;
    }

    public boolean isNewVehicle() {
        return newVehicle;
    }

    public void setNewVehicle(boolean newVehicle) {
        this.newVehicle = newVehicle;
    }

    public int getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(int travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format(
                "[vehicleId: %s, cityId: %s, startTime: %s, endTime: %s, positionChanged: %b, new: %b, travelledDistance: %d]",
                vehicleId, cityId, sdf.format(startTime), sdf.format(endTime), positionChanged, newVehicle, travelledDistance);
    }
}
