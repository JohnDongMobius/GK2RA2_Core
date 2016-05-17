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
--  Table structure for `moc_in_reconsiliation`
-- ----------------------------
DROP TABLE IF EXISTS `moc_in_reconsiliation`;
CREATE TABLE `moc_in_reconsiliation` (
  `traffic_date` date DEFAULT NULL,
  `moc_call_count` int(11) DEFAULT NULL,
  `moc_duration` int(11) DEFAULT NULL,
  `in_call_count` int(11) DEFAULT NULL,
  `in_duration` int(11) DEFAULT NULL,
  `moc_call_count_without_in` int(11) DEFAULT NULL,
  `moc_duration_without_in` int(11) DEFAULT NULL,
  `in_call_count_without_moc` int(11) DEFAULT NULL,
  `in_duration_without_moc` int(11) DEFAULT NULL,
  `insert_date` datetime DEFAULT NULL,
  `redo_times` int(11) DEFAULT 0,
  KEY `idx_traffic_date` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `moc_in_reconsiliation`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
