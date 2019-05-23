/*
Navicat MySQL Data Transfer

Source Server         : monitor
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : monitor

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-04-08 00:00:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for radiation
-- ----------------------------
DROP TABLE IF EXISTS `radiation`;
CREATE TABLE `radiation` (
  `RAD_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UNIT_ID` char(16) NOT NULL,
  `RAD_VALUE` int(11) DEFAULT NULL,
  `UPLOAD_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`RAD_ID`) USING BTREE,
  KEY `RAD_UNIT_ID` (`UNIT_ID`) USING BTREE,
  CONSTRAINT `RAD_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sensor
-- ----------------------------
DROP TABLE IF EXISTS `sensor`;
CREATE TABLE `sensor` (
  `SENSOR_ID` char(16) NOT NULL DEFAULT 'SS',
  `RADIATION_MODEL_ID` varchar(32) DEFAULT NULL,
  `SENSOR_NAME` varchar(128) NOT NULL,
  `UNIT_ID` char(16) NOT NULL,
  `SENSOR_REMARK` varchar(1024) DEFAULT NULL,
  `SENSOR_SN` varchar(128) DEFAULT NULL,
  `SENSOR_MODEL` varchar(64) DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`SENSOR_ID`) USING BTREE,
  UNIQUE KEY `RADIATION_MODEL_ID_idx` (`RADIATION_MODEL_ID`) USING BTREE,
  KEY `SENSOR_UNIT_ID_idx` (`UNIT_ID`) USING BTREE,
  CONSTRAINT `SENSOR_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sensor_data
-- ----------------------------
DROP TABLE IF EXISTS `sensor_data`;
CREATE TABLE `sensor_data` (
  `DATA_ID` int(11) NOT NULL AUTO_INCREMENT,
  `RADIATION_MODEL_ID` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `DATA_VALUE` int(11) NOT NULL,
  `DATA_TIME` timestamp NOT NULL,
  PRIMARY KEY (`DATA_ID`),
  KEY `sensor_id_fk` (`RADIATION_MODEL_ID`),
  CONSTRAINT `sensor_id_fk` FOREIGN KEY (`RADIATION_MODEL_ID`) REFERENCES `sensor` (`RADIATION_MODEL_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sensor_seq
-- ----------------------------
DROP TABLE IF EXISTS `sensor_seq`;
CREATE TABLE `sensor_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for threshold
-- ----------------------------
DROP TABLE IF EXISTS `threshold`;
CREATE TABLE `threshold` (
  `DATA_ID` varchar(32) NOT NULL,
  `NORML` int(11) NOT NULL,
  `WARN` int(11) NOT NULL,
  PRIMARY KEY (`DATA_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `UNIT_ID` char(16) NOT NULL DEFAULT 'UT00000000000000',
  `UNIT_NAME` varchar(128) NOT NULL,
  `PARENT_ID` char(16) DEFAULT NULL,
  `REMARK` varchar(256) DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `LEAF` tinyint(1) NOT NULL DEFAULT '0',
  `UNIT_STATUS` int(2) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`UNIT_ID`) USING BTREE,
  KEY `PARENT_ID_INX` (`PARENT_ID`) USING BTREE,
  KEY `UNIT_NAME_IDX` (`UNIT_NAME`) USING BTREE,
  KEY `ACTIVE_IDX` (`ACTIVE`) USING BTREE,
  KEY `LEAF_IDX` (`LEAF`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for unit_manager
-- ----------------------------
DROP TABLE IF EXISTS `unit_manager`;
CREATE TABLE `unit_manager` (
  `UNIT_ID` char(16) NOT NULL,
  `USER_ID` char(6) NOT NULL,
  PRIMARY KEY (`UNIT_ID`,`USER_ID`) USING BTREE,
  KEY `UNIT_ID_IDX` (`UNIT_ID`) USING BTREE,
  KEY `MANAGER_ID_IDX` (`USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for unit_seq
-- ----------------------------
DROP TABLE IF EXISTS `unit_seq`;
CREATE TABLE `unit_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for unit_warn
-- ----------------------------
DROP TABLE IF EXISTS `unit_warn`;
CREATE TABLE `unit_warn` (
  `WARN_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UNIT_ID` char(16) NOT NULL,
  `UNIT_STATUS` int(2) NOT NULL,
  `NOTIFY_TIME` datetime NOT NULL,
  PRIMARY KEY (`WARN_ID`) USING BTREE,
  KEY `UNIT_ID_WARN_idx` (`UNIT_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=373 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USER_ID` char(6) NOT NULL,
  `USER_NAME` varchar(64) NOT NULL,
  `MAIL_ADDRESS` varchar(64) NOT NULL,
  `USER_ROLES` varchar(128) DEFAULT NULL,
  `USER_PASSWORD` varchar(64) NOT NULL,
  `PHONE_NUMBER` varchar(20) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL,
  PRIMARY KEY (`USER_ID`) USING BTREE,
  KEY `USERNAME` (`USER_NAME`) USING BTREE,
  KEY `USERMAIL` (`MAIL_ADDRESS`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Procedure structure for countLightByStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `countLightByStatus`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `countLightByStatus`(IN `parentUnitId` char(16),IN `stats` int,OUT `size` int)
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE cont INT DEFAULT 0;
    DECLARE unitId CHAR(16);
    DECLARE unitStatus INT(11);
    DECLARE unitLeaf BOOL;
    DECLARE curl CURSOR FOR SELECT `UNIT_ID`,`UNIT_STATUS`,`LEAF` FROM `UNIT` WHERE `ACTIVE`=true AND `PARENT_ID`=parentUnitId;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
    SET @@max_sp_recursion_depth = 100;

    OPEN curl;
    uLoop: LOOP
        FETCH curl INTO unitId,unitStatus,unitLeaf;
        IF done THEN
            LEAVE uLoop;
        ELSEIF unitLeaf AND unitStatus = stats THEN
            SET cont = cont + 1;
        ELSE
            CALL countLightByStatus(unitId, stats, @t_cont);
            SET cont = cont + @t_cont;
        END IF;
    END LOOP uLoop;
    CLOSE curl;
    SELECT cont INTO size;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for countSensor
-- ----------------------------
DROP PROCEDURE IF EXISTS `countSensor`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `countSensor`(IN `parentUnitId` char(16),IN `inactiveHours` int,OUT `activeCount` int,OUT `inactiveCount` int)
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE activeSize INT DEFAULT 0;
    DECLARE inactiveSize INT DEFAULT 0;
    DECLARE unitId CHAR(16);
    DECLARE unitLeaf BOOL;
    DECLARE curl CURSOR FOR SELECT `UNIT_ID`,`LEAF` FROM `UNIT` WHERE `ACTIVE`=true AND `PARENT_ID`=parentUnitId;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
    SET @@max_sp_recursion_depth = 10;

    OPEN curl;
    uLoop: LOOP
        FETCH curl INTO unitId,unitLeaf;
        IF done THEN
            LEAVE uLoop;
        ELSEIF unitLeaf THEN
            SELECT COUNT(1) INTO @t_count FROM (SELECT MAX(D.`DATA_TIME`) AS LAST_TIME FROM `SENSOR_DATA` D, `SENSOR` S WHERE D.`RADIATION_MODEL_ID`=S.`RADIATION_MODEL_ID` AND S.`UNIT_ID`=unitId GROUP BY D.`RADIATION_MODEL_ID`) T WHERE TIMESTAMPDIFF(HOUR, T.`LAST_TIME`, NOW()) < inactiveHours;
            SET activeSize = activeSize + @t_count;
            SELECT COUNT(1) INTO @t_count FROM (SELECT MAX(D.`DATA_TIME`) AS LAST_TIME FROM `SENSOR_DATA` D, `SENSOR` S WHERE D.`RADIATION_MODEL_ID`=S.`RADIATION_MODEL_ID` AND S.`UNIT_ID`=unitId GROUP BY D.`RADIATION_MODEL_ID`) T WHERE TIMESTAMPDIFF(HOUR, T.`LAST_TIME`, NOW()) >= inactiveHours;
            SET inactiveSize = inactiveSize + @t_count;
        ELSE
            CALL countSensor(unitId, inactiveHours, @activeCount, @inactiveCount);
            SET activeSize = activeSize + @activeCount;
            SET inactiveSize = inactiveSize + @inactiveCount;
        END IF;
    END LOOP uLoop;
    CLOSE curl;
    SELECT activeSize INTO activeCount;
    SELECT inactiveSize INTO inactiveCount;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getRadiationUnitStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `getRadiationUnitStatus`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRadiationUnitStatus`(IN unitId CHAR(16), IN norml INT, IN warn INT, OUT stats INT)
BEGIN
    DECLARE ret INT DEFAULT 0;
    DECLARE countTotal INT DEFAULT 0;
    DECLARE countWarn INT DEFAULT 0;
    DECLARE countError INT DEFAULT 0;

    SELECT COUNT(1) INTO countTotal FROM `radiation` WHERE `UNIT_ID`=unitId ORDER BY RAD_ID DESC  LIMIT 0, 10;
    SELECT COUNT(1) INTO countError FROM `radiation` WHERE `UNIT_ID`=unitId AND `RAD_VALUE` <= warn ORDER BY RAD_ID DESC LIMIT 0, 10;
    SELECT COUNT(1) INTO countWarn FROM `radiation` WHERE `UNIT_ID`=unitId AND `RAD_VALUE` <= norml ORDER BY RAD_ID DESC LIMIT 0, 10;
    
    IF countTotal = 0 THEN
        SET ret = -1;
    ELSEIF countTotal < 10 THEN
        SET ret = 0;
    ELSEIF countError >= 4 THEN
        SET ret = 2;
    ELSEIF countWarn >= 4 THEN
        SET ret = 1;
    END IF;

    SELECT ret INTO stats;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getUnitByManagerParentId
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnitByManagerParentId`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUnitByManagerParentId`(IN userId CHAR(6), IN unitParentId CHAR(16))
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE unitId CHAR(16);

    DECLARE curl CURSOR FOR SELECT U.`UNIT_ID` FROM `unit` U, `unit_manager` M WHERE  U.`UNIT_ID`=M.`UNIT_ID` AND U.`ACTIVE`=true AND U.`LEAF`=true AND M.`USER_ID`=userId;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;

    OPEN curl;
    uLoop: LOOP
        FETCH curl INTO unitId;
        IF done THEN
            LEAVE uLoop;
        ELSE
            CALL getUnitUntilParentId(unitId, unitParentId);
        END IF;
    END LOOP uLoop;
    CLOSE curl;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getUnitUntilParentId
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnitUntilParentId`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUnitUntilParentId`(IN unitId CHAR(16), IN unitParentId CHAR(16))
BEGIN
    DECLARE pId CHAR(16);
    DECLARE normalLightCount INT DEFAULT 0;
    DECLARE warnLightCount INT DEFAULT 0;
    DECLARE errorLightCount INT DEFAULT 0;

    SET @@max_sp_recursion_depth = 100;
    SELECT `PARENT_ID` INTO pId FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
    IF unitParentId=pId THEN
        CALL countLightByStatus(unitId, 0, @size);
        SET normalLightCount = normalLightCount + @size;
        CALL countLightByStatus(unitId, -1, @size);
        SET normalLightCount = normalLightCount + @size;

        CALL countLightByStatus(unitId, 1, @size);
        SET warnLightCount = warnLightCount + @size;

        CALL countLightByStatus(unitId, 2, @size);
        SET errorLightCount = errorLightCount + @size;

        SELECT `UNIT_ID`, `UNIT_NAME`, `PARENT_ID`, `REMARK`, `UNIT_STATUS`, `LEAF`, errorLightCount AS `ERROR_COUNT`, warnLightCount AS `WARN_COUNT`, normalLightCount AS `NORMAL_COUNT` FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
    ELSEIF pId IS NOT NULL THEN
        CALL getUnitUntilParentId(pId, unitParentId);
    END IF;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateParentStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateParentStatus`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateParentStatus`(IN unitId CHAR(16), IN stats INT)
BEGIN
    DECLARE pId CHAR(16);
    DECLARE pStatus INT;

    SELECT `PARENT_ID` INTO pId FROM `UNIT` WHERE `ACTIVE`=true AND `UNIT_ID`=unitId;
    WHILE pId != 'UT00000000000000' DO
        SELECT `UNIT_STATUS` INTO pStatus FROM `UNIT` WHERE `ACTIVE`=true AND `UNIT_ID`=pId;    
        IF pStatus < stats THEN
            UPDATE `UNIT` SET `UNIT_STATUS`=stats WHERE `UNIT_ID`=pId;
        ELSE
            SET stats=pStatus;
        END IF;
        SELECT `PARENT_ID` INTO pId FROM `UNIT` WHERE `ACTIVE`=true AND `UNIT_ID`=pId;
    END WHILE;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateUnitStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateUnitStatus`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateUnitStatus`()
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE stats INT;
    DECLARE norml INT;
    DECLARE warn INT;
    DECLARE thresholdId varchar(64);
    DECLARE leafUnitId CHAR(16);
    DECLARE curl CURSOR FOR SELECT `UNIT_ID` FROM `UNIT` WHERE `ACTIVE`=true AND `LEAF`=true;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;

    SELECT * INTO thresholdId, norml, warn FROM `threshold` WHERE `DATA_ID`='RADIATION';
    SET @@max_sp_recursion_depth = 100;

    OPEN curl;
    uLoop: LOOP
        FETCH curl INTO leafUnitId;
        IF done THEN
            LEAVE uLoop;
        ELSE
            CALL getRadiationUnitStatus(leafUnitId, norml, warn, stats);
            UPDATE `UNIT` SET `UNIT_STATUS`=stats WHERE `UNIT_ID`=leafUnitId;
            CALL updateParentStatus(leafUnitId, stats);
        END IF;
    END LOOP uLoop;
    CLOSE curl;    
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getUnitPath
-- ----------------------------
DROP FUNCTION IF EXISTS `getUnitPath`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getUnitPath`(id char(16)) RETURNS varchar(2048) CHARSET utf8mb4 COLLATE utf8mb4_bin
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE currentPId char(16);  
    DECLARE fullPath varchar(2048);
    DECLARE tempName varchar(2048);
    SET tempName='';

    SELECT `UNIT_NAME`, `PARENT_ID` INTO fullPath, currentPId FROM `unit` WHERE `UNIT_ID` = id AND ACTIVE=true;

    WHILE currentPId IS NOT NULL DO
        SELECT `UNIT_NAME`, `PARENT_ID` INTO tempName, currentPId FROM `unit` WHERE `UNIT_ID` = currentPId AND ACTIVE=true;
        IF tempName <> '/' THEN
            SET fullPath = CONCAT(tempName, '/', fullPath);
        ELSE
            SET fullPath = CONCAT(tempName, fullPath);
        END IF;
    END WHILE;

    RETURN fullPath;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getUnitIdChain
-- ----------------------------
DROP FUNCTION IF EXISTS `getUnitIdChain`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getUnitIdChain`(id char(16)) RETURNS varchar(1024) CHARSET utf8mb4 COLLATE utf8mb4_bin
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE currentPId char(16);  
    DECLARE unitIdChain varchar(1024);
    DECLARE tempId varchar(1024);
    SET tempId='';

    SELECT `UNIT_ID`, `PARENT_ID` INTO unitIdChain, currentPId FROM `unit` WHERE `UNIT_ID` = id AND ACTIVE=true;

    WHILE currentPId IS NOT NULL DO
        SELECT `UNIT_ID`, `PARENT_ID` INTO tempId, currentPId FROM `unit` WHERE `UNIT_ID` = currentPId AND ACTIVE=true;
        SET unitIdChain = CONCAT(tempId, ',', unitIdChain);
    END WHILE;

    RETURN unitIdChain;
END
;;
DELIMITER ;

DROP TRIGGER IF EXISTS `sensor_BEFORE_INSERT`;
DELIMITER ;;
CREATE TRIGGER `sensor_BEFORE_INSERT` BEFORE INSERT ON `sensor` FOR EACH ROW BEGIN
    INSERT INTO sensor_seq VALUES (NULL);
    SET NEW.SENSOR_ID = CONCAT('SS', LPAD(LAST_INSERT_ID(), 14, '0'));
END
;;
DELIMITER ;
