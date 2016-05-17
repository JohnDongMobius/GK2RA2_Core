/*
 Navicat Premium Data Transfer

 Source Server         : Maldives472_2 10.213.2.54 34722
 Source Server Type    : MySQL
 Source Server Version : 50508
 Source Host           : 10.213.2.54
 Source Database       : fraud_472_2

 Target Server Type    : MySQL
 Target Server Version : 50508
 File Encoding         : utf-8

 Date: 05/12/2016 17:46:03 PM
 
 private long id;
	private String trafficDate;
	private String trafficHour;
	private long sMsisdn;
	private long sImsi;
	private String tapcode;
	private int numberOfCalls;
	private long totalDuration;
	private int ruleId;
	
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `hourly_msc_irsf_report`
-- ----------------------------
DROP TABLE IF EXISTS `hourly_msc_irsf_report`;
CREATE TABLE `hourly_msc_irsf_report` (
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` int(2) DEFAULT 0,
  `s_msisdn` bigint(20) DEFAULT 0,
  `s_imsi` bigint(20) DEFAULT 0,
  `tap_code` varchar(20) DEFAULT NULL,
  `number_of_calls` bigint(20) DEFAULT 0,
  `total_duration` bigint(20) DEFAULT 0,
  `rule_id` int(2) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
