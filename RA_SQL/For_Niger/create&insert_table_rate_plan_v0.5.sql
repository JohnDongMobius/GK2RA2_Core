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
--  Table structure for `rate_plan_trunk`,type: 1-domestic, 2-international
--  unit: 30 means rate is based on 30s; 60 means rate is based on 60s.
--  time_period_start: the rate works since this date
--  time_period_end: the rate works before this date, if blank, no end date.
-- ----------------------------
DROP TABLE IF EXISTS `rate_plan_interconnect`;
CREATE TABLE `rate_plan_interconnect` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `rate_plan_code` varchar(100) DEFAULT "",
  `product_ID` varchar(50) DEFAULT "",
  `type` int(3) DEFAULT 1,
  `s_num_code` varchar(50) DEFAULT "",
  `o_num_code` varchar(50) DEFAULT "",
  `time_period_start` varchar(20) DEFAULT "",
  `time_period_end` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `unit` int(3) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`id`),
  KEY `idx_s_num_code` (`s_num_code`),
  KEY `idx_o_num_code` (`o_num_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `rate_plan_interconnect`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

insert into rate_plan_interconnect(product_ID,type,s_num_code,o_num_code,time_period_start,rate,unit) values ('DIRECT',1,'NIGER','NIGER','2014-04-01',35,60);
insert into rate_plan_interconnect(product_ID,type,s_num_code,o_num_code,time_period_start,rate,unit) values ('TRANSIT SPECIAL',1,'INTERNATIONAL','NIGER','2014-04-01',63,60);
insert into rate_plan_interconnect(product_ID,type,s_num_code,o_num_code,time_period_start,rate,unit) values ('TRANSIT SPECIAL',1,'INTERNATIONAL','INTERNATIONAL','2014-04-01',63,60);
