/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_614_0

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 08/10/2013 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for short_msisdn_group
-- ----------------------------
DROP TABLE IF EXISTS `short_msisdn_group`;
CREATE TABLE `short_msisdn_group` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `group_id` int(15) NOT NULL DEFAULT 0,
  `group_name` varchar(128) NOT NULL,
  `capacity` int(15) NOT NULL DEFAULT 0,
  `insert_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `short_msisdn_group`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;