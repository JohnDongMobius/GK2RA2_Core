
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


load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Maldives/doc/472_2_trunk_list.csv" into table ra_trunk fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3) set source_operator_id='472_2',trunk_name=@col1,interconnect_operator=@col2,traffic_direction='Incoming',trunk_scope=@col3;

load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Maldives/doc/472_2_trunk_list.csv" into table ra_trunk fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3) set source_operator_id='472_2',trunk_name=@col1,interconnect_operator=@col2,traffic_direction='Outgoing',trunk_scope=@col3;

load data infile "/tmp/472_2_trunk_list.csv" into table ra_trunk fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3) set source_operator_id='472_2',trunk_name=@col1,interconnect_operator=@col2,traffic_direction='Incoming',trunk_scope=@col3;

load data infile "/tmp/472_2_trunk_list.csv" into table ra_trunk fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3) set source_operator_id='472_2',trunk_name=@col1,interconnect_operator=@col2,traffic_direction='Outgoing',trunk_scope=@col3;


update ra_trunk set trunk_type=1 where traffic_direction='Incoming';
update ra_trunk set trunk_type=2 where traffic_direction='Outgoing';
update ra_trunk set trunk_scope='Internal' where trunk_scope = 'Ooredoo';
update ra_trunk set trunk_scope='National' where trunk_scope = 'Domestic';

#Remove internal trunk, as only summarize domestic and international.
delete from ra_trunk where trunk_scope='Internal';
