#20140715, initial.
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_roaming_summary`
-- ----------------------------
DROP TABLE IF EXISTS `ra_roaming_summary`;
CREATE TABLE `ra_roaming_summary` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT '00',
  `report_type` int(5) DEFAULT 0,
  `tap_code` varchar(10) DEFAULT NULL,
  `traffic_direction` varchar(50) DEFAULT 0,
	`imsi_prefix` varchar(5),
  `call_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  
  `product_ID` varchar(50) DEFAULT 0,
  `rate` float(9,3) DEFAULT 0,
  `amount_count` decimal(17,2) DEFAULT 0,
  `redo_times` int(11) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_roaming_summary`
--  PRIMARY KEY (`id`),
-- ----------------------------		

#20140715, add call id range for drill-down query.
alter table ra_roaming_summary add column min_call_id bigint(20) DEFAULT 0, add column max_call_id bigint(20) DEFAULT 0;
alter table ra_roaming_summary add column call_type int(3) DEFAULT 0;

#20140814, add call_count for voice and sms.
#if add data or accumulate amount, we have to update this via add call_type column and remove seperate counts.
alter table ra_roaming_summary add column `call_count_voice` bigint(15) DEFAULT 0, add column `call_count_sms` bigint(15) DEFAULT 0;

#20140918, compile 5-6 length imsi_prefix.
alter table ra_roaming_summary change column `imsi_prefix` `imsi_prefix` varchar(6);

#20150804, add for 425_6
alter table ra_roaming_summary add column `country_name` varchar(30) DEFAULT NULL, add column `call_count_data` bigint(15) DEFAULT 0, add column `download_volume_count` bigint(15) DEFAULT 0, add column `upload_volume_count` bigint(15) DEFAULT 0;

