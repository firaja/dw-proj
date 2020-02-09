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

DROP TABLE IF EXISTS vehicle_types;
CREATE TABLE IF NOT EXISTS vehicle_types
(
    id TINYINT UNSEIGNED,
    type_name VARCHAR(20) NOT NULL
)

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
    vehicle_type        TINYINT UNSIGNED,
    /*zone                VARCHAR(50),
    FOREIGN KEY (zone) REFERENCES zones (zone_id),*/
    PRIMARY KEY (id, query_time),
    FOREIGN KEY (vehicle_type) REFERENCES types (id)
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
    travelled_distance   MEDIUMINT UNSIGNED,
    vehicle_type        TINYINT UNSIGNED,
    UNIQUE (vehicle_id, city_id, start_profiling_time, end_profiling_time),
    FOREIGN KEY (vehicle_type) REFERENCES types (id),
    FOREIGN KEY (city_id) REFERENCES cities (name)
) ENGINE = INNODB;

DROP TABLE IF EXISTS vehicle_hourly_use;
CREATE TABLE IF NOT EXISTS vehicle_hourly_use
(
    vehicle_id         VARCHAR(30),
    city_id            VARCHAR(50),
    start_time         DATETIME,
    end_time           DATETIME,
    uses               SMALLINT UNSIGNED,
    travelled_distance MEDIUMINT UNSIGNED,
    vehicle_type        TINYINT UNSIGNED,
    FOREIGN KEY (vehicle_type) REFERENCES types (id),
    UNIQUE (vehicle_id, city_id, start_time, end_time, uses, travelled_distance)
);

DROP TABLE IF EXISTS city_hourly_use;
CREATE TABLE IF NOT EXISTS city_hourly_use
(
    city_id                  VARCHAR(50),
    start_time               DATETIME,
    end_time                 DATETIME,
    total_uses               SMALLINT UNSIGNED,
    total_travelled_distance MEDIUMINT UNSIGNED,
    total_vehicles           SMALLINT UNSIGNED,
    vehicle_type        TINYINT UNSIGNED,
    FOREIGN KEY (vehicle_type) REFERENCES types (id),
    UNIQUE (city_id, start_time, end_time)
);

DROP TABLE IF EXISTS weather_detections;
CREATE TABLE IF NOT EXISTS weather_detections
(
    id                MEDIUMINT UNSIGNED,
    city              VARCHAR(50),
    detection_time    DATETIME NOT NULL,
    rain              FLOAT,
    relative_humidity FLOAT,
    wind              FLOAT,
    temperature       FLOAT,
    PRIMARY KEY (id, detection_time),
    FOREIGN KEY (city) REFERENCES cities (name),
    INDEX (city, detection_time)
) ENGINE = INNODB;

DROP TABLE IF EXISTS weather_detections_aggregated;
CREATE TABLE IF NOT EXISTS weather_detections_aggregated
(
    city              VARCHAR(50),
    detection_time    DATETIME NOT NULL,
    rain              FLOAT,
    relative_humidity FLOAT,
    wind              FLOAT,
    temperature       FLOAT,
    PRIMARY KEY (city, detection_time)
) ENGINE = INNODB;

DROP TABLE IF EXISTS weather_final;
CREATE TABLE IF NOT EXISTS weather_final
(
    start_profiling_time DATETIME,
    end_profiling_time   DATETIME,
    city              VARCHAR(50),
    rain_level INT NOT NULL,
    wind_level INT NOT NULL,
    temperature_level INT NOT NULL,
    PRIMARY KEY (city,start_profiling_time,end_profiling_time)
) ENGINE = INNODB;

DROP TABLE IF EXISTS cities;
CREATE TABLE IF NOT EXISTS cities
(
    id SMALLINT UNSIGNED,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    INDEX (name)
) ENGINE = INNODB;

DROP TABLE IF EXISTS strikes;
CREATE TABLE IF NOT EXISTS strikes
(
    start_time DATETIME NOT NULL,
    end_time   DATETIME NOT NULL,
    name       VARCHAR(100),
    city       VARCHAR(50),
    area_of_interest VARCHAR(256),
    PRIMARY KEY (start_time, end_time, name, city)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS city_translation
(
    name        VARCHAR(50),
    translation VARCHAR(50) NOT NULL,
    PRIMARY KEY (name)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS precipitations
(
    minimum_rain INT NOT NULL,
    maximum_rain INT NOT NULL,
    level        INT NOT NULL,
    name         VARCHAR(50),
    PRIMARY KEY (level)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS temperatures
(
    minimum_temp FLOAT NOT NULL,
    maximum_temp FLOAT NOT NULL,
    level INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (level)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS winds
(
    minimum_wind FLOAT NOT NULL,
    maximum_wind FLOAT NOT NULL,
    level INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (level)
) ENGINE = INNODB;



