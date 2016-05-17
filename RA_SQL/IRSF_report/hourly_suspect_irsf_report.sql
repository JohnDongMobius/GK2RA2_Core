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
--  Table structure for `hourly_suspect_irsf_report`
-- ----------------------------
DROP TABLE IF EXISTS `hourly_suspect_irsf_report`;
CREATE TABLE `hourly_suspect_irsf_report` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `report_type` varchar(3) NOT NULL,
  `traffic_date` date NOT NULL,
  `traffic_hour` varchar(2) NOT NULL,
  `o_msisdn` bigint(20) NOT NULL,
  `type` varchar(10) NOT NULL,
  `international_country` varchar(20) NOT NULL,
  `international_call_in_count` int(15) NOT NULL,
  `international_call_in_duration` bigint(20) NOT NULL,
  `a_call_count` int(11) NOT NULL,
  `a_ratio` double NOT NULL,
  `irsf_number_range` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `hourly_suspect_irsf_report`
-- ----------------------------

alter table hourly_suspect_irsf_report add column irsf_number_range bigint(20) default 0;

SET FOREIGN_KEY_CHECKS = 1;
