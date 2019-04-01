/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : monitor

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 01/04/2019 09:33:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for radiation
-- ----------------------------
DROP TABLE IF EXISTS `radiation`;
CREATE TABLE `radiation`  (
  `RAD_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UNIT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `RAD_VALUE` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`RAD_ID`) USING BTREE,
  INDEX `RAD_UNIT_ID`(`UNIT_ID`) USING BTREE,
  CONSTRAINT `RAD_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sensor
-- ----------------------------
DROP TABLE IF EXISTS `sensor`;
CREATE TABLE `sensor`  (
  `SENSOR_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SS',
  `RADIATION_MODEL_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `SENSOR_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `UNIT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SENSOR_REMARK` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `SENSOR_SN` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `SENSOR_MODEL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`SENSOR_ID`) USING BTREE,
  UNIQUE INDEX `RADIATION_MODEL_ID_idx`(`RADIATION_MODEL_ID`) USING BTREE,
  INDEX `SENSOR_UNIT_ID_idx`(`UNIT_ID`) USING BTREE,
  CONSTRAINT `SENSOR_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sensor_seq
-- ----------------------------
DROP TABLE IF EXISTS `sensor_seq`;
CREATE TABLE `sensor_seq`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for threshold
-- ----------------------------
DROP TABLE IF EXISTS `threshold`;
CREATE TABLE `threshold`  (
  `DATA_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `NORML` int(11) NOT NULL,
  `WARN` int(11) NOT NULL,
  PRIMARY KEY (`DATA_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit`  (
  `UNIT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'UT00000000000000',
  `UNIT_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `PARENT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `REMARK` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT 1,
  `LEAF` tinyint(1) NOT NULL DEFAULT 0,
  `UNIT_STATUS` int(2) NOT NULL DEFAULT -1,
  PRIMARY KEY (`UNIT_ID`) USING BTREE,
  INDEX `PARENT_ID_INX`(`PARENT_ID`) USING BTREE,
  INDEX `UNIT_NAME_IDX`(`UNIT_NAME`) USING BTREE,
  INDEX `ACTIVE_IDX`(`ACTIVE`) USING BTREE,
  INDEX `LEAF_IDX`(`LEAF`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for unit_manager
-- ----------------------------
DROP TABLE IF EXISTS `unit_manager`;
CREATE TABLE `unit_manager`  (
  `UNIT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `USER_ID` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`UNIT_ID`, `USER_ID`) USING BTREE,
  INDEX `UNIT_ID_IDX`(`UNIT_ID`) USING BTREE,
  INDEX `MANAGER_ID_IDX`(`USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for unit_seq
-- ----------------------------
DROP TABLE IF EXISTS `unit_seq`;
CREATE TABLE `unit_seq`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for unit_warn
-- ----------------------------
DROP TABLE IF EXISTS `unit_warn`;
CREATE TABLE `unit_warn`  (
  `WARN_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UNIT_ID` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `UNIT_STATUS` int(2) NOT NULL,
  `NOTIFY_TIME` datetime(0) NOT NULL,
  PRIMARY KEY (`WARN_ID`) USING BTREE,
  INDEX `UNIT_ID_WARN_idx`(`UNIT_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `USER_ID` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `USER_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `MAIL_ADDRESS` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `USER_ROLES` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `USER_PASSWORD` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `PHONE_NUMBER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL,
  PRIMARY KEY (`USER_ID`) USING BTREE,
  INDEX `USERNAME`(`USER_NAME`) USING BTREE,
  INDEX `USERMAIL`(`MAIL_ADDRESS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Procedure structure for getRadiationUnitStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `getRadiationUnitStatus`;
delimiter ;;
CREATE PROCEDURE `getRadiationUnitStatus`(IN unitId CHAR(16), IN norml INT, IN warn INT, OUT stats INT)
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
delimiter ;

-- ----------------------------
-- Procedure structure for getUnitByManagerParentId
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnitByManagerParentId`;
delimiter ;;
CREATE PROCEDURE `getUnitByManagerParentId`(IN userId CHAR(6), IN unitParentId CHAR(16))
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
delimiter ;

-- ----------------------------
-- Function structure for getUnitPath
-- ----------------------------
DROP FUNCTION IF EXISTS `getUnitPath`;
delimiter ;;
CREATE FUNCTION `getUnitPath`(id char(16))
 RETURNS varchar(2048) CHARSET utf8mb4
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
delimiter ;

-- ----------------------------
-- Procedure structure for getUnitUntilParentId
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnitUntilParentId`;
delimiter ;;
CREATE PROCEDURE `getUnitUntilParentId`(IN unitId CHAR(16), IN unitParentId CHAR(16))
BEGIN
    DECLARE pId CHAR(16);
	SET @@max_sp_recursion_depth = 100;
    SELECT `PARENT_ID` INTO pId FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
    IF unitParentId=pId THEN
        SELECT `UNIT_ID`, `UNIT_NAME`, `PARENT_ID`, `REMARK`, `UNIT_STATUS` FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
    ELSEIF pId IS NOT NULL THEN
        CALL getUnitUntilParentId(pId, unitParentId);
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for updateParentStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateParentStatus`;
delimiter ;;
CREATE PROCEDURE `updateParentStatus`(IN unitId CHAR(16), IN stats INT)
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
delimiter ;

-- ----------------------------
-- Procedure structure for updateUnitStatus
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateUnitStatus`;
delimiter ;;
CREATE PROCEDURE `updateUnitStatus`()
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
delimiter ;

-- ----------------------------
-- Triggers structure for table sensor
-- ----------------------------
DROP TRIGGER IF EXISTS `sensor_BEFORE_INSERT`;
delimiter ;;
CREATE TRIGGER `sensor_BEFORE_INSERT` BEFORE INSERT ON `sensor` FOR EACH ROW BEGIN
    INSERT INTO sensor_seq VALUES (NULL);
    SET NEW.SENSOR_ID = CONCAT('SS', LPAD(LAST_INSERT_ID(), 14, '0'));
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
