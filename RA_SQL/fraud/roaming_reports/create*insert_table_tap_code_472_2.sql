
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tap_code`, 
-- ----------------------------
DROP TABLE IF EXISTS `tap_code`;
CREATE TABLE `tap_code` (
	`imsi_prefix` varchar(6),
  `tap_code` varchar(10) DEFAULT NULL,
 	`country_name` varchar(30) DEFAULT NULL,
	`imsi_range` varchar(100) DEFAULT NULL,
	`msisdn_prefix` varchar(100) DEFAULT NULL,
	`operator` varchar(300) DEFAULT NULL,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP,
  KEY (`imsi_prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `tap_code`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Maldives/doc/472_2_tapcode_list_v2.0.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5) set imsi_prefix=@col4,tap_code=@col1,msisdn_prefix=@col5,operator=@col2,country_name=@col3;

delete from tap_code where imsi_prefix = 'IMSI';

load data infile "/tmp/472_2_tapcode_list_v2.0.csv" into table tap_code fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4,@col5) set imsi_prefix=@col4,tap_code=@col1,msisdn_prefix=@col5,operator=@col2,country_name=@col3;

delete from tap_code where imsi_prefix = 'IMSI';

