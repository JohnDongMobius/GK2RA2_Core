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
--  Table structure for `traffic_type`, 
-- ----------------------------
DROP TABLE IF EXISTS `traffic_type`;
CREATE TABLE `traffic_type` (
	`id` int(5),
  `traffic_type` varchar(64) DEFAULT "",
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `traffic_type`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


#add traffic_type
insert into traffic_type values (1, 'INTERNAL');
insert into traffic_type values (2, 'DOMESTIC');
insert into traffic_type values (3, 'INTERNATIONAL');

