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
--  Table structure for `ra_traffic_summary_hourly`
-- ----------------------------
DROP TABLE IF EXISTS `ra_traffic_summary_hourly`;
CREATE TABLE `ra_traffic_summary_hourly` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT '00',
  `report_type` int(5) DEFAULT 0,
  `feed_type` int(5) DEFAULT 0,
  `call_type` int(5) DEFAULT 0,
  `record_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  `amount_count` bigint(15) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_traffic_hour` (`traffic_hour`),
  KEY `idx_feed_type` (`feed_type`),
  KEY `idx_call_type` (`call_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_traffic_summary_hourly`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;



#add column hour in table ra_detail
#alter table ra_detail add column `traffic_hour` varchar(2) DEFAULT '00';

