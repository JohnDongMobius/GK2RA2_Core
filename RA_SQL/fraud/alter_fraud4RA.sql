--For file count requirement.
alter table ra_summary add column `msc_file_count` int(11) DEFAULT NULL, add `billing_file_count` int(11) DEFAULT NULL;
alter table ra_detail add column insert_time datetime default null, add file_name varchar(60), change column `sw_id` `sw_id` mediumint(5) UNSIGNED DEFAULT NULL, change column `trunk_in` `trunk_in` mediumint(5) UNSIGNED DEFAULT NULL,change column `trunk_out` `trunk_out` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_cause` `term_cause` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_reason` `term_reason` mediumint(5) UNSIGNED DEFAULT NULL;

--For email alert requirement.
alter table ra_summary add `email_alert` int(1) DEFAULT 0;

--2013-07-23 for data(sgsn/ggsn) requirement.
alter table ra_detail add column volume_download bigint(20), add column volume_upload bigint(20), add column charge_identifier bigint(20);

--2013-07-24 adjust default values for number type. After this, need to update legacy values. Otherwise, portal will show Number errors.
alter table ra_detail 
	alter column duration set default 0, alter column call_type set default 0,alter column sw_id set default 0,
	alter column s_msisdn set default 0,alter column o_msisdn set default 0,alter column s_imsi set default 0,
	alter column s_imei set default 0,alter column s_ci set default 0,alter column s_lac set default 0,
	alter column trunk_in set default 0,alter column trunk_out set default 0,alter column term_cause set default 0,
	alter column term_reason set default 0,alter column ss_code set default 0,alter column charge_indicator set default 0,
	alter column detail_type set default 0,alter column billing_type set default 0, alter column volume_download set default 0, 
	alter column volume_upload set default 0, alter column charge_identifier set default 0; 	
	
alter table ra_file_detail
	alter column billing_type set default 0, alter column detail_type set default 0;
	
--2013-08-07: add ip addresses.
alter table ra_detail add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 

--2013-08-?: adjust default values for number type.

--2013-08-15: adjust table name and field name to correct process logic.
alter table ra_summary change column billing_type report_type int;
alter table ra_file_detail rename ra_file_name;
alter table ra_file_name change column billing_type report_type int;
alter table ra_file_name change column detail_type respective_type int;
--update ra_file_name set respective_type=2;
alter table ra_detail change column billing_type report_type int;

--2013-09-04: add type for ra_file_name to distinguish which feed it belongs to.
--						type value options: 101-MSC feed, 201-SGSN feed, 202-GGSN feed, 301-IN feed, 302-TAPOUT feed, 303-TAPIN feed, 304-Billing Huaiwei feed
alter table ra_file_name add column type int(5) default 0, add column file_size_bytes bigint(20) default 0;
alter table ra_file_name rename ra_feed;
alter table ra_feed change call_time traffic_date date default NULL;
--insert into ra_file_name (traffic_date,file_name,report_type,respective_type,insert_time,type,file_size_bytes) select traffic_date,file_name,report_type,respective_type,insert_time,type,file_size_bytes from ra_feed;

--2013-10-12: add CUG and group num.
alter table ra_detail add column short_num int(5) default 0, add column group_num int(3) default 0;

--2013-11-15: add call_amount.
alter table ra_detail add column call_amount int(8) default 0;

--2014-03-26: add file-report.
alter table ra_feed_summary add column uploaded_file_count int(11) DEFAULT 0, add column processed_file_count int(11) DEFAULT 0, add column unprocessed_file_count int(11) DEFAULT 0;
alter table ra_feed add column unprocessed_reason varchar(10) DEFAULT NULL;
alter table ra_feed alter column respective_type set default 1;
update ra_feed set respective_type=1;

--2015-04-13: update id for ra_detail.
alter table ra_detail change id id bigint(20) NOT NULL AUTO_INCREMENT;

--2015-10-14: extend duration size.
alter table ra_summary change billing_duration billing_duration bigint(20) default 0;
alter table ra_summary change moc_duration_without_billing moc_duration_without_billing bigint(20) default 0;
alter table ra_summary change billing_duration_without_moc billing_duration_without_moc bigint(20) default 0;
alter table ra_summary change moc_duration moc_duration bigint(20) default 0;

--2016-5-12: add data volume gap for data volume mimatching function.
alter table ra_summary add column data_volum_gap bigint(20) default 0;
