
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

insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'QTEL%I', 'QTEL', 'Incoming', 'International');           
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'QTEL%O', 'QTEL', 'Outgoing', 'International');          
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BRK%I',  'Netvision', 'Incoming', 'International');       
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BRK%O',  'Netvision', 'Outgoing', 'International');      
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'GL%I',   '012 Smile', 'Incoming', 'International');        
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'GL%O',   '012 Smile', 'Outgoing', 'International');       
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BZI%I',  'Bezek', 'Incoming', 'International');          
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BZI%O',  'Bezek', 'Outgoing', 'International');         
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BIS%I',  'Bezek', 'Incoming', 'International');          
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BIS%O',  'Bezek', 'Outgoing', 'International');         
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'PAL%I',  'PALTEL', 'Incoming', 'International');          
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'PAL%O',  'PALTEL', 'Outgoing', 'International');         
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'NISC%I', 'ORANGE JORDON', 'Incoming', 'International');  
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'NISC%O', 'ORANGE JORDON', 'Outgoing', 'International'); 
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', '*', 'Unknown', 'Incoming', 'International');             
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', '*', 'Unknown', 'Outgoing', 'International');             

insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'OR%I', 'ORANGE JORDAN', 'Incoming', 'National');
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'OR%O', 'ORANGE JORDAN', 'Outgoing', 'National');  
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BZQ%I', 'BEZEK DOMESTIC', 'Incoming', 'National');
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'BZQ%O', 'BEZEK DOMESTIC', 'Outgoing', 'National');  
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'CEL%I', 'CELLCOM MOBILE', 'Incoming', 'National');
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'CEL%O', 'CELLCOM MOBILE', 'Outgoing', 'National');  
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'PL%I', 'PELEPHONE MOBILE', 'Incoming', 'National');
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', 'PL%O', 'PELEPHONE MOBILE', 'Outgoing', 'National');      
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', '*', 'Unknown', 'Incoming', 'National');             
insert into ra_trunk (source_operator_id, trunk_name, interconnect_operator, traffic_direction, trunk_scope) values('425_6', '*', 'Unknown', 'Outgoing', 'National');           

update ra_trunk set trunk_type=1 where traffic_direction='Incoming';
update ra_trunk set trunk_type=2 where traffic_direction='Outgoing';
