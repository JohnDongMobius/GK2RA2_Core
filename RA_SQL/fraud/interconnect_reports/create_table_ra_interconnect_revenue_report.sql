#140715, combine hourly and daily into one table.
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_interconnect_revenue_report`
-- ----------------------------
DROP TABLE IF EXISTS `ra_interconnect_revenue_report`;
CREATE TABLE `ra_interconnect_revenue_report` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT '00',
  `report_type` int(5) DEFAULT 0,
  `Franchise_ID` varchar(20) DEFAULT 0,
  `traffic_direction` varchar(50) DEFAULT 0,
  `interconnect_operator` varchar(50) DEFAULT 0,
  `product_ID` varchar(50) DEFAULT 0,
  `trunk_name` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `call_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  `amount_count` decimal(17,2) DEFAULT 0,
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_interconnect_revenue_report`
--  PRIMARY KEY (`id`),
-- ----------------------------		

SET FOREIGN_KEY_CHECKS = 1;

alter table ra_interconnect_revenue_report add column trunk_scope varchar(50) DEFAULT NULL;

#20140604, add for interconnect report requirement change.
alter table ra_interconnect_revenue_report add column moc_call_count bigint(15) DEFAULT 0, add column moc_duration_count bigint(15) DEFAULT 0, add column mtc_call_count bigint(15) DEFAULT 0, add column mtc_duration_count bigint(15) DEFAULT 0, add column tst_call_count bigint(15) DEFAULT 0, add column tst_duration_count bigint(15) DEFAULT 0;

#20140715, add call id range for drill-down query.
alter table ra_interconnect_revenue_report add column min_call_id bigint(20) DEFAULT 0, add column max_call_id bigint(20) DEFAULT 0;

#20140716, change product_id to traffic_type
alter table ra_interconnect_revenue_report change column product_ID traffic_type varchar(50) DEFAULT 0;
