-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: monitor
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `unit` (
  `UNIT_ID` char(16) NOT NULL DEFAULT 'UT00000000000000',
  `UNIT_NAME` varchar(128) NOT NULL,
  `PARENT_ID` char(16) DEFAULT NULL,
  `REMARK` varchar(256) DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `LEAF` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UNIT_ID`),
  KEY `PARENT_ID_INX` (`PARENT_ID`) /*!80000 INVISIBLE */,
  KEY `UNIT_NAME_IDX` (`UNIT_NAME`),
  KEY `ACTIVE_IDX` (`ACTIVE`) /*!80000 INVISIBLE */,
  KEY `LEAF_IDX` (`LEAF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES ('UT00000000000001','/',NULL,'系统根节点',1,0),('UT00000000000002','XXXXXXXXXXXXXXXX医院','UT00000000000001','安徽省合肥市XXX路YY号',1,0),('UT00000000000003','YYYYYYYYYYY科室','UT00000000000002','XXXXXXXXXXYYYYYYYYYYYYYYYY',1,0),('UT00000000000004','rererererer','UT00000000000003','rerererererrerererererrerererererrerererererrerererererrererererer',0,0),('UT00000000000005','谢谢谢谢谢谢','UT00000000000001','xxxxxxxxxxxxxxxxxx嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻嘻',1,0),('UT00000000000006','涛涛日日日仍然','UT00000000000005','xxxxxxxxxxxx',1,1);
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`shen`@`localhost`*/ /*!50003 TRIGGER `unit_BEFORE_INSERT` BEFORE INSERT ON `unit` FOR EACH ROW BEGIN
	INSERT INTO unit_seq VALUES (NULL);
    SET NEW.UNIT_ID = CONCAT('UT', LPAD(LAST_INSERT_ID(), 14, '0'));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-11  1:48:42
