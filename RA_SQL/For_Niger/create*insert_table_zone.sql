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
--  Table structure for `zone`, 
-- ----------------------------
DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone` (
	`id` int(5),
  `zone_name` varchar(64) DEFAULT "",
  `zone_desc` varchar(256) DEFAULT "",
  `first_charging_interval` smallint DEFAULT 0,
  `rate` float(9,3) DEFAULT 0,
  `currency` varchar (64),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `rate_plan`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


#add zone for Niger-Airtel'
insert into zone values (1, 'Zone A', 'for international zone A', 1, 1.65, '');
insert into zone values (2, 'Zone B', 'for international zone B', 1, 2.5, '');
insert into zone values (3, 'Zone C', 'for international zone C', 1, 3, '');
insert into zone values (4, 'Zone D', 'for international zone D', 1, 5, '');
insert into zone values (5, 'REST OF THE WORLD', 'for international rest of world', 1, 5, '');

#Assuming rate is second unit, convert to minute unit.
update zone set rate=rate*60;
