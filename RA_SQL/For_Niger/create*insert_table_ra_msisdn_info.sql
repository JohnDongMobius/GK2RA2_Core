
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_msisdn_info`, 
-- ----------------------------
DROP TABLE IF EXISTS `ra_msisdn_info`;
CREATE TABLE `ra_msisdn_info` (
  `msisdn` bigint(20) DEFAULT NULL,
  `type` tinyint(3) DEFAULT NULL,
  `filter_type` tinyint(3) DEFAULT NULL,
  `imsi` bigint(8) DEFAULT '0',
  `short_num` bigint(10) DEFAULT NULL,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_msisdn_info_msisdn` (`msisdn`, `type`)
) ENGINE=InnoDB AUTO_INCREMENT=46220 DEFAULT CHARSET=utf8;

#for Palestine start
load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Palestine/doc/Wataniya Palestine_Toll Free Numbers.csv" into table ra_msisdn_info fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2) set msisdn="970"+@col2,short_num=@col1,type=2,filter_type=2;

load data infile "/tmp/Wataniya Palestine_Toll Free Numbers.csv" into table ra_msisdn_info fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2) set msisdn=@col2,short_num=@col1,type=2,filter_type=2;

update ra_msisdn_info set msisdn=97000000000+msisdn where type=2 and filter_type=2 and length(msisdn)=8;
update ra_msisdn_info set msisdn=970000000000+msisdn where type=2 and filter_type=2 and length(msisdn)=9;

#for Palestine end
