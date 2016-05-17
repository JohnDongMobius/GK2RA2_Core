alter table recon_moc_detail add column (
	`sw_id` tinyint(3) unsigned DEFAULT NULL,
	`s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` smallint(5) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL
  );
  
  alter table recon_in_detail add column (
	`sw_id` tinyint(3) unsigned DEFAULT NULL,
	`s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` smallint(5) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL
  );
  
  alter table recon_moc_detail_out add column (
	`sw_id` tinyint(3) unsigned DEFAULT NULL,
	`s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` smallint(5) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL
  );
  
  alter table recon_out_detail add column (
	`sw_id` tinyint(3) unsigned DEFAULT NULL,
	`s_imsi` bigint(20) unsigned DEFAULT NULL,
  `s_imei` bigint(20) unsigned DEFAULT NULL,
  `s_ci` smallint(5) unsigned DEFAULT NULL,
  `s_lac` smallint(5) unsigned DEFAULT NULL,
  `trunk_in` smallint(5) unsigned DEFAULT NULL,
  `trunk_out` smallint(5) unsigned DEFAULT NULL,
  `term_cause` tinyint(3) unsigned DEFAULT NULL,
  `term_reason` tinyint(3) unsigned DEFAULT NULL,
  `ss_code` tinyint(3) unsigned DEFAULT NULL,
  `charge_indicator` tinyint(3) unsigned DEFAULT NULL
  );