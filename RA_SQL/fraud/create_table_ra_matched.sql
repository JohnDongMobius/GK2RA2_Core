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
--  Table structure for `ra_matched`
-- ----------------------------
DROP TABLE IF EXISTS `ra_matched`;
CREATE TABLE `ra_matched` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `msc_call_type` tinyint(2) DEFAULT NULL,
  `msc_s_msisdn` bigint(20) DEFAULT NULL,
  `msc_o_msisdn` bigint(20) DEFAULT NULL,
  `msc_duration` bigint(20) DEFAULT NULL,
  `msc_call_time` datetime NOT NULL,
	`msc_s_imsi` bigint(20) unsigned DEFAULT NULL,
  `msc_charge_indicator` tinyint(3) unsigned DEFAULT NULL,
  `billing_type` tinyint(3) unsigned DEFAULT NULL,
  `billing_call_type` tinyint(2) DEFAULT NULL,
  `billing_s_msisdn` bigint(20) DEFAULT NULL,
  `billing_o_msisdn` bigint(20) DEFAULT NULL,
  `billing_duration` bigint(20) DEFAULT NULL,
  `billing_call_time` datetime NOT NULL,
  `billing_s_imsi` bigint(20) unsigned DEFAULT NULL,
  `flag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`,`msc_call_time`),
  KEY `msc_call_time` (`msc_call_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
PARTITION BY RANGE  (TO_DAYS(msc_call_time))
(PARTITION p201305 VALUES LESS THAN (735385) ENGINE = InnoDB,
 PARTITION p201306 VALUES LESS THAN (735415) ENGINE = InnoDB,
 PARTITION p201307 VALUES LESS THAN (735446) ENGINE = InnoDB,
 PARTITION p201308 VALUES LESS THAN (735477) ENGINE = InnoDB,
 PARTITION p201309 VALUES LESS THAN (735507) ENGINE = InnoDB,
 PARTITION p201310 VALUES LESS THAN (735538) ENGINE = InnoDB,
 PARTITION p201311 VALUES LESS THAN (735568) ENGINE = InnoDB,
 PARTITION p201312 VALUES LESS THAN (735599) ENGINE = InnoDB,
 PARTITION p201401 VALUES LESS THAN (735630) ENGINE = InnoDB,
 PARTITION p201402 VALUES LESS THAN (735658) ENGINE = InnoDB,
 PARTITION p201403 VALUES LESS THAN (735689) ENGINE = InnoDB,
 PARTITION p201404 VALUES LESS THAN (735719) ENGINE = InnoDB
);

SET FOREIGN_KEY_CHECKS = 1;
