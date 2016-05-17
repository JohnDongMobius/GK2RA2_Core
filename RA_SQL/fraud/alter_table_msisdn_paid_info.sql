alter table msisdn_paid_info add column (
	imsi bigint(8) DEFAULT 0, 
	p_date varchar(20) DEFAULT NULL, 
	KEY `idx_p_date` (`p_date`));
	
	
	