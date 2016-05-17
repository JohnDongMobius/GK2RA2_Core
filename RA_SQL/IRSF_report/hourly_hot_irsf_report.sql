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
--  Table structure for `hourly_hot_irsf_report`
-- ----------------------------
DROP TABLE IF EXISTS `hourly_hot_irsf_report`;
CREATE TABLE `hourly_hot_irsf_report` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `report_type` varchar(3) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT NULL,
  `s_msisdn` bigint(20) DEFAULT NULL,
  `msisdn_list_group` varchar(20) DEFAULT NULL,
  `irsf_call_count` int(11) DEFAULT NULL,
  `irsf_duration` bigint(20) DEFAULT NULL,
  `call_out_count` int(11) DEFAULT NULL,
  `call_out_duration` bigint(20) DEFAULT NULL,
  `b_call_count` int(11) DEFAULT NULL,
  `b_ratio` double DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `PK` (`traffic_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `hourly_hot_irsf_report`
-- ----------------------------

alter table hourly_hot_irsf_report add column irsf_number_range bigint(20) default 0;

SET FOREIGN_KEY_CHECKS = 1;
