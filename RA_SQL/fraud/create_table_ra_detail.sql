/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 12/22/2011 17:53:05 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ra_detail`;
CREATE TABLE `ra_detail` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `call_type` tinyint(2) DEFAULT NULL,
  `s_msisdn` bigint(20) DEFAULT NULL,
  `o_msisdn` bigint(20) DEFAULT NULL,
  `duration` bigint(20) DEFAULT NULL,
  `call_time` datetime NOT NULL,
  `sw_id` tinyint(3) unsigned DEFAULT NULL,
	`s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` int(5) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL,
  `billing_type` tinyint(3) unsigned DEFAULT NULL,
  `detail_type` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`call_time`),
  KEY `call_time` (`call_time`),
  KEY `call_time` (`call_time`),
  KEY `call_time` (`call_time`),
  KEY `call_time` (`call_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
PARTITION BY RANGE  (TO_DAYS(call_time))
(
);

SET FOREIGN_KEY_CHECKS = 1;
