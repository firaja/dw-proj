-- Add cities from table city_hourly_use performing city name translation
INSERT INTO mydb.city
SELECT
	dw.city_translation.name
FROM dw.city_hourly_use
JOIN
	dw.city_translation
ON
	dw.city_hourly_use.city_id = dw.city_translation.name;

-- Add vehicle profilings date to date table
INSERT INTO mydb.date
SELECT
	dw.city_hourly_use.start_time,
	DAY(dw.city_hourly_use.start_time),
	MONT(dw.city_hourly_use.start_time),
	YEAR(dw.city_hourly_use.start_time),
	HOUR(dw.city_hourly_use.start_time)
FROM dw.city_hourly_use.start_time;

-- Add strikes to strike table
-- Use the city id related to the city field present the strikes table
INSERT INTO mydb.strike
SELECT
	dw.strike.name,
	dw.strike.start_time,
	dw.strike.end_time,
	mydb.city.id
FROM
	dw.strikes
JOIN
	mydb.city
ON
	mydb.city.name = dw.strikes.name;

-- Add weather detections to weather table
INSERT INTO mydb.weather
SELECT
	dw.weather_hourly_profiling.wind_level,
	dw.weather_hourly_profiling.temperature_level,
	dw.weather_hourly_profiling.rain_level,
	mydb.city.id
FROM
	dw.weather_hourly_profiling
JOIN
	dw.weather_hourly_profiling
ON
	dw.weather_hourly_profiling.city = mydb.city.name;

-- Add vehicles profilings to table vehicle_use
INSERT INTO mydb.vehicle_use
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
ON
	dw.city_translation.translation = mydb.city.name
JOIN
	dw.weather_hourly_profiling
ON
	dw.weather_hourly_profiling.start_profiling_time = dw.weather_hourly_profiling.start_profiling_time AND
	dw.weather_hourly_profiling.end_profiling_time = dw.weather_hourly_profiling.end_profiling_time
JOIN
	mydb.weather
ON
	dw.weather_hourly_profiling.wind_level = mydb.weather.wind_level AND
	dw.weather_hourly_profiling.temperature_level = mydb.weather.temperature_level AND
	dw.weather_hourly_profiling.rain_level = mydb.weather.rain_level
LEFT JOIN
	mydb.strike
ON
	dw.city_hourly_use.start_time >= mydb.strike.start_time AND
	dw.city_hourly_use.end_time <= mydb.strike.end_time;