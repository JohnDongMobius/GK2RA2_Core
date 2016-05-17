CREATE TABLE `ra_trunk_excluded` (
  `trunk_id` mediumint(5) unsigned NOT NULL,
  `trunk_value` varchar(8) DEFAULT NULL,
  `trunk_type` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`trunk_id`),
  KEY `Index_trunk_1` (`trunk_id`),
  KEY `Index_trunk_2` (`trunk_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into ra_trunk_excluded (trunk_id,trunk_value,trunk_type) value (2,'0GRI3','2');
insert into ra_trunk_excluded (trunk_id,trunk_value,trunk_type) value (0,'0','2');
