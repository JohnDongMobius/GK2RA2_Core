/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Version : 50510
 Source Host           : 127.0.0.1
 Source Database       : fraud_472_2

 Target Server Version : 50510
 File Encoding         : utf-8

 Date: 08/11/2015 15:27:16 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `hourly_report_status`
-- ----------------------------
DROP TABLE IF EXISTS `hourly_report_status`;
CREATE TABLE `hourly_report_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` varchar(32) NOT NULL,
  `hour` varchar(32) NOT NULL,
  `report_type` tinyint(3) NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
