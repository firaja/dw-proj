/* Helbiz */

CREATE DATABASE IF NOT EXISTS dw CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE dw;

CREATE TABLE IF NOT EXISTS vehicles (
    id VARCHAR(30),
    city VARCHAR(50),
    latitude DOUBLE,
    longitude DOUBLE,
    battery_level_miles SMALLINT UNSIGNED,
    power TINYINT UNSIGNED,
    miles_range MEDIUMINT UNSIGNED,
    query_time DATETIME,
    UNIQUE(id, city, query_time)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS vehicles_profilings (
    vehicle_id VARCHAR(30),
    city_id VARCHAR(50),
    start_profiling_time DATETIME,
    end_profiling_time DATETIME,
    position_changed BOOLEAN,
    new BOOLEAN,
    UNIQUE(vehicle_id, city_id, start_profiling_time, end_profiling_time)
);