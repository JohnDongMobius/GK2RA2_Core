/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 05/23/2012 15:15:34 PM
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

-- ----------------------------
--  Records of `ra_process_status`
-- ----------------------------
BEGIN;
INSERT INTO `ra_process_status` VALUES ('paid_list_update_status', '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
