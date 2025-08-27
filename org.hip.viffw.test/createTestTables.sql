-- Creates tables for testing purposes in the database vif_test.

-- drop tables
DROP TABLE IF EXISTS `tbltest`;
DROP TABLE IF EXISTS `tblTestShort`;
DROP TABLE IF EXISTS `tblparticipant`;
DROP TABLE IF EXISTS `tblgroup`;
DROP TABLE IF EXISTS `tblgroupadmin`;
DROP TABLE IF EXISTS `tblBlobTest`;

-- tbltest
CREATE TABLE `tbltest` (
	`TestID` INT(11) NOT NULL AUTO_INCREMENT,
	`sName` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`sFirstName` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`sStreet` VARCHAR(255) NULL DEFAULT NULL,
	`sPLZ` VARCHAR(255) NULL DEFAULT NULL,
	`sCity` VARCHAR(255) NULL DEFAULT NULL,
	`sTel` VARCHAR(255) NULL DEFAULT NULL,
	`sFax` VARCHAR(255) NULL DEFAULT NULL,
	`sMail` VARCHAR(255) NULL DEFAULT NULL,
	`sLanguage` VARCHAR(255) NULL DEFAULT NULL,
	`sPassword` VARCHAR(255) NULL DEFAULT NULL,
	`dtMutation` DATETIME NULL DEFAULT NULL,
	`bSex` TINYINT(4) NULL DEFAULT NULL,
	`fAmount` FLOAT(12) NULL DEFAULT NULL,
	`fDouble` DOUBLE(22,3) NULL DEFAULT NULL,
	PRIMARY KEY (`TestID`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;

-- tblTestShort
CREATE TABLE `tblTestShort` (
	`TestID` INT NOT NULL AUTO_INCREMENT,
	`ShortID` INT NOT NULL DEFAULT '0',
	PRIMARY KEY (`TestID`)
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;

-- tblgroup
CREATE TABLE `tblGroup` (
	`GroupID` INT NOT NULL AUTO_INCREMENT,
	`sName` VARCHAR(255) NULL DEFAULT '',
	`sDescription` VARCHAR(255) NULL DEFAULT '',
	`nReviewer` INT NULL DEFAULT NULL,
	`nGuestDepth` INT NULL DEFAULT NULL,
	`nMinGroupSize` INT NULL DEFAULT NULL,
	`nState` INT NULL DEFAULT NULL,
	PRIMARY KEY (`GroupID`)
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;

-- tblparticipant
CREATE TABLE `vif_test`.`tblparticipant` (
  `MemberID` INT NOT NULL,
  `GroupID` INT NULL,
  `dtSuspendFrom` DATETIME NULL,
  `dtSuspendTo` DATETIME NULL,
  PRIMARY KEY (`MemberID`),
  UNIQUE INDEX `MemberID_UNIQUE` (`MemberID` ASC),
  INDEX `GroupID_idx` (`GroupID` ASC)
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;

-- tblgroupadmin
CREATE TABLE `vif_test`.`tblgroupadmin` (
  `GroupAdminID` INT NOT NULL AUTO_INCREMENT,
  `MemberID` INT NULL,
  `GroupID` INT NULL,
  PRIMARY KEY (`GroupAdminID`),
  INDEX `GroupID_idx` (`GroupID` ASC),
  INDEX `MemberID_idx` (`MemberID` ASC),
  UNIQUE INDEX `GroupAdminID_UNIQUE` (`GroupAdminID` ASC)
) 
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;

-- tblBlobTest
CREATE TABLE `vif_test`.`tblBlobTest` (
  `BlobTestID` INT NOT NULL AUTO_INCREMENT,
  `sName` VARCHAR(255) NULL DEFAULT '',
  `xValue` BLOB,
  PRIMARY KEY (`BlobTestID`)
) 
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
;
