/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 02/23/2012 23:44:53 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_billing_nodup`
-- ----------------------------
DROP TABLE IF EXISTS `ra_billing_nodup`;
CREATE TABLE `ra_billing_nodup` (
  `call_time` datetime NOT NULL,
  `duration` int(10) unsigned DEFAULT NULL,
  `call_type` tinyint(3) unsigned DEFAULT NULL,
  `sw_id` tinyint(3) unsigned DEFAULT NULL,
  `s_msisdn` bigint(20) unsigned DEFAULT NULL,
  `o_msisdn` bigint(20) unsigned DEFAULT NULL,
  `s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` int(10) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL,
  `feed_type` tinyint(3) unsigned DEFAULT NULL,
  `billing_type` tinyint(3) unsigned DEFAULT NULL,
  KEY `call_time` (`call_time`),
  KEY `s_msisdn` (`s_msisdn`),
  KEY `o_msisdn` (`o_msisdn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (TO_DAYS(call_time))
(
 PARTITION p20131001 VALUES LESS THAN (735508) ENGINE = InnoDB,
 PARTITION p20131002 VALUES LESS THAN (735509) ENGINE = InnoDB,
 PARTITION p20131003 VALUES LESS THAN (735510) ENGINE = InnoDB,
 PARTITION p20131004 VALUES LESS THAN (735511) ENGINE = InnoDB,
 PARTITION p20131005 VALUES LESS THAN (735512) ENGINE = InnoDB,
 PARTITION p20131006 VALUES LESS THAN (735513) ENGINE = InnoDB,
 PARTITION p20131007 VALUES LESS THAN (735514) ENGINE = InnoDB,
 PARTITION p20131008 VALUES LESS THAN (735515) ENGINE = InnoDB,
 PARTITION p20131009 VALUES LESS THAN (735516) ENGINE = InnoDB,
 PARTITION p20131010 VALUES LESS THAN (735517) ENGINE = InnoDB,
 PARTITION p20131011 VALUES LESS THAN (735518) ENGINE = InnoDB,
 PARTITION p20131012 VALUES LESS THAN (735519) ENGINE = InnoDB,
 PARTITION p20131013 VALUES LESS THAN (735520) ENGINE = InnoDB,
 PARTITION p20131014 VALUES LESS THAN (735521) ENGINE = InnoDB,
 PARTITION p20131015 VALUES LESS THAN (735522) ENGINE = InnoDB,
 PARTITION p20131016 VALUES LESS THAN (735523) ENGINE = InnoDB,
 PARTITION p20131017 VALUES LESS THAN (735524) ENGINE = InnoDB,
 PARTITION p20131018 VALUES LESS THAN (735525) ENGINE = InnoDB,
 PARTITION p20131019 VALUES LESS THAN (735526) ENGINE = InnoDB,
 PARTITION p20131020 VALUES LESS THAN (735527) ENGINE = InnoDB,
 PARTITION p20131021 VALUES LESS THAN (735528) ENGINE = InnoDB,
 PARTITION p20131022 VALUES LESS THAN (735529) ENGINE = InnoDB,
 PARTITION p20131023 VALUES LESS THAN (735530) ENGINE = InnoDB,
 PARTITION p20131024 VALUES LESS THAN (735531) ENGINE = InnoDB,
 PARTITION p20131025 VALUES LESS THAN (735532) ENGINE = InnoDB,
 PARTITION p20131026 VALUES LESS THAN (735533) ENGINE = InnoDB,
 PARTITION p20131027 VALUES LESS THAN (735534) ENGINE = InnoDB,
 PARTITION p20131028 VALUES LESS THAN (735535) ENGINE = InnoDB,
 PARTITION p20131029 VALUES LESS THAN (735536) ENGINE = InnoDB,
 PARTITION p20131030 VALUES LESS THAN (735537) ENGINE = InnoDB,
 PARTITION p20131031 VALUES LESS THAN (735538) ENGINE = InnoDB) */;

SET FOREIGN_KEY_CHECKS = 1;