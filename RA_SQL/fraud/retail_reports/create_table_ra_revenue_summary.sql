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
--  Table structure for `ra_revenue_summary`
-- ----------------------------
DROP TABLE IF EXISTS `ra_revenue_summary`;
CREATE TABLE `ra_revenue_summary` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT '00',
  `report_type` int(5) DEFAULT 0,
  `call_type` varchar(20) DEFAULT 0,
  `franchise_id` tinyint(3) DEFAULT 0,
  `s_num_code` varchar(50) DEFAULT 0,
  `o_num_code` varchar(50) DEFAULT 0,
  `product_id` varchar(50) DEFAULT 0,
  `time_period` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `call_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  `amount_count` decimal(17,2) DEFAULT 0,
  `insert_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_report_type` (`report_type`),
  KEY `idx_call_type` (`call_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_revenue_summary`
-- ----------------------------		

SET FOREIGN_KEY_CHECKS = 1;

#rename product_id to sub_product_id
alter table ra_revenue_summary change product_ID sub_product_ID varchar(100) DEFAULT 0;

#add product_id
alter table ra_revenue_summary add column `product_ID` varchar(50) DEFAULT 0;

#20140715, add call id range for drill-down query, change franchise_id type.
alter table ra_revenue_summary add column min_call_id bigint(20) DEFAULT 0, add column max_call_id bigint(20) DEFAULT 0;
alter table ra_revenue_summary change franchise_ID franchise_ID varchar(50) DEFAULT 0;
