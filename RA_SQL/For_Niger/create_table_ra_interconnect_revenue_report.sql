/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_614_0

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 04/23/2014 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_interconnect_revenue_report`
-- ----------------------------
DROP TABLE IF EXISTS `ra_interconnect_revenue_report`;
CREATE TABLE `ra_interconnect_revenue_report` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `report_type` int(5) DEFAULT 0,
  `Franchise_ID` varchar(20) DEFAULT 0,
  `traffic_direction` varchar(50) DEFAULT 0,
  `interconnect_operator` varchar(50) DEFAULT 0,
  `product_ID` varchar(50) DEFAULT 0,
  `in_trunk_name` varchar(20) DEFAULT "",
  `out_trunk_name` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `call_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  `amount_count` decimal(17,2) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_interconnect_revenue_report`
-- `id` int(15) NOT NULL AUTO_INCREMENT,
--  PRIMARY KEY (`id`),
-- ----------------------------		

SET FOREIGN_KEY_CHECKS = 1;

alter table ra_interconnect_revenue_report add column trunk_scope varchar(50) DEFAULT NULL;

--20140604, add for interconnect report requirement change.
alter table ra_interconnect_revenue_report add column moc_call_count bigint(15) DEFAULT 0, add column moc_duration_count bigint(15) DEFAULT 0, add column mtc_call_count bigint(15) DEFAULT 0, add column mtc_duration_count bigint(15) DEFAULT 0, add column tst_call_count bigint(15) DEFAULT 0, add column tst_duration_count bigint(15) DEFAULT 0;

