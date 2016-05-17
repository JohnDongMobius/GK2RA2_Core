/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 01/10/2012 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_summary`
-- ----------------------------
DROP TABLE IF EXISTS `ra_summary`;
CREATE TABLE `ra_summary` (
  `traffic_date` date DEFAULT NULL,
  `moc_call_count` int(11) DEFAULT NULL,
  `moc_duration` int(11) DEFAULT NULL,
  `billing_call_count` int(11) DEFAULT NULL,
  `billing_duration` int(11) DEFAULT NULL,
  `moc_call_count_without_billing` int(11) DEFAULT NULL,
  `moc_duration_without_billing` int(11) DEFAULT NULL,
  `billing_call_count_without_moc` int(11) DEFAULT NULL,
  `billing_duration_without_moc` int(11) DEFAULT NULL,
  `insert_date` datetime DEFAULT NULL,
  `billing_type` tinyint(3) unsigned DEFAULT NULL,
  `redo_times` int(11) DEFAULT 0,
  KEY `idx_traffic_date` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_summary`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
