/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 02/23/2012 21:59:08 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `moc_out_reconsiliation`
-- ----------------------------
DROP TABLE IF EXISTS `moc_out_reconsiliation`;
CREATE TABLE `moc_out_reconsiliation` (
  `traffic_date` date DEFAULT NULL,
  `moc_call_count` int(11) DEFAULT NULL,
  `moc_duration` int(11) DEFAULT NULL,
  `out_call_count` int(11) DEFAULT NULL,
  `out_duration` int(11) DEFAULT NULL,
  `moc_call_count_without_out` int(11) DEFAULT NULL,
  `moc_duration_without_out` int(11) DEFAULT NULL,
  `out_call_count_without_moc` int(11) DEFAULT NULL,
  `out_duration_without_moc` int(11) DEFAULT NULL,
  `insert_date` datetime DEFAULT NULL,
  `redo_times` int(11) unsigned DEFAULT '0',
  KEY `idx_traffic_date` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
