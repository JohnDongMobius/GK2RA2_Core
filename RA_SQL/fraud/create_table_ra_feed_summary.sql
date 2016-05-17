/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 05/09/2013 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_feed_summary`
-- ----------------------------
DROP TABLE IF EXISTS `ra_feed_summary`;
CREATE TABLE `ra_feed_summary` (
  `traffic_date` date DEFAULT NULL,
  `feed_type` int(5) DEFAULT 0,
  `uploaded_file_count` int(11) DEFAULT 0,
  `processed_file_count` int(11) DEFAULT 0,
  `unprocessed_file_count` int(11) DEFAULT 0,
  `redo_times` int(11) DEFAULT 0,
  KEY `idx_traffic_date` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_feed_summary`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
