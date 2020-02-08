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
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`city`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`city` ;

CREATE TABLE IF NOT EXISTS `mydb`.`city` (
  `id` INT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`weather`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`weather` ;

CREATE TABLE IF NOT EXISTS `mydb`.`weather` (
  `id` INT NOT NULL,
  `wind_level` INT NOT NULL,
  `temperature_level` INT NOT NULL,
  `rain_level` INT NOT NULL,
  `city` INT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_weather_1_idx` (`city` ASC),
  INDEX `fk_weather_2_idx` (`start_time` ASC),
  INDEX `fk_weather_3_idx` (`end_time` ASC),
  CONSTRAINT `fk_weather_1`
    FOREIGN KEY (`city`)
    REFERENCES `mydb`.`city` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_weather_2`
    FOREIGN KEY (`start_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_weather_3`
    FOREIGN KEY (`end_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`strike`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`strike` ;

CREATE TABLE IF NOT EXISTS `mydb`.`strike` (
  `id` INT NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `city` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_strike_1_idx` (`start_time` ASC),
  INDEX `fk_strike_2_idx` (`end_time` ASC),
  INDEX `fk_strike_3_idx` (`city` ASC),
  CONSTRAINT `fk_strike_1`
    FOREIGN KEY (`start_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_strike_2`
    FOREIGN KEY (`end_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_strike_3`
    FOREIGN KEY (`city`)
    REFERENCES `mydb`.`city` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`vehicle_use`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`vehicle_use` ;

CREATE TABLE IF NOT EXISTS `mydb`.`vehicle_use` (
  `id` INT NOT NULL,
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
  INDEX `fk_vehicle_use_5_idx` (`strike` ASC),
  CONSTRAINT `fk_vehicle_use_1`
    FOREIGN KEY (`start_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_use_2`
    FOREIGN KEY (`end_time`)
    REFERENCES `mydb`.`date` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_use_3`
    FOREIGN KEY (`city`)
    REFERENCES `mydb`.`city` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_use_4`
    FOREIGN KEY (`weather`)
    REFERENCES `mydb`.`weather` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_use_5`
    FOREIGN KEY (`strike`)
    REFERENCES `mydb`.`strike` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
