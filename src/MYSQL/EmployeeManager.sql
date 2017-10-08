-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 139.196.123.150    Database: EmployeeManager
-- ------------------------------------------------------
-- Server version	5.5.53

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `caseReport`
--

DROP TABLE IF EXISTS `caseReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caseReport` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` char(32) NOT NULL,
  `leaderName` char(16) DEFAULT NULL,
  `members` char(64) DEFAULT NULL,
  `category` tinyint(4) NOT NULL,
  `reportText` text,
  `reportPath` text,
  `scoreType` tinyint(1) NOT NULL,
  `singleScore` int(11) DEFAULT '0',
  `submitTime` datetime DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `isPass` tinyint(1) DEFAULT NULL,
  `COMMENT` text,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_2` (`userID`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `dID` char(3) NOT NULL,
  `userID` char(32) NOT NULL,
  `dName` char(32) NOT NULL,
  `isLeader` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dID`,`userID`),
  KEY `FK_Reference_7` (`userID`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generalReport`
--

DROP TABLE IF EXISTS `generalReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `generalReport` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` char(32) NOT NULL,
  `leaderName` char(16) DEFAULT NULL,
  `category` tinyint(4) NOT NULL,
  `reportText` text,
  `reportPath` text,
  `submitTime` datetime DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `isPass` tinyint(1) DEFAULT NULL,
  `COMMENT` text,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_1` (`userID`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `leaderReport`
--

DROP TABLE IF EXISTS `leaderReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leaderReport` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` char(32) NOT NULL,
  `members` char(64) DEFAULT NULL,
  `category` tinyint(4) NOT NULL,
  `reportText` text,
  `reportPath` text,
  `scoreType` tinyint(1) NOT NULL,
  `singleScore` int(11) DEFAULT '0',
  `submitTime` datetime DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `isPass` tinyint(1) DEFAULT NULL,
  `COMMENT` text,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_3` (`userID`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `photo`
--

DROP TABLE IF EXISTS `photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photo` (
  `photoID` int(11) NOT NULL,
  `userID` char(32) NOT NULL,
  `details` text,
  `photoPath` text NOT NULL,
  PRIMARY KEY (`photoID`),
  KEY `FK_Reference_4` (`userID`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `undealedCaseReport`
--

DROP TABLE IF EXISTS `undealedCaseReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `undealedCaseReport` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` char(32) NOT NULL,
  `leaderName` char(16) NOT NULL,
  `members` char(64) DEFAULT NULL,
  `category` tinyint(4) NOT NULL,
  `reportText` text,
  `reportPath` text,
  `scoreType` tinyint(1) NOT NULL,
  `singleScore` int(11) DEFAULT '0',
  `submitTime` datetime DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `isPass` tinyint(1) DEFAULT NULL,
  `COMMENT` text,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_6` (`userID`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `undealedGeneralReport`
--

DROP TABLE IF EXISTS `undealedGeneralReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `undealedGeneralReport` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` char(32) NOT NULL,
  `leaderName` char(16) NOT NULL,
  `category` tinyint(4) NOT NULL,
  `reportText` text,
  `reportPath` text,
  `submitTime` datetime DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `isPass` tinyint(1) DEFAULT NULL,
  `COMMENT` text,
  PRIMARY KEY (`reportID`),
  KEY `FK_Reference_8` (`userID`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userID` char(32) NOT NULL,
  `avatarURL` text,
  `userName` char(16) NOT NULL,
  `duty` char(64) DEFAULT NULL,
  `title` tinyint(4) DEFAULT NULL,
  `position` tinyint(4) DEFAULT NULL,
  `STATUS` tinyint(4) DEFAULT '0',
  `f_score` int(11) DEFAULT '0',
  `s_score` int(11) DEFAULT '0',
  `tel` char(11) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `email` char(32) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'EmployeeManager'
--

--
-- Dumping routines for database 'EmployeeManager'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-08 22:09:00
