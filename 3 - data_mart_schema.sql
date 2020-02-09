SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`date`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`date` ;

CREATE TABLE IF NOT EXISTS `mydb`.`date` (
  `id` DATETIME NOT NULL,
  `day` INT NOT NULL,
  `month` INT NOT NULL,
  `year` INT NOT NULL,
  `hour` INT NOT NULL,
  `weekday` VARCHAR(20),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`city`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`city` ;

CREATE TABLE IF NOT EXISTS `mydb`.`city` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`weather`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`weather` ;

CREATE TABLE IF NOT EXISTS `mydb`.`weather` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `wind_level` INT NOT NULL,
  `temperature_level` INT NOT NULL,
  `rain_level` INT NOT NULL,
  `city` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_weather_idx` (`city` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`strike`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`strike` ;

CREATE TABLE IF NOT EXISTS `mydb`.`strike` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `city` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_strike_1_idx` (`start_time` ASC),
  INDEX `fk_strike_2_idx` (`end_time` ASC),
  INDEX `fk_strike_3_idx` (`city` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`vehicle_use`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`vehicle_use` ;

CREATE TABLE IF NOT EXISTS `mydb`.`vehicle_use` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uses` INT NOT NULL,
  `travelled_distance` INT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `city` INT NOT NULL,
  `weather` INT NOT NULL,
  `strike` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_vehicle_use_1_idx` (`start_time` ASC),
  INDEX `fk_vehicle_use_2_idx` (`end_time` ASC),
  INDEX `fk_vehicle_use_3_idx` (`city` ASC),
  INDEX `fk_vehicle_use_4_idx` (`weather` ASC),
  INDEX `fk_vehicle_use_5_idx` (`strike` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
