CREATE TABLE `EmployeeManager`.`reservation` (
  `userID` CHAR(32) NOT NULL,
  `members` TINYINT(4) NOT NULL,
  `suits` TINYINT(4) NOT NULL,
  `time` DATETIME NOT NULL,
  `type` TINYINT(1) NOT NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE `EmployeeManager`.`previousReservation` (
  `userID` CHAR(32) NOT NULL,
  `members` TINYINT(4) NOT NULL,
  `suits` TINYINT(4) NOT NULL,
  `time` DATETIME NOT NULL,
  `type` TINYINT(1) NOT NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
