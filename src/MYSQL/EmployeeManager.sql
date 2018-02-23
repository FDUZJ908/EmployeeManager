-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 139.196.123.150    Database: EmployeeManager
-- ------------------------------------------------------
-- Server version	5.5.53

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `QRCode`
--

DROP TABLE IF EXISTS `QRCode`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QRCode` (
  `QRID`   INT(11)  NOT NULL AUTO_INCREMENT,
  `s_time` DATETIME NOT NULL,
  `e_time` DATETIME NOT NULL,
  `token`  INT(11)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`QRID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `caseReport`
--

DROP TABLE IF EXISTS `caseReport`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caseReport` (
  `reportID`    INT(11)    NOT NULL AUTO_INCREMENT,
  `userID`      CHAR(32)   NOT NULL,
  `leaderName`  CHAR(16)            DEFAULT NULL,
  `members`     CHAR(64)            DEFAULT NULL,
  `category`    CHAR(32)   NOT NULL,
  `reportText`  TEXT,
  `reportPath`  TEXT,
  `scoreType`   TINYINT(1) NOT NULL,
  `singleScore` INT(11)             DEFAULT '0',
  `submitTime`  DATETIME            DEFAULT NULL,
  `checkTime`   DATETIME            DEFAULT NULL,
  `isPass`      TINYINT(1)          DEFAULT NULL,
  `comment`     TEXT,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_2` (`userID`),
  KEY `FK_Reference_9` (`category`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`category`) REFERENCES `reportType` (`typeName`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `dID`      CHAR(3)    NOT NULL,
  `userID`   CHAR(32)   NOT NULL,
  `dName`    CHAR(32)   NOT NULL,
  `isLeader` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dID`, `userID`),
  KEY `FK_Reference_7` (`userID`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generalReport`
--

DROP TABLE IF EXISTS `generalReport`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `generalReport` (
  `reportID`   INT(11)  NOT NULL AUTO_INCREMENT,
  `userID`     CHAR(32) NOT NULL,
  `leaderName` CHAR(16)          DEFAULT NULL,
  `category`   CHAR(32) NOT NULL,
  `reportText` TEXT,
  `reportPath` TEXT,
  `submitTime` DATETIME          DEFAULT NULL,
  `checkTime`  DATETIME          DEFAULT NULL,
  `isPass`     TINYINT(1)        DEFAULT NULL,
  `comment`    TEXT,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_1` (`userID`),
  KEY `FK_Reference_11` (`category`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`category`) REFERENCES `reportType` (`typeName`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leaderReport`
--

DROP TABLE IF EXISTS `leaderReport`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leaderReport` (
  `reportID`    INT(11)    NOT NULL AUTO_INCREMENT,
  `userID`      CHAR(32)   NOT NULL,
  `members`     CHAR(64)            DEFAULT NULL,
  `category`    CHAR(32)   NOT NULL,
  `reportText`  TEXT,
  `reportPath`  TEXT,
  `scoreType`   TINYINT(1) NOT NULL,
  `singleScore` INT(11)             DEFAULT '0',
  `submitTime`  DATETIME            DEFAULT NULL,
  `checkTime`   DATETIME            DEFAULT NULL,
  `isPass`      TINYINT(1)          DEFAULT '1',
  `comment`     TEXT,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_3` (`userID`),
  KEY `FK_Reference_13` (`category`),
  CONSTRAINT `FK_Reference_13` FOREIGN KEY (`category`) REFERENCES `reportType` (`typeName`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilege` (
  `pID`              INT(11) NOT NULL AUTO_INCREMENT,
  `privilege`        INT(11) NOT NULL,
  `weekday`          TINYINT(4)       DEFAULT NULL,
  `pushTime`         TIME             DEFAULT NULL,
  `leaderPostLimit`  INT(11)          DEFAULT NULL,
  `leaderScoreLimit` INT(11)          DEFAULT NULL,
  PRIMARY KEY (`pID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reportType`
--

DROP TABLE IF EXISTS `reportType`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reportType` (
  `typeName`   CHAR(32) NOT NULL,
  `typeValue`  INT(11)  NOT NULL DEFAULT '0',
  `typeRemark` CHAR(64)          DEFAULT NULL,
  PRIMARY KEY (`typeName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sysVar`
--

DROP TABLE IF EXISTS `sysVar`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sysVar` (
  `varName` CHAR(32) NOT NULL,
  `value`   INT(11)  DEFAULT NULL,
  `string`  CHAR(32) DEFAULT NULL,
  PRIMARY KEY (`varName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `undealedCaseReport`
--

DROP TABLE IF EXISTS `undealedCaseReport`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `undealedCaseReport` (
  `reportID`    INT(11)    NOT NULL AUTO_INCREMENT,
  `userID`      CHAR(32)   NOT NULL,
  `leaderName`  CHAR(16)   NOT NULL,
  `members`     CHAR(64)            DEFAULT NULL,
  `category`    CHAR(32)   NOT NULL,
  `reportText`  TEXT,
  `reportPath`  TEXT,
  `scoreType`   TINYINT(1) NOT NULL,
  `singleScore` INT(11)             DEFAULT '0',
  `submitTime`  DATETIME            DEFAULT NULL,
  `checkTime`   DATETIME            DEFAULT NULL,
  `isPass`      TINYINT(1)          DEFAULT NULL,
  `comment`     TEXT,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_6` (`userID`),
  KEY `FK_Reference_15` (`category`),
  CONSTRAINT `FK_Reference_15` FOREIGN KEY (`category`) REFERENCES `reportType` (`typeName`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `undealedGeneralReport`
--

DROP TABLE IF EXISTS `undealedGeneralReport`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `undealedGeneralReport` (
  `reportID`   INT(11)  NOT NULL AUTO_INCREMENT,
  `userID`     CHAR(32) NOT NULL,
  `leaderName` CHAR(16) NOT NULL,
  `category`   CHAR(32) NOT NULL,
  `reportText` TEXT,
  `reportPath` TEXT,
  `submitTime` DATETIME          DEFAULT NULL,
  `checkTime`  DATETIME          DEFAULT NULL,
  `isPass`     TINYINT(1)        DEFAULT NULL,
  `comment`    TEXT,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_8` (`userID`),
  KEY `FK_Reference_17` (`category`),
  CONSTRAINT `FK_Reference_17` FOREIGN KEY (`category`) REFERENCES `reportType` (`typeName`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userID`    CHAR(32) NOT NULL,
  `avatarURL` TEXT,
  `userName`  CHAR(16) NOT NULL UNIQUE,
  `duty`      CHAR(64)          DEFAULT NULL,
  `title`     TINYINT(4)        DEFAULT NULL,
  `position`  TINYINT(4)        DEFAULT NULL,
  `STATUS`    TINYINT(4)        DEFAULT '0',
  `f_score`   INT(11)           DEFAULT '0',
  `s_score`   INT(11)           DEFAULT '0',
  `tel`       CHAR(11)          DEFAULT NULL,
  `gender`    CHAR(1)           DEFAULT '0',
  `email`     CHAR(32)          DEFAULT NULL,
  `remark`    TEXT,
  `privilege` INT(11)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`userID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'EmployeeManager'
--

--
-- Dumping routines for database 'EmployeeManager'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2018-01-23  0:19:32

CREATE TABLE ministry (
  `dID`   INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `dName` CHAR(32) NOT NULL UNIQUE
)
  CHARSET = utf8mb4;

INSERT INTO ministry
  SELECT
    dID,
    min(dName) AS dName
  FROM department
  GROUP BY dID;

ALTER TABLE department
  CHANGE dID dID INT NOT NULL;
ALTER TABLE department
  ADD CONSTRAINT FOREIGN KEY FK_dID(dID) REFERENCES ministry (dID);

ALTER TABLE ministry
  CHANGE dName dName CHAR(32) NOT NULL UNIQUE;

ALTER TABLE user
  CHANGE gender gender CHAR(1) DEFAULT '0';

ALTER TABLE user
  CHANGE userName `userName` CHAR(16) NOT NULL UNIQUE;
