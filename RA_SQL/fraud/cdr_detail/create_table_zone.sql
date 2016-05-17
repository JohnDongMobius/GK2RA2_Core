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
--  Table structure for `zone`, 
-- ----------------------------
DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone` (
	`id` int(5),
  `zone_name` varchar(64) DEFAULT "",
  `zone_desc` varchar(256) DEFAULT "",
  `first_charging_interval` smallint DEFAULT 0,
  `rate` float(9,3) DEFAULT 0,
  `currency` varchar (64),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `rate_plan`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


