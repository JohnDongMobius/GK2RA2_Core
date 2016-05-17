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
--  Table structure for `trunk_scope`, 
-- ----------------------------
DROP TABLE IF EXISTS `trunk_scope`;
CREATE TABLE `trunk_scope` (
	`id` int(5),
  `trunk_scope` varchar(64) DEFAULT "",
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `trunk_scope`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


#add trunk scope'
insert into trunk_scope values (1, 'Internal');
insert into trunk_scope values (2, 'Domestic');
insert into trunk_scope values (3, 'International');
