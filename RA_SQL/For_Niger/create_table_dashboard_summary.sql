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
--  Table structure for `dashboard_summary`
-- ----------------------------
DROP TABLE IF EXISTS `dashboard_summary`;
CREATE TABLE `dashboard_summary` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `type` int(5) DEFAULT 0,
  `repective_x` varchar(60) DEFAULT NULL,
  `count_y` int(11) DEFAULT 0,
  `insert_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `dashboard_summary`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
