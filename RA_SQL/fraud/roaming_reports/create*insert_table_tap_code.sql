
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tap_code`, 
-- ----------------------------
DROP TABLE IF EXISTS `tap_code`;
CREATE TABLE `tap_code` (
	`imsi_prefix` varchar(6),
  `tap_code` varchar(10) DEFAULT NULL,
	`imsi_range` varchar(100) DEFAULT NULL,
	`operator` varchar(300) DEFAULT NULL,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP,
  KEY (`imsi_prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `tap_code`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

#load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Palestine/doc/palestine_tap_code_list2.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2) set imsi_prefix=@col2,tap_code=@col1;

#load data infile "/tmp/palestine_tap_code_list2.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2) set imsi_prefix=@col2,tap_code=@col1;


load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Palestine/doc/palestine_tap_code_list3.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set imsi_prefix=@col4,tap_code=@col2,imsi_range=@col1,operator=@col3;

load data infile "/tmp/palestine_tap_code_list3.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set imsi_prefix=@col4,tap_code=@col2,imsi_range=@col1,operator=@col3;

delete from tap_code where imsi_prefix = 'SUBSTR';
update tap_code set imsi_range=substring(imsi_range,1,length(imsi_range)-1);

#@20141106, update request from customer
update tap_code set imsi_range='250396400000000-250396499999999' where imsi_range='250396490000000-250396499999999';

#@20150330, (execute individually) add two operators: ZAFVC (Vodacom (PTY) Ltd - South Africa) and KOROR (LG Telecom Korea)
insert into tap_code (imsi_prefix,tap_code,imsi_range,operator) values(65501, 'ZAFVC', '655010000000000-655019999999999', 'Vodacom (PTY) Ltd - South Africa');
insert into tap_code (imsi_prefix,tap_code,imsi_range,operator) values(45006, 'KOROR', '450060000000000-450069999999999', 'LG Telecom Korea');
