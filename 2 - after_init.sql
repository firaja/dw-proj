USE dw;

INSERT INTO weather_hourly_profiling
SELECT
    DATE_FORMAT(weather_detections.detection_time, '%Y-%m-%d %H:00:00'),
    DATE_FORMAT(DATE_ADD(weather_detections.detection_time, INTERVAL 1 HOUR), '%Y-%m-%d %H:00:00'),
    weather_detections.city,
    precipitations.level,
    winds.level,
    temperatures.level
FROM
    weather_detections
JOIN
    precipitations
    ON
        weather_detections.rain >= precipitations.minimum_rain AND
        weather_detections.rain < precipitations.maximum_rain
JOIN
    winds
    ON
        weather_detections.wind >= winds.minimum_wind AND
        weather_detections.wind < winds.maximum_wind
JOIN
    temperatures
    ON
       weather_detections.temperature >= temperatures.minimum_temp AND
       weather_detections.temperature < temperatures.maximum_temp;