/*
Navicat MySQL Data Transfer

Source Server         : monitor
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : monitor

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-03-30 22:55:16
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
  PRIMARY KEY (`RAD_ID`),
  KEY `RAD_UNIT_ID` (`UNIT_ID`),
  CONSTRAINT `RAD_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of radiation
-- ----------------------------
INSERT INTO `radiation` VALUES ('1', 'UT00000000000007', '55');
INSERT INTO `radiation` VALUES ('2', 'UT00000000000007', '64');
INSERT INTO `radiation` VALUES ('3', 'UT00000000000007', '77');
INSERT INTO `radiation` VALUES ('4', 'UT00000000000007', '64');
INSERT INTO `radiation` VALUES ('5', 'UT00000000000007', '52');
INSERT INTO `radiation` VALUES ('6', 'UT00000000000007', '37');
INSERT INTO `radiation` VALUES ('7', 'UT00000000000007', '93');
INSERT INTO `radiation` VALUES ('8', 'UT00000000000007', '78');
INSERT INTO `radiation` VALUES ('9', 'UT00000000000007', '11');
INSERT INTO `radiation` VALUES ('10', 'UT00000000000007', '48');
INSERT INTO `radiation` VALUES ('11', 'UT00000000000007', '17');
INSERT INTO `radiation` VALUES ('12', 'UT00000000000007', '22');
INSERT INTO `radiation` VALUES ('13', 'UT00000000000007', '91');
INSERT INTO `radiation` VALUES ('14', 'UT00000000000007', '29');
INSERT INTO `radiation` VALUES ('15', 'UT00000000000007', '42');
INSERT INTO `radiation` VALUES ('16', 'UT00000000000007', '85');
INSERT INTO `radiation` VALUES ('17', 'UT00000000000007', '77');
INSERT INTO `radiation` VALUES ('18', 'UT00000000000007', '87');
INSERT INTO `radiation` VALUES ('19', 'UT00000000000007', '79');
INSERT INTO `radiation` VALUES ('20', 'UT00000000000007', '32');
INSERT INTO `radiation` VALUES ('21', 'UT00000000000007', '70');
INSERT INTO `radiation` VALUES ('22', 'UT00000000000007', '23');
INSERT INTO `radiation` VALUES ('23', 'UT00000000000007', '63');
INSERT INTO `radiation` VALUES ('24', 'UT00000000000007', '39');
INSERT INTO `radiation` VALUES ('25', 'UT00000000000007', '43');
INSERT INTO `radiation` VALUES ('26', 'UT00000000000007', '11');
INSERT INTO `radiation` VALUES ('27', 'UT00000000000007', '44');
INSERT INTO `radiation` VALUES ('28', 'UT00000000000007', '49');
INSERT INTO `radiation` VALUES ('29', 'UT00000000000007', '25');
INSERT INTO `radiation` VALUES ('30', 'UT00000000000007', '24');
INSERT INTO `radiation` VALUES ('31', 'UT00000000000007', '54');
INSERT INTO `radiation` VALUES ('32', 'UT00000000000007', '90');
INSERT INTO `radiation` VALUES ('33', 'UT00000000000007', '88');
INSERT INTO `radiation` VALUES ('34', 'UT00000000000007', '66');
INSERT INTO `radiation` VALUES ('35', 'UT00000000000007', '35');
INSERT INTO `radiation` VALUES ('36', 'UT00000000000007', '33');
INSERT INTO `radiation` VALUES ('37', 'UT00000000000007', '65');
INSERT INTO `radiation` VALUES ('38', 'UT00000000000007', '73');
INSERT INTO `radiation` VALUES ('39', 'UT00000000000007', '9');
INSERT INTO `radiation` VALUES ('40', 'UT00000000000007', '70');
INSERT INTO `radiation` VALUES ('41', 'UT00000000000007', '80');
INSERT INTO `radiation` VALUES ('42', 'UT00000000000007', '15');
INSERT INTO `radiation` VALUES ('43', 'UT00000000000007', '72');
INSERT INTO `radiation` VALUES ('44', 'UT00000000000007', '94');
INSERT INTO `radiation` VALUES ('45', 'UT00000000000007', '94');
INSERT INTO `radiation` VALUES ('46', 'UT00000000000007', '12');
INSERT INTO `radiation` VALUES ('47', 'UT00000000000007', '18');
INSERT INTO `radiation` VALUES ('48', 'UT00000000000007', '65');
INSERT INTO `radiation` VALUES ('49', 'UT00000000000007', '31');
INSERT INTO `radiation` VALUES ('50', 'UT00000000000007', '92');
INSERT INTO `radiation` VALUES ('51', 'UT00000000000007', '17');
INSERT INTO `radiation` VALUES ('52', 'UT00000000000007', '33');
INSERT INTO `radiation` VALUES ('53', 'UT00000000000007', '27');
INSERT INTO `radiation` VALUES ('54', 'UT00000000000007', '93');
INSERT INTO `radiation` VALUES ('55', 'UT00000000000007', '1');
INSERT INTO `radiation` VALUES ('56', 'UT00000000000007', '82');
INSERT INTO `radiation` VALUES ('57', 'UT00000000000007', '52');
INSERT INTO `radiation` VALUES ('58', 'UT00000000000007', '72');
INSERT INTO `radiation` VALUES ('59', 'UT00000000000007', '50');
INSERT INTO `radiation` VALUES ('60', 'UT00000000000007', '91');
INSERT INTO `radiation` VALUES ('61', 'UT00000000000007', '58');
INSERT INTO `radiation` VALUES ('62', 'UT00000000000007', '47');
INSERT INTO `radiation` VALUES ('63', 'UT00000000000007', '43');
INSERT INTO `radiation` VALUES ('64', 'UT00000000000007', '95');
INSERT INTO `radiation` VALUES ('65', 'UT00000000000007', '85');
INSERT INTO `radiation` VALUES ('66', 'UT00000000000007', '85');
INSERT INTO `radiation` VALUES ('67', 'UT00000000000007', '97');
INSERT INTO `radiation` VALUES ('68', 'UT00000000000007', '86');
INSERT INTO `radiation` VALUES ('69', 'UT00000000000007', '50');
INSERT INTO `radiation` VALUES ('70', 'UT00000000000007', '90');
INSERT INTO `radiation` VALUES ('71', 'UT00000000000007', '60');
INSERT INTO `radiation` VALUES ('72', 'UT00000000000007', '56');
INSERT INTO `radiation` VALUES ('73', 'UT00000000000007', '98');
INSERT INTO `radiation` VALUES ('74', 'UT00000000000007', '7');
INSERT INTO `radiation` VALUES ('75', 'UT00000000000007', '82');
INSERT INTO `radiation` VALUES ('76', 'UT00000000000007', '0');
INSERT INTO `radiation` VALUES ('77', 'UT00000000000007', '90');
INSERT INTO `radiation` VALUES ('78', 'UT00000000000007', '21');
INSERT INTO `radiation` VALUES ('79', 'UT00000000000007', '18');
INSERT INTO `radiation` VALUES ('80', 'UT00000000000007', '33');
INSERT INTO `radiation` VALUES ('81', 'UT00000000000007', '24');
INSERT INTO `radiation` VALUES ('82', 'UT00000000000007', '52');
INSERT INTO `radiation` VALUES ('83', 'UT00000000000007', '61');
INSERT INTO `radiation` VALUES ('84', 'UT00000000000007', '51');
INSERT INTO `radiation` VALUES ('85', 'UT00000000000007', '34');
INSERT INTO `radiation` VALUES ('86', 'UT00000000000007', '49');
INSERT INTO `radiation` VALUES ('87', 'UT00000000000007', '39');
INSERT INTO `radiation` VALUES ('88', 'UT00000000000007', '78');
INSERT INTO `radiation` VALUES ('89', 'UT00000000000007', '20');
INSERT INTO `radiation` VALUES ('90', 'UT00000000000007', '94');
INSERT INTO `radiation` VALUES ('91', 'UT00000000000007', '43');
INSERT INTO `radiation` VALUES ('92', 'UT00000000000007', '56');
INSERT INTO `radiation` VALUES ('93', 'UT00000000000007', '86');
INSERT INTO `radiation` VALUES ('94', 'UT00000000000007', '9');
INSERT INTO `radiation` VALUES ('95', 'UT00000000000007', '78');
INSERT INTO `radiation` VALUES ('96', 'UT00000000000007', '54');
INSERT INTO `radiation` VALUES ('97', 'UT00000000000007', '90');
INSERT INTO `radiation` VALUES ('98', 'UT00000000000007', '63');
INSERT INTO `radiation` VALUES ('99', 'UT00000000000007', '78');
INSERT INTO `radiation` VALUES ('100', 'UT00000000000007', '51');
INSERT INTO `radiation` VALUES ('101', 'UT00000000000008', '50');
INSERT INTO `radiation` VALUES ('102', 'UT00000000000008', '50');
INSERT INTO `radiation` VALUES ('103', 'UT00000000000008', '55');

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
  PRIMARY KEY (`SENSOR_ID`),
  UNIQUE KEY `RADIATION_MODEL_ID_idx` (`RADIATION_MODEL_ID`) USING BTREE,
  KEY `SENSOR_UNIT_ID_idx` (`UNIT_ID`),
  CONSTRAINT `SENSOR_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`UNIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sensor
-- ----------------------------
INSERT INTO `sensor` VALUES ('SS00000000000001', null, 'AYFY-sensor-1', 'UT00000000000008', 'xxx', '1234567', 'UV', '0');
INSERT INTO `sensor` VALUES ('SS00000000000002', 'xxxxxxxxxxxx', 'AYFY-sensor-2', 'UT00000000000008', 'fdf', '234454545', 'UV', '1');

-- ----------------------------
-- Table structure for sensor_seq
-- ----------------------------
DROP TABLE IF EXISTS `sensor_seq`;
CREATE TABLE `sensor_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sensor_seq
-- ----------------------------
INSERT INTO `sensor_seq` VALUES ('1');
INSERT INTO `sensor_seq` VALUES ('2');

-- ----------------------------
-- Table structure for threshold
-- ----------------------------
DROP TABLE IF EXISTS `threshold`;
CREATE TABLE `threshold` (
  `DATA_ID` varchar(32) NOT NULL,
  `NORML` int(11) NOT NULL,
  `WARN` int(11) NOT NULL,
  PRIMARY KEY (`DATA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of threshold
-- ----------------------------
INSERT INTO `threshold` VALUES ('RADIATION', '70', '50');

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
  PRIMARY KEY (`UNIT_ID`),
  KEY `PARENT_ID_INX` (`PARENT_ID`),
  KEY `UNIT_NAME_IDX` (`UNIT_NAME`),
  KEY `ACTIVE_IDX` (`ACTIVE`),
  KEY `LEAF_IDX` (`LEAF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('UT00000000000000', '/', null, '系统根节点', '1', '0', '-1');
INSERT INTO `unit` VALUES ('UT00000000000001', 'AAA省BBB市第一人民医院', 'UT00000000000000', '地址：BBB市CCC路123号', '1', '0', '2');
INSERT INTO `unit` VALUES ('UT00000000000002', '口腔科', 'UT00000000000001', '', '1', '0', '2');
INSERT INTO `unit` VALUES ('UT00000000000003', '神经外科', 'UT00000000000001', '', '1', '0', '0');
INSERT INTO `unit` VALUES ('UT00000000000004', '口腔科病房402', 'UT00000000000002', '外科大楼5楼', '1', '0', '2');
INSERT INTO `unit` VALUES ('UT00000000000005', '神经外科病房708', 'UT00000000000003', '外科大楼7层', '1', '0', '0');
INSERT INTO `unit` VALUES ('UT00000000000006', '紫外光灯01', 'UT00000000000004', '病房窗户附近', '1', '1', '-1');
INSERT INTO `unit` VALUES ('UT00000000000007', '紫外光灯02', 'UT00000000000004', '门后', '1', '1', '2');
INSERT INTO `unit` VALUES ('UT00000000000008', '紫外光灯03', 'UT00000000000005', '门口', '1', '1', '0');

-- ----------------------------
-- Table structure for unit_manager
-- ----------------------------
DROP TABLE IF EXISTS `unit_manager`;
CREATE TABLE `unit_manager` (
  `UNIT_ID` char(16) NOT NULL,
  `USER_ID` char(6) NOT NULL,
  PRIMARY KEY (`UNIT_ID`,`USER_ID`),
  KEY `UNIT_ID_IDX` (`UNIT_ID`),
  KEY `MANAGER_ID_IDX` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of unit_manager
-- ----------------------------
INSERT INTO `unit_manager` VALUES ('UT00000000000006', '000001');
INSERT INTO `unit_manager` VALUES ('UT00000000000007', '000001');
INSERT INTO `unit_manager` VALUES ('UT00000000000008', '000001');

-- ----------------------------
-- Table structure for unit_seq
-- ----------------------------
DROP TABLE IF EXISTS `unit_seq`;
CREATE TABLE `unit_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of unit_seq
-- ----------------------------
INSERT INTO `unit_seq` VALUES ('1');
INSERT INTO `unit_seq` VALUES ('2');
INSERT INTO `unit_seq` VALUES ('3');
INSERT INTO `unit_seq` VALUES ('4');
INSERT INTO `unit_seq` VALUES ('5');
INSERT INTO `unit_seq` VALUES ('6');
INSERT INTO `unit_seq` VALUES ('7');
INSERT INTO `unit_seq` VALUES ('8');

-- ----------------------------
-- Table structure for unit_warn
-- ----------------------------
DROP TABLE IF EXISTS `unit_warn`;
CREATE TABLE `unit_warn` (
  `WARN_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UNIT_ID` char(16) NOT NULL,
  `UNIT_STATUS` int(2) NOT NULL,
  `NOTIFY_TIME` datetime NOT NULL,
  PRIMARY KEY (`WARN_ID`),
  KEY `UNIT_ID_WARN_idx` (`UNIT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of unit_warn
-- ----------------------------
INSERT INTO `unit_warn` VALUES ('1', 'UT00000000000007', '2', '2018-12-24 15:15:11');
INSERT INTO `unit_warn` VALUES ('2', 'UT00000000000007', '2', '2018-12-24 15:30:12');
INSERT INTO `unit_warn` VALUES ('3', 'UT00000000000007', '2', '2018-12-24 15:55:10');
INSERT INTO `unit_warn` VALUES ('4', 'UT00000000000007', '2', '2019-03-25 16:30:11');
INSERT INTO `unit_warn` VALUES ('5', 'UT00000000000007', '2', '2019-03-30 12:40:11');
INSERT INTO `unit_warn` VALUES ('6', 'UT00000000000007', '2', '2019-03-30 12:45:10');
INSERT INTO `unit_warn` VALUES ('7', 'UT00000000000007', '2', '2019-03-30 12:55:10');
INSERT INTO `unit_warn` VALUES ('8', 'UT00000000000007', '2', '2019-03-30 13:00:10');
INSERT INTO `unit_warn` VALUES ('9', 'UT00000000000007', '2', '2019-03-30 13:05:10');
INSERT INTO `unit_warn` VALUES ('10', 'UT00000000000007', '2', '2019-03-30 13:10:11');
INSERT INTO `unit_warn` VALUES ('11', 'UT00000000000007', '2', '2019-03-30 13:15:11');
INSERT INTO `unit_warn` VALUES ('12', 'UT00000000000007', '2', '2019-03-30 13:30:10');
INSERT INTO `unit_warn` VALUES ('13', 'UT00000000000007', '2', '2019-03-30 13:55:10');
INSERT INTO `unit_warn` VALUES ('14', 'UT00000000000007', '2', '2019-03-30 14:20:10');
INSERT INTO `unit_warn` VALUES ('15', 'UT00000000000007', '2', '2019-03-30 14:45:10');

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
  PRIMARY KEY (`USER_ID`),
  KEY `USERNAME` (`USER_NAME`),
  KEY `USERMAIL` (`MAIL_ADDRESS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('000001', '张三', 'zhs@test.org', 'ADMIN', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '1234567890', '1');

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
	SET @@max_sp_recursion_depth = 100;
    SELECT `PARENT_ID` INTO pId FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
    IF unitParentId=pId THEN
        SELECT `UNIT_ID`, `UNIT_NAME`, `PARENT_ID`, `REMARK`, `UNIT_STATUS` FROM `unit` WHERE `ACTIVE` = true AND `UNIT_ID`=unitId;
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
CREATE DEFINER=`root`@`localhost` FUNCTION `getUnitPath`(id char(16)) RETURNS varchar(2048) CHARSET utf8mb4
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
DROP TRIGGER IF EXISTS `sensor_BEFORE_INSERT`;
DELIMITER ;;
CREATE TRIGGER `sensor_BEFORE_INSERT` BEFORE INSERT ON `sensor` FOR EACH ROW BEGIN
    INSERT INTO sensor_seq VALUES (NULL);
    SET NEW.SENSOR_ID = CONCAT('SS', LPAD(LAST_INSERT_ID(), 14, '0'));
END
;;
DELIMITER ;
