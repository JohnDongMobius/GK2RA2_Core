/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_614_0

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 05/09/2013 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for operators
-- ----------------------------
DROP TABLE IF EXISTS `operators`;
CREATE TABLE `operators` (
	`id` int(15) NOT NULL DEFAULT 0,
  `mcc` varchar(8) NOT NULL,
  `mnc` varchar(8) NOT NULL,
  `country_code` varchar(8) NOT NULL,
  `operator_name` varchar(128) NOT NULL,
  `timezone` varchar(100) DEFAULT NULL,
  `tblname` varchar(100) DEFAULT NULL,
  `mask_value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`mcc`,`mnc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `operators`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


insert into operators values (1,614,1,227,'Sahelcom','Africa/Niamey','operators',0);
insert into operators values (2,614,2,227,'Airtel','Africa/Niamey','operators',0);
insert into operators values (3,614,3,227,'Moov','Africa/Niamey','operators',0);
insert into operators values (4,614,4,227,'Orange','Africa/Niamey','operators',0);
insert into operators values (5,614,6,227,'Sonitel','Africa/Niamey','operators',0);
