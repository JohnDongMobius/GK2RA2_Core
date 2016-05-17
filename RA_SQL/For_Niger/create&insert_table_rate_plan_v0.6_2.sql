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
--  Table structure for `rate_plan`, type=1(voice),2(message)
-- ----------------------------
DROP TABLE IF EXISTS `rate_plan`;
CREATE TABLE `rate_plan` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `rate_plan_code` varchar(100) DEFAULT "",
  `product_ID` varchar(50) DEFAULT "",
  `type` int(3) DEFAULT 1,
  `s_num_code` varchar(50) DEFAULT "",
  `o_num_code` varchar(50) DEFAULT "",
  `time_period` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `call_type` int(5) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_s_num_code` (`s_num_code`),
  KEY `idx_o_num_code` (`o_num_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `rate_plan`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;



#ADD rate_plan for voice
#before load data, need to remove dummy columns in csv and enter a blank line in the last of csv.
load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/NIGER RETAIL RATE PLAN V0.6.csv" into table rate_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5,@col6,@col7,@col8) set rate_plan_code=@col1,s_num_code=@col2,o_num_code=@col3,rate=@col4,call_type=1,time_period=@col8,type=1;
load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/NIGER RETAIL RATE PLAN SMS V0.6.csv" into table rate_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set rate_plan_code=@col2,product_id=@col1,s_num_code=@col3,o_num_code=@col4,call_type=1,type=2;

load data infile "/tmp/NIGER RETAIL RATE PLAN V0.6.csv" into table rate_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5,@col6,@col7,@col8) set rate_plan_code=@col1,s_num_code=@col2,o_num_code=@col3,rate=@col4,call_type=1,time_period=@col8,type=1;
load data infile "/tmp/NIGER RETAIL RATE PLAN SMS V0.6.csv" into table rate_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col8) set rate_plan_code=@col2,product_id=@col1,s_num_code=@col3,o_num_code=@col4,call_type=1,type=2;

#Update rate in rate_plan for SMS
update rate_plan set rate=15 where type=2 and rate_plan_code like '%ON-NET';
update rate_plan set rate=18 where type=2 and rate_plan_code like'%OFF-NET';
update rate_plan set rate=102 where type=2 and product_id like 'INTERNATIONAL%';
update rate_plan set rate_plan_code='AIRTEL_OFF-NET_CDMA' where rate_plan_code='AIRTEL_OFF-NET CDMA';

#Update rate
update rate_plan set rate_plan_code='SONITEL_SHORT_NUMBER' where rate_plan_code = 'SONITEL_SHORT_CODES';

#insert SMS for Sonitel.

#verify count.
select count(*) from rate_plan;
select time_period, count(*) from rate_plan group by time_period;
select count(*) from rate_plan where rate_plan_code like '%SMS%';


#20140525
#delte airtel international voice rate plan, insert later with zone.
delete from rate_plan where rate_plan_code like 'AIRTEL_INT_%' and product_ID = '';

#add Airtel International rate with zone.
alter table rate_plan add column zone_id int(5) default 0, add column service_type int(5) default 0;

#create a tmp table for international zone info.
drop table tmp_int_zone_rate;
CREATE TABLE `tmp_int_zone_rate` (
  `reference` varchar(100) DEFAULT "",
  `zoning` varchar(50) DEFAULT "",
  `indicatif` int(10) DEFAULT 1,
  `destination` varchar(50) DEFAULT "",
  `num_code` varchar(50) DEFAULT "",
  `country` varchar(50) DEFAULT "",
  `zone_id` int(2) DEFAULT 0
)ENGINE=InnoDB DEFAULT CHARSET='utf8';

#load data from zone_country_list.csv
load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/zone_country_list.csv" into table tmp_int_zone_rate fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set reference=@col1,zoning=@col2,indicatif=@col3,destination=@col4;
load data infile "/tmp/zone_country_list.csv" into table tmp_int_zone_rate fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set reference=@col1,zoning=@col2,indicatif=@col3,destination=@col4;


delete from tmp_int_zone_rate where reference='Reference' or indicatif=0;

#fill num_code, code_location, zone_id values
update tmp_int_zone_rate set 
	num_code=(select num_code from numbering_plan where indicatif=num_prefix limit 1),
	country=(select code_location from numbering_plan where indicatif=num_prefix limit 1),
	zone_id=(select id from zone where zoning=zone_name);

#handle several exceptional/missing values
#insert into numbering plan already.

#insert zone rate items for Airtel into rate_plan.
insert into rate_plan 
	(rate_plan_code, product_ID, type, s_num_code, o_num_code, call_type, zone_id) 
select concat('AIRTEL_INT_',country), 'INTERNATIONAL', 1, 'NIGER_AIRTEL_MOBILE', num_code, 1, zone_id
from tmp_int_zone_rate;

#drop tmp table
drop table tmp_int_zone_rate;

#update service_type
update rate_plan set service_type=1 where type in (1,3,5);
update rate_plan set service_type=2 where type in (2,4,6);
