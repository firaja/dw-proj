-- Add cities from table city_hourly_use performing city name translation
INSERT INTO mydb.city (name)
SELECT DISTINCT
	dw.city_translation.translation
FROM dw.city_hourly_use
JOIN
	dw.city_translation
ON
	dw.city_hourly_use.city_id = dw.city_translation.name;

-- Add vehicle profilings date to date table
INSERT INTO mydb.date
SELECT DISTINCT
	dw.city_hourly_use.start_time,
	DAY(dw.city_hourly_use.start_time),
	MONTH(dw.city_hourly_use.start_time),
	YEAR(dw.city_hourly_use.start_time),
	HOUR(dw.city_hourly_use.start_time),
	LOWER(DATE_FORMAT(id, "%W"))
FROM dw.city_hourly_use;

-- Add strikes to strike table
-- Use the city id related to the city field present the strikes table
INSERT INTO mydb.strike (name, start_time, end_time, city)
SELECT
	dw.strikes.name,
	dw.strikes.start_time,
	dw.strikes.end_time,
	mydb.city.id
FROM
	dw.strikes
JOIN
	mydb.city
ON
	dw.strikes.city = mydb.city.name
COLLATE utf8_unicode_ci;

-- Add weather detections to weather table
INSERT INTO mydb.weather (wind_level, temperature_level, rain_level, city)
SELECT DISTINCT
	dw.weather_final.wind_level,
	dw.weather_final.temperature_level,
	dw.weather_final.rain_level,
	mydb.city.id
FROM
	dw.weather_final
JOIN
	mydb.city
ON
	dw.weather_final.city = mydb.city.name
COLLATE utf8_unicode_ci;

-- Add vehicles profilings to table vehicle_use
INSERT INTO mydb.vehicle_use (uses,travelled_distance,start_time,end_time,city,weather,strike)
SELECT
	dw.city_hourly_use.total_uses,
	dw.city_hourly_use.total_travelled_distance,
	dw.city_hourly_use.start_time,
	dw.city_hourly_use.end_time,
	mydb.city.id,
	mydb.weather.id,
	mydb.strike.id
FROM
	dw.city_hourly_use
JOIN
	dw.city_translation
ON
	dw.city_hourly_use.city_id = dw.city_translation.name
JOIN
	mydb.city
JOIN
	dw.weather_final
ON
	dw.city_hourly_use.start_time >= dw.weather_final.start_profiling_time AND
	dw.city_hourly_use.end_time <= dw.weather_final.end_profiling_time
JOIN
	mydb.weather
ON
	dw.weather_final.wind_level = mydb.weather.wind_level AND
	dw.weather_final.temperature_level = mydb.weather.temperature_level AND
	dw.weather_final.rain_level = mydb.weather.rain_level
LEFT JOIN
	mydb.strike
ON
	dw.city_hourly_use.start_time >= mydb.strike.start_time AND
	dw.city_hourly_use.end_time <= mydb.strike.end_time
WHERE mydb.city.name = 'Torino';