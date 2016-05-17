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
--  Table structure for `msisdn_type`, 
-- ----------------------------
DROP TABLE IF EXISTS `msisdn_type`;
CREATE TABLE `msisdn_type` (
	`code` varchar(2),
  `msisdn_type` varchar(64) DEFAULT "",
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `msisdn_type`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


#add trunk scope'
insert into msisdn_type values ('L', 'local number');
insert into msisdn_type values ('I', 'international number');
insert into msisdn_type values ('S', 'short number');
insert into msisdn_type values ('D', 'domestic long number');
insert into msisdn_type values ('Z', '0');
insert into msisdn_type values ('U', 'undefined number');
