/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_614_0

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 16/10/2013 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for lookup
-- ----------------------------
DROP TABLE IF EXISTS `lookup`;
CREATE TABLE `lookup` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `code` int(5) NOT NULL DEFAULT 0,
  `name` varchar(128) NOT NULL,
  `report_type` int(3) NOT NULL DEFAULT 0,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `lookup`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

insert into lookup (code, name, report_type, description) values (1, 'GSM', 202, 'GSM file');
insert into lookup (code, name, report_type, description) values (2, 'CDMA', 202, 'CDMA file');
insert into lookup (code, name, report_type, description) values (3, 'IGW', 202, 'IGW file');
insert into lookup (code, name, report_type, description) values (4, 'PSTN', 202, 'PSTN file');

insert into lookup (code, name, report_type, description) values (101, 'MSC', 203, 'MSC feed');
insert into lookup (code, name, report_type, description) values (201, 'SGSN', 203, 'SGSN feed');
insert into lookup (code, name, report_type, description) values (202, 'GGSN', 203, 'GGSN feed');
insert into lookup (code, name, report_type, description) values (301, 'IN', 203, 'IN feed');
insert into lookup (code, name, report_type, description) values (302, 'TAPOUT', 203, 'TAPOUT feed');
insert into lookup (code, name, report_type, description) values (303, 'TAPIN', 203, 'TAPIN feed');
insert into lookup (code, name, report_type, description) values (304, 'Billing Huawei', 203, 'Billing Huawei feed');

insert into lookup (code, name, report_type, description) values (6143, '101,102,103,111,112,113', 301, 'CALL TYPE VISIBILITY for Moov');

insert into lookup (code, name, report_type, description) values (11, '20', 301,'unit:%, call volume threhold triggering alarm for Sahelcom');
insert into lookup (code, name, report_type, description) values (12, '20', 301,'unit:%, call volume threhold triggering alarm for Airtel');
insert into lookup (code, name, report_type, description) values (13, '20', 301,'unit:%, call volume threhold triggering alarm for Moov');
insert into lookup (code, name, report_type, description) values (14, '20', 301,'unit:%, call volume threhold triggering alarm for Orange');
insert into lookup (code, name, report_type, description) values (15, '20', 301,'unit:%, call volume threhold triggering alarm for Sonitel');