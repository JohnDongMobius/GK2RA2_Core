CREATE TABLE `hot_irsf_num` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `source` varchar(32),
  `type` varchar(32),
  `irsf_number` varchar(32) ,
  `country` varchar(255) ,
  `cdr_attribute` varchar(255) ,
  `description` varchar(255) ,
  `date_entered` datetime ,
  `entered_by` varchar(64) ,
  `date_updated` datetime,
  `updated_by` varchar(64) ,
  `expiration_date` datetime ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table hot_irsf_num add column data_source_doc_name varchar(255); 