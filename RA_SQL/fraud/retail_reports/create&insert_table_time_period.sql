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
--  Table structure for `time_period`  local timezone.
-- ----------------------------
DROP TABLE IF EXISTS `time_period`;
CREATE TABLE `time_period` (
	`id` int(15) NOT NULL,
  `day_in_week` varchar(20) DEFAULT "",
  `start_time` time DEFAULT "00:00:00",
  `end_time` time DEFAULT "00:00:00",
  `pricing_period` varchar(20) DEFAULT "",
  
  PRIMARY KEY (`id`),
  UNIQUE `unique_index` (`day_in_week`,`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `time_period`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (1,"Monday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (2,"Tuesday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (3,"Wednesday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (4,"Thursday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (5,"Friday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (6,"Saturday", "07:00:00", "19:00:00", "PEAK");
insert into time_period (id,day_in_week,start_time, end_time, pricing_period) values (7,"Sunday", "07:00:00", "19:00:00", "PEAK");
                                                                                    