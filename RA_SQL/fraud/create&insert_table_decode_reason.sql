SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `decode_reason`
-- ----------------------------
drop table if exists  decode_reason;
CREATE TABLE `decode_reason` (
  `id` tinyint(3) unsigned NOT NULL,
  `reason` varchar(20) DEFAULT NULL,
  `desc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM;

-- ----------------------------
--  Records of `decode_reason`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

insert into decode_reason values(0, "Unknown", "Default for the file processing detail record");
insert into decode_reason values(1, "Successfully processed", "Indicates the file was successfully processed");
insert into decode_reason values(2, "No content", "File is decoded but the cdr doesn't have any data. file size is too small.");
insert into decode_reason values(3, "No known call types", "Expected call types were not found in the file");
insert into decode_reason values(4, "ZIP File error", "If a zip file cannot be opened or throws exception");
insert into decode_reason values(5, "File Format Error", "File document format error");

--1 dedicates file is  decoded with no error.
--2 dedicates file is decoded but the cdr doesn't have any data. file size is too small.
--3 dedicates file is decoded but the cdr doesn't include the valid data ,all are useless call types.
--4 dedicates file is corrupted.
--5 file document format error
