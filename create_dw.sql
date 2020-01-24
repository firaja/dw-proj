SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT;
SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS;
SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION;
SET NAMES utf8;
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';
SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0;

/* Helbiz */

DROP DATABASE IF EXISTS dw;
CREATE DATABASE IF NOT EXISTS dw CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE dw;

DROP TABLE IF EXISTS cities;
CREATE TABLE IF NOT EXISTS cities
(
    name    VARCHAR(50) NOT NULL,
    PRIMARY KEY (name),
    INDEX (name)
) ENGINE = INNODB;

/*
Query to add cities:

INSERT INTO cities VALUES ('torino');
 */

DROP TABLE IF EXISTS vehicles;
CREATE TABLE IF NOT EXISTS vehicles
(
    id                  VARCHAR(30),
    city                VARCHAR(50),
    latitude            DOUBLE,
    longitude           DOUBLE,
    battery_level_miles SMALLINT UNSIGNED,
    power               TINYINT UNSIGNED,
    miles_range         MEDIUMINT UNSIGNED,
    query_time          DATETIME,
    zone                VARCHAR(50),
    FOREIGN KEY (zone) REFERENCES zones (zone_id),
    UNIQUE (id, city, query_time)
) ENGINE = INNODB;

DROP TABLE IF EXISTS vehicles_profilings;
CREATE TABLE IF NOT EXISTS vehicles_profilings
(
    vehicle_id           VARCHAR(30),
    city_id              VARCHAR(50),
    start_profiling_time DATETIME,
    end_profiling_time   DATETIME,
    position_changed     BOOLEAN,
    new                  BOOLEAN,
    travelled_distance MEDIUMINT UNSIGNED,
    UNIQUE (vehicle_id, city_id, start_profiling_time, end_profiling_time),
    FOREIGN KEY (city_id) REFERENCES cities (name)
) ENGINE = INNODB;

DROP TABLE IF EXISTS vehicle_hourly_use;
CREATE TABLE IF NOT EXISTS vehicle_hourly_use (
    vehicle_id           VARCHAR(30),
    city_id              VARCHAR(50),
    start_time           DATETIME,
    end_time             DATETIME,
    uses                 SMALLINT UNSIGNED,
    travelled_distance   MEDIUMINT UNSIGNED,
    UNIQUE(vehicle_id, city_id, start_time, end_time, uses, travelled_distance)
);

DROP TABLE IF EXISTS city_hourly_use;
CREATE TABLE IF NOT EXISTS city_hourly_use (
    city_id                  VARCHAR(50),
    start_time               DATETIME,
    end_time                 DATETIME,
    total_uses               SMALLINT UNSIGNED,
    total_travelled_distance MEDIUMINT UNSIGNED,
    total_vehicles           SMALLINT UNSIGNED,
    UNIQUE(city_id, start_time, end_time)
);

DROP TABLE IF EXISTS weather_detections;
CREATE TABLE IF NOT EXISTS weather_detections
(
    zone              VARCHAR(50),
    detection_time    DATETIME NOT NULL,
    rain              FLOAT,
    relative_humidity FLOAT,
    wind              FLOAT,
    PRIMARY KEY (zone, detection_time),
    FOREIGN KEY (zone) REFERENCES zones (zone_id),
    INDEX (zone, detection_time)
) ENGINE = INNODB;


DROP TABLE IF EXISTS zones;
CREATE TABLE IF NOT EXISTS zones
(
    zone_id VARCHAR(50)  NOT NULL,
    city    VARCHAR(50)  NOT NULL,
    center  POINT        NOT NULL,
    radius  INT UNSIGNED NOT NULL,
    PRIMARY KEY (zone_id),
    INDEX (zone_id),
    FOREIGN KEY (city) REFERENCES cities (name)
) ENGINE = INNODB;

DROP TABLE IF EXISTS strikes;
CREATE TABLE IF NOT EXISTS strikes
(
    start_time DATETIME NOT NULL,
    end_time   DATETIME NOT NULL,
    type       VARCHAR(50),
    city       VARCHAR(50),
    PRIMARY KEY (start_time, end_time, type, city),
    FOREIGN KEY (city) REFERENCES cities (name)
) ENGINE = INNODB;



