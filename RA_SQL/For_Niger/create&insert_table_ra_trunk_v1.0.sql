/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : fraud_614_0

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 04/23/2014 15:59:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_trunk`
-- ----------------------------
DROP TABLE IF EXISTS `ra_trunk`;
CREATE TABLE `ra_trunk` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `source_operator_id` varchar(20) DEFAULT 0,
  `trunk_name` varchar(20) DEFAULT "",
  `interconnect_operator` varchar(50) DEFAULT 0,
  `traffic_direction` varchar(50) DEFAULT 0,
  `trunk_type` int(3) DEFAULT 0,
  `trunk_scope` varchar(20) DEFAULT "",
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `ra_trunk_tmp`;
CREATE TABLE `ra_trunk_tmp` (
  `source_operator` varchar(20) DEFAULT 0,
  `trunk_in` varchar(20) DEFAULT "",
  `trunk_out` varchar(20) DEFAULT "",
  `interconnect_operator` varchar(50) DEFAULT 0,
  `traffic_direction` varchar(50) DEFAULT 0,
  `trunk_scope` varchar(20) DEFAULT ""
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_trunk`
-- ----------------------------		

SET FOREIGN_KEY_CHECKS = 1;

load data infile "/tmp/Niger-TrunkDefinitionsV1.0.csv" into table ra_trunk_tmp fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5,@col6) set source_operator=@col1,trunk_in=@col2,trunk_out=@col3,interconnect_operator=@col4,traffic_direction=@col5,trunk_scope=@col6;

load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/Niger-TrunkDefinitionsV1.0.csv" into table ra_trunk_tmp fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5,@col6) set source_operator=@col1,trunk_in=@col2,trunk_out=@col3,interconnect_operator=@col4,traffic_direction=@col5,trunk_scope=@col6;

delete from ra_trunk_tmp where source_operator='Source Operator' or source_operator='';

insert into ra_trunk(source_operator_id,trunk_name,interconnect_operator,traffic_direction,trunk_type,trunk_scope) select source_operator,trunk_in,interconnect_operator,'Incoming',1,trunk_scope from ra_trunk_tmp where trunk_in != '';

insert into ra_trunk(source_operator_id,trunk_name,interconnect_operator,traffic_direction,trunk_type,trunk_scope) select source_operator,trunk_out,interconnect_operator,'Outgoing',2,trunk_scope from ra_trunk_tmp where trunk_out != '';

drop table ra_trunk_tmp;
