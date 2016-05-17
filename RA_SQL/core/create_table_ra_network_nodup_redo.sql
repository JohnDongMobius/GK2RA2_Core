/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 07/23/2013 13:11:53 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_network_nodup_redo`
-- ----------------------------
DROP TABLE IF EXISTS `ra_network_nodup_redo`;
CREATE TABLE `ra_network_nodup_redo` (
  `call_time` datetime NOT NULL,
  `duration` int(10) unsigned DEFAULT 0,
  `call_type` tinyint(3) unsigned DEFAULT 0,
  `sw_id` mediumint(5) unsigned DEFAULT 0,
  `s_msisdn` bigint(20) unsigned DEFAULT 0,
  `o_msisdn` bigint(20) unsigned DEFAULT 0,
  `s_imsi` bigint(20) unsigned DEFAULT 0,
  `s_imei` bigint(20) unsigned DEFAULT 0,
  `s_ci` int(10) unsigned DEFAULT 0,
  `s_lac` smallint(5) unsigned DEFAULT 0,
  `trunk_in` mediumint(5) unsigned DEFAULT 0,
  `trunk_out` mediumint(5) unsigned DEFAULT 0,
  `term_cause` mediumint(5) unsigned DEFAULT 0,
  `term_reason` mediumint(5) unsigned DEFAULT 0,
  `ss_code` tinyint(3) unsigned DEFAULT 0,
  `charge_indicator` tinyint(3) unsigned DEFAULT 0,
  `feed_type` tinyint(3) unsigned DEFAULT 0,
  `billing_type` tinyint(3) unsigned DEFAULT 0,
  `insert_time` datetime DEFAULT NULL,
  `file_name` varchar(60) DEFAULT NULL,
  `call_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `volume_download` bigint(20) unsigned DEFAULT 0,
  `volume_upload` bigint(20) unsigned DEFAULT 0,
  `charge_identifier` bigint(20) unsigned DEFAULT 0,
  PRIMARY KEY (`call_time`,`call_id`),
  KEY `call_time` (`call_time`),
  KEY `s_msisdn` (`s_msisdn`),
  KEY `o_msisdn` (`o_msisdn`),
  KEY `call_id` (`call_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1244379 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (TO_DAYS(call_time))
(PARTITION p20131001 VALUES LESS THAN (735508) ENGINE = InnoDB,
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
 PARTITION p20131031 VALUES LESS THAN (735538) ENGINE = InnoDB)*/;

SET FOREIGN_KEY_CHECKS = 1;
