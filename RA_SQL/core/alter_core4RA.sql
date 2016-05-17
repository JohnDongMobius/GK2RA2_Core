--2013-06-06: add some columns
--done: 472_2, 425_6, 614_2,3,4
alter table ra_billing change column `sw_id` `sw_id` mediumint(5) UNSIGNED DEFAULT NULL;

--done: 472_2
alter table ra_billing add column insert_time datetime default null, add file_name varchar(60), change column `trunk_in` `trunk_in` mediumint(5) UNSIGNED DEFAULT NULL,change column `trunk_out` `trunk_out` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_cause` `term_cause` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_reason` `term_reason` mediumint(5) UNSIGNED DEFAULT NULL;
alter table ra_billing add column end_time datetime default null;

--done: 472_2
alter table ra_billing_nodup add column insert_time datetime default null, add file_name varchar(60), change column `sw_id` `sw_id` mediumint(5) UNSIGNED DEFAULT NULL, change column `trunk_in` `trunk_in` mediumint(5) UNSIGNED DEFAULT NULL,change column `trunk_out` `trunk_out` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_cause` `term_cause` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_reason` `term_reason` mediumint(5) UNSIGNED DEFAULT NULL;

--done: 
alter table ra_billing_nodup add column end_time datetime default null;

--done: 472_2
alter table ra_billing_nodup_redo add column insert_time datetime default null, add file_name varchar(60), change column `sw_id` `sw_id` mediumint(5) UNSIGNED DEFAULT NULL, change column `trunk_in` `trunk_in` mediumint(5) UNSIGNED DEFAULT NULL,change column `trunk_out` `trunk_out` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_cause` `term_cause` mediumint(5) UNSIGNED DEFAULT NULL, change column `term_reason` `term_reason` mediumint(5) UNSIGNED DEFAULT NULL;

--done: 
alter table ra_billing_nodup_redo add column end_time datetime default null;

--2013-07-03: add for RA-PP-V2.
alter table ra_billing add column call_id bigint(20) NOT NULL AUTO_INCREMENT, add PRIMARY KEY (call_time, call_id), add KEY (call_id);
alter table ra_billing_nodup add column call_id bigint(20) NOT NULL, add PRIMARY KEY (call_time, call_id), add KEY (call_id);

--2013-07-23: add for Data.
alter table ra_billing add column volume_download bigint(20), add column volume_upload bigint(20), add column charge_identifier bigint(20);
alter table ra_billing_nodup add column volume_download bigint(20), add column volume_upload bigint(20), add column charge_identifier bigint(20);
alter table ra_billing_nodup_redo add column volume_download bigint(20), add column volume_upload bigint(20), add column charge_identifier bigint(20);

--2013-07-24: adjust default values for number type.
alter table ra_billing 
	alter column duration set default 0, alter column call_type set default 0,alter column sw_id set default 0,
	alter column s_msisdn set default 0,alter column o_msisdn set default 0,alter column s_imsi set default 0,
	alter column s_imei set default 0,alter column s_ci set default 0,alter column s_lac set default 0,
	alter column trunk_in set default 0,alter column trunk_out set default 0,alter column term_cause set default 0,
	alter column term_reason set default 0,alter column ss_code set default 0,alter column charge_indicator set default 0,
	alter column feed_type set default 0,alter column billing_type set default 0, alter column volume_download set default 0, 
	alter column volume_upload set default 0, alter column charge_identifier set default 0;
	
alter table ra_billing_nodup 
	alter column duration set default 0, alter column call_type set default 0,alter column sw_id set default 0,
	alter column s_msisdn set default 0,alter column o_msisdn set default 0,alter column s_imsi set default 0,
	alter column s_imei set default 0,alter column s_ci set default 0,alter column s_lac set default 0,
	alter column trunk_in set default 0,alter column trunk_out set default 0,alter column term_cause set default 0,
	alter column term_reason set default 0,alter column ss_code set default 0,alter column charge_indicator set default 0,
	alter column feed_type set default 0,alter column billing_type set default 0, alter column volume_download set default 0, 
	alter column volume_upload set default 0, alter column charge_identifier set default 0;
	
alter table ra_billing_nodup_redo 
	alter column duration set default 0, alter column call_type set default 0,alter column sw_id set default 0,
	alter column s_msisdn set default 0,alter column o_msisdn set default 0,alter column s_imsi set default 0,
	alter column s_imei set default 0,alter column s_ci set default 0,alter column s_lac set default 0,
	alter column trunk_in set default 0,alter column trunk_out set default 0,alter column term_cause set default 0,
	alter column term_reason set default 0,alter column ss_code set default 0,alter column charge_indicator set default 0,
	alter column feed_type set default 0,alter column billing_type set default 0, alter column volume_download set default 0, 
	alter column volume_upload set default 0, alter column charge_identifier set default 0;

--2013-08-07: add ip addresses.
alter table ra_billing add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 
alter table ra_billing_nodup add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 
alter table ra_billing_nodup_redo add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 
alter table ra_network add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 
alter table ra_network_nodup add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 
alter table ra_network_nodup_redo add column sgsn_address varchar(30) DEFAULT NULL, add column ggsn_address varchar(30) DEFAULT NULL, add column pdp_address varchar(30) DEFAULT NULL; 

--2013-08-08: remove auto_increment of call_id
alter table ra_network_nodup modify column call_id bigint(20) NOT NULL;
alter table ra_network_nodup_redo modify column call_id bigint(20) NOT NULL;

--2013-09-04: also need to add feed_type in call_count table. feed_type=1**(MSC),2**(GPRS-Network),3**(billing)
--						feed_type value options: 101-MSC feed, 201-SGSN feed, 202-GGSN feed, 301-IN feed, 302-TAPOUT feed, 303-TAPIN feed, 304-Billing Huaiwei feed, 305-Legacy feed
alter table call_count add column feed_type int(5) default 0;
alter table call_count add column file_type int(5) default 0;


--2013-11-15: add call_amount in ra_billing.
alter table ra_billing add column call_amount int(8) default 0;
alter table ra_billing_nodup add column call_amount int(8) default 0;
alter table ra_billing_nodup_redo add column call_amount int(8) default 0;

--2014-02-27: modify feed type for ra_billing_nodup and ra_billing_nodup_redo
alter table ra_billing_nodup change column `feed_type` `feed_type` int(3) default 0;
alter table ra_billing_nodup_redo change column `feed_type` `feed_type` int(3) default 0;

--2014-05-30: add forwarded_num to storage real called num, add transit_call_id to storage call id of mapping transit CDR.
alter table calls_nodup add column forwarded_num bigint(20) unsigned, add column transit_call_id bigint(20) unsigned;
alter table calls_nodup_redo add column forwarded_num bigint(20) unsigned, add column transit_call_id bigint(20) unsigned;

--2014-07-14: add forwarded_num to storage real called num.
alter table ra_billing add column forwarded_num bigint(20) unsigned;
alter table ra_billing_nodup add column forwarded_num bigint(20) unsigned;
alter table ra_billing_nodup_redo add column forwarded_num bigint(20) unsigned;

--2014-09-16: add forwarded_num for consistent process of PP.
alter table ra_network add column forwarded_num bigint(20) unsigned;
alter table ra_network_nodup add column forwarded_num bigint(20) unsigned;
alter table ra_network_nodup_redo add column forwarded_num bigint(20) unsigned;

