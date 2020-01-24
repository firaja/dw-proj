package it.unumib.disco.dw.etl.utils;

public class CalculationUtils
{
    /**
     * It calculates the distance between two geographic coordinates
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return distance between two points
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2)
    {
        if (lat1 == lat2 && lon1 == lon2)
        {
            return 0.0;
        }
        else
        {
            double lat1Rad, lon1Rad, lat2Rad, lon2Rad;
            lat1Rad = Math.PI * lat1 / 180.0;
            lon1Rad = Math.PI * lon1 / 180.0;
            lat2Rad = Math.PI * lat2 / 180.0;
            lon2Rad = Math.PI * lon2 / 180.0;
            double phi = Math.abs(lon1Rad - lon2Rad);
            double p = Math.acos(Math.sin(lat2Rad) * Math.sin(lat1Rad) +
                    Math.cos(lat2Rad) * Math.cos(lat1Rad) * Math.cos(phi));
            return Math.round(p * 6371000);
        }
    }
}
