/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 04/12/2012 11:19:05 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_process_status`
-- ----------------------------
DROP TABLE IF EXISTS `ra_process_status`;
CREATE TABLE `ra_process_status` (
  `status_parameter` varchar(128) NOT NULL DEFAULT '',
  `status_value` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`status_parameter`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
