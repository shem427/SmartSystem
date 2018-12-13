use `monitor`;

DROP TABLE IF EXISTS `threshold`;
CREATE TABLE `threshold` (
  `TYPE` varchar(32) NOT NULL,
  `NORMAL` int(11) NOT NULL,
  `WARNING` int(11) NOT NULL,
  PRIMARY KEY (`TYPE`)
);

DROP TABLE IF EXISTS `unit_seq`;
CREATE TABLE `unit_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `radiation`;
DROP TABLE IF EXISTS `sensor`;
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `UNIT_ID` char(16) NOT NULL DEFAULT 'UT00000000000000',
  `UNIT_NAME` varchar(128) NOT NULL,
  `PARENT_ID` char(16) DEFAULT NULL,
  `REMARK` varchar(256) DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `LEAF` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UNIT_ID`),
  KEY `PARENT_ID_INX` (`PARENT_ID`),
  KEY `UNIT_NAME_IDX` (`UNIT_NAME`),
  KEY `ACTIVE_IDX` (`ACTIVE`),
  KEY `LEAF_IDX` (`LEAF`)
);

CREATE TABLE `radiation` (
  `RAD_ID` BIGINT NOT NULL AUTO_INCREMENT,
  `UNIT_ID` CHAR(16) NOT NULL,
  `RAD_VALUE` VARCHAR(45) NULL,
  PRIMARY KEY (`RAD_ID`),
  INDEX `RAD_UNIT_ID_idx` (`UNIT_ID` ASC) VISIBLE,
  CONSTRAINT `RAD_UNIT_ID`
    FOREIGN KEY (`UNIT_ID`)
    REFERENCES `unit` (`UNIT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

DROP TRIGGER IF EXISTS `unit_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `unit_BEFORE_INSERT` BEFORE INSERT ON `unit` FOR EACH ROW
BEGIN
    INSERT INTO unit_seq VALUES (NULL);
    SET NEW.UNIT_ID = CONCAT('UT', LPAD(LAST_INSERT_ID(), 14, '0'));
END$$
DELIMITER ;

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
);

DROP TABLE IF EXISTS `unit_manager`;
CREATE TABLE `unit_manager` (
  `UNIT_ID` char(16) NOT NULL,
  `USER_ID` char(6) NOT NULL,
  PRIMARY KEY (`UNIT_ID`,`USER_ID`),
  KEY `UNIT_ID_IDX` (`UNIT_ID`),
  KEY `MANAGER_ID_IDX` (`USER_ID`)
);

DROP TABLE IF EXISTS `sensor_seq`;
CREATE TABLE `sensor_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);

CREATE TABLE `sensor` (
  `SENSOR_ID` char(16) NOT NULL,
  `SENSOR_NAME` varchar(128) DEFAULT NULL,
  `UNIT_ID` char(16) NOT NULL,
  `SENSOR_REMARK` varchar(1024) DEFAULT NULL,
  `SENSOR_SN` varchar(128) DEFAULT NULL,
  `SENSOR_MODEL` varchar(64) DEFAULT NULL,
  `ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`SENSOR_ID`),
  KEY `SENSOR_UNIT_ID_idx` (`UNIT_ID`),
  CONSTRAINT `SENSOR_UNIT_ID` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`unit_id`)
);

DROP TRIGGER IF EXISTS `sensor_BEFORE_INSERT`;
DELIMITER $$
CREATE TRIGGER `sensor_BEFORE_INSERT` BEFORE INSERT ON `sensor` FOR EACH ROW
BEGIN
    INSERT INTO sensor_seq VALUES (NULL);
    SET NEW.SENSOR_ID = CONCAT('UT', LPAD(LAST_INSERT_ID(), 14, '0'));
END$$
DELIMITER ;

INSERT INTO `unit` VALUES ('UT00000000000001','/',NULL,'系统根节点',1,0);
INSERT INTO `user` VALUES ('000001','张三','zhs@test.org','','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','1234567890', 1);
INSERT INTO threshold values ('RADIATION', 70, 50);


DROP FUNCTION IF EXISTS `getUnitPath`;
DELIMITER $$
CREATE FUNCTION `getUnitPath`(id char(16)) RETURNS varchar(2048) CHARSET utf8mb4
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
END$$
DELIMITER ;

DROP procedure IF EXISTS `getRadiationUnitStatus`;
DELIMITER $$
CREATE PROCEDURE `getRadiationUnitStatus`(IN unitId CHAR(16), IN warn INT, IN err INT, OUT stats INT)
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE radValue INT;
    DECLARE ret INT DEFAULT 0;
    DECLARE countWarn INT DEFAULT 0;
    DECLARE countError INT DEFAULT 0;
    
    DECLARE curl CURSOR FOR SELECT `RAD_VALUE` FROM `RADIATION` WHERE `UNIT_ID`=unitId ORDER BY RAD_ID DESC LIMIT 0, 10;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
    
    OPEN curl;
    radLoop: LOOP
        FETCH curl INTO radValue;
        IF done THEN
            SET ret = -1;
            LEAVE radLoop;
        ELSEIF radValue <= err THEN
            SET countError = countError + 1;
        ELSEIF radValue > err AND radValue <= warn THEN
            SET countWarn = countWarn + 1;
        END IF;
    END LOOP radLoop;
    CLOSE curl;
    
    IF countError >= 4 THEN
        SET ret = 2;
    ELSEIF countWarn >= 4 THEN
        SET ret = 2;
    END IF;
    
    SELECT ret INTO stats;
END$$

DELIMITER ;


DROP procedure IF EXISTS `getUnitStatus`;
DELIMITER $$
CREATE PROCEDURE `getUnitStatus`(IN unitId CHAR(16), IN normal INT, IN warn INT, OUT stats INT)
BEGIN
    DECLARE done BOOL DEFAULT false;
    DECLARE parentUnitId INT;
    
    DECLARE curl CURSOR FOR SELECT `UNIT_ID` FROM `UNIT` WHERE `PARENT_ID`=unitId;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
    
    OPEN curl;
    radLoop: LOOP
        FETCH curl INTO parentUnitId;
        IF done THEN
            CALL getRadiationUnitStatus(unitId, normal, warn, stats);
            LEAVE radLoop;
        ELSE
            CALL getUnitStatus(unitId, normal, warn, stats);
        END IF;
    END LOOP radLoop;
    CLOSE curl;
END$$

DELIMITER ;


DROP function IF EXISTS `getUnitStatus`;
DELIMITER $$
CREATE FUNCTION `getUnitStatus` (id CHAR(16)) RETURNS INT
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE stats INT;
    DECLARE normal INT;
    DECLARE warn INT;
    
    SELECT `NORMAL`, `WARNING` INTO normal, warn FROM `THRESHOLD` WHERE `TYPE`='RADIATION';

    CALL getUnitStatus(id, normal, warn, stats);
    RETURN stats;
END$$

DELIMITER ;
