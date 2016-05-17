#20140701: table created for v1 reorg.
#20140715: give up this table, as we combine daily and hourly. 
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_revenue_summary_hourly_v1`
-- ----------------------------
#DROP TABLE IF EXISTS `ra_revenue_summary_hourly_v1`;
#CREATE TABLE `ra_revenue_summary_hourly_v1` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `traffic_date` date DEFAULT NULL,
  `traffic_hour` varchar(2) DEFAULT '00',
  `report_type` int(5) DEFAULT 0,
  `call_type` varchar(20) DEFAULT 0,
  `franchise_ID` tinyint(3) DEFAULT 0,
  `s_num_code` varchar(50) DEFAULT 0,
  `o_num_code` varchar(50) DEFAULT 0,
  `product_ID` varchar(50) DEFAULT 0,
  `time_period` varchar(20) DEFAULT "",
  `rate` float(9,3) DEFAULT 0,
  `call_count` bigint(15) DEFAULT 0,
  `duration_count` bigint(15) DEFAULT 0,
  `amount_count` decimal(17,2) DEFAULT 0,
  `insert_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_traffic_date` (`traffic_date`),
  KEY `idx_report_type` (`report_type`),
  KEY `idx_call_type` (`call_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `ra_revenue_summary_hourly_v1`
-- ----------------------------		

SET FOREIGN_KEY_CHECKS = 1;

#rename product_id to sub_product_id
alter table ra_revenue_summary_hourly_v1 change product_ID sub_product_ID varchar(100) DEFAULT 0;

#add product_id
alter table ra_revenue_summary_hourly_v1 add column `product_ID` varchar(50) DEFAULT 0;

#20140715, add call id range for drill-down query.
alter table ra_revenue_summary_hourly_v1 add column min_call_id bigint(20) DEFAULT 0, add column max_call_id bigint(20) DEFAULT 0;

