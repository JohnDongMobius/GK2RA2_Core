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
--  Table structure for `numbering_plan`
-- ----------------------------
DROP TABLE IF EXISTS `numbering_plan`;
CREATE TABLE `numbering_plan` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `mcc` varchar(8) NOT NULL,
  `mnc` varchar(8) NOT NULL,
  `num_prefix` int(8) DEFAULT 0,
  `num_code` varchar(50) NULL,
  `code_location` varchar(30) NULL,
  `description` varchar(100) NULL,
  `insert_time` timestamp DEFAULT current_timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `numbering_plan`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


#For Sahelcom
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,1,22793,'NIGER_SAHELCOM_MOBILE','Niger','Sahelcom GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,227098612,'NIGER_SAHELCOM_FAX_SERVICES','Niger','Sahelcom Fax Services',current_timestamp());

#For Airtel
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22788,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22789,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22796,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22797,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22798,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,2,22799,'NIGER_AIRTEL_MOBILE','Niger','Airtel GSM',current_timestamp());

#For Moov
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,3,22794,'NIGER_MOOV_MOBILE','Niger','Moov GSM',current_timestamp());

#For Orange
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,4,22723,'NIGER_ORANGE_MOBILE','Niger','Orange GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,4,22790,'NIGER_ORANGE_MOBILE','Niger','Orange GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,4,22791,'NIGER_ORANGE_MOBILE','Niger','Orange GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,4,22792,'NIGER_ORANGE_MOBILE','Niger','Orange GSM',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,4,22780,'NIGER_ORANGE_MOBILE','Niger','Orange GSM',current_timestamp());
                                               
#For Sonitel
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,227080,'NIGER_SONITEL_FREEPHONE','Niger','Sonitel FreePhone',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,22708300,'NIGER_SONITEL_UNIVERSAL_ACCESS','Niger','Sonitel Uniersal Call 1',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,22708700,'NIGER_SONITEL_UNIVERSAL_ACCESS','Niger','Sonitel Uniersal Call 2',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,2270986,'NIGER_SONITEL_FAX_SERVICES','Niger','Sonitel Fax Services',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,22720,'SONITEL_GEOGRAPHIC','Niger','Sonitel Fixline',current_timestamp());
insert into numbering_plan (mcc,mnc,num_prefix,num_code,code_location,description,insert_time) values (614,6,22721,'SONITEL_MOBILE','Niger','Sonitel CDMA',current_timestamp());


#insert into international code.
#load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/Niger Number Plan V.5.csv" into table numbering_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set num_prefix=@col1,num_code=@col3,code_location=@col4,description=@col2;

load data infile "/tmp/Niger Number Plan V.5.csv" into table numbering_plan fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set num_prefix=@col1,num_code=@col3,code_location=@col4,description=@col2;
