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
--  Table structure for short_msisdn
-- ----------------------------
DROP TABLE IF EXISTS `short_msisdn`;
CREATE TABLE `short_msisdn` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `short_num` int(10) NOT NULL DEFAULT 0,
  `subscriber_num` bigint(20) NOT NULL DEFAULT 0,
  `local_DN_set` int(5) NOT NULL DEFAULT 0,
  `group_id` int(3) NOT NULL DEFAULT 0,
  `insert_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`short_num`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `short_msisdn`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;