USE dw;

-- Aggregate data from table weather_detections
INSERT INTO weather_detections_aggregated
SELECT
    city,
    detection_time,
    COALESCE(AVG(rain), 0),
    COALESCE(AVG(relative_humidity), 0),
    COALESCE(AVG(wind), 0),
    COALESCE(AVG(temperature), 0)
FROM weather_detections
GROUP BY city, detection_time;

-- Convert estimated relative detections form weather_detections_aggregated using defined level
INSERT INTO weather_final
SELECT
    DATE_FORMAT(weather_detections_aggregated.detection_time, '%Y-%m-%d %H:00:00'),
    DATE_FORMAT(DATE_ADD(weather_detections_aggregated.detection_time, INTERVAL 1 DAY), '%Y-%m-%d %H:00:00'),
    weather_detections_aggregated.city,
    precipitations.level,
    winds.level,
    temperatures.level
FROM
    weather_detections_aggregated
JOIN
    precipitations
ON
    weather_detections_aggregated.rain >= precipitations.minimum_rain AND
    weather_detections_aggregated.rain < precipitations.maximum_rain
JOIN
    winds
ON
    weather_detections_aggregated.wind >= winds.minimum_wind AND
    weather_detections_aggregated.wind < winds.maximum_wind
JOIN
    temperatures
ON
   weather_detections_aggregated.temperature >= temperatures.minimum_temp AND
   weather_detections_aggregated.temperature < temperatures.maximum_temp
WHERE
    weather_detections_aggregated.detection_time >= '2020-01-01 00:00:00' AND
    weather_detections_aggregated.detection_time <= '2020-02-08 00:00:00';