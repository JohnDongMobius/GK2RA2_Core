/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 12/22/2011 17:53:05 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `irsf_detail`
-- ----------------------------
DROP TABLE IF EXISTS `irsf_detail`;
/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50519
 Source Host           : localhost
 Source Database       : core_603_3

 Target Server Version : 50519
 File Encoding         : utf-8

 Date: 12/22/2011 17:53:05 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `irsf_detail`
-- ----------------------------
DROP TABLE IF EXISTS `irsf_detail`;
CREATE TABLE `irsf_detail` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `call_type` tinyint(2) DEFAULT '0',
  `type` varchar(30) DEFAULT NULL,
  
  `s_msisdn` bigint(20) DEFAULT '0',
  `o_msisdn` bigint(20) DEFAULT '0',
  `duration` bigint(20) DEFAULT '0',
  `call_time` datetime NOT NULL,
  `sw_id` mediumint(5) unsigned DEFAULT '0',
  `s_imsi` bigint(20) unsigned DEFAULT '0',
  `s_imei` bigint(20) unsigned DEFAULT '0',
  `s_ci` int(5) unsigned DEFAULT '0',
  `s_lac` smallint(5) unsigned DEFAULT '0',
  `trunk_in` mediumint(5) unsigned DEFAULT '0',
  `trunk_out` mediumint(5) unsigned DEFAULT '0',
  `term_cause` mediumint(5) unsigned DEFAULT '0',
  `term_reason` mediumint(5) unsigned DEFAULT '0',
  `ss_code` int(10) unsigned DEFAULT '0',
  `charge_indicator` tinyint(3) unsigned DEFAULT '0',
  `detail_type` tinyint(3) unsigned DEFAULT '0',
  `report_type` varchar(3) DEFAULT NULL,
  `insert_time` datetime DEFAULT NULL,
  `file_name` varchar(60) DEFAULT NULL,
  `volume_download` bigint(20) DEFAULT '0',
  `volume_upload` bigint(20) DEFAULT '0',
  `charge_identifier` bigint(20) DEFAULT '0',
  `sgsn_address` varchar(30) DEFAULT NULL,
  `ggsn_address` varchar(30) DEFAULT NULL,
  `pdp_address` varchar(30) DEFAULT NULL,
  `short_num` int(5) DEFAULT '0',
  `group_num` int(3) DEFAULT '0',
  PRIMARY KEY (`id`,`call_time`),
  KEY `call_time` (`call_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
PARTITION BY RANGE (TO_DAYS(call_time))
(PARTITION p20150521 VALUES LESS THAN (736105) ENGINE = InnoDB,
 PARTITION p20150522 VALUES LESS THAN (736106) ENGINE = InnoDB,
 PARTITION p20150523 VALUES LESS THAN (736107) ENGINE = InnoDB,
 PARTITION p20150524 VALUES LESS THAN (736108) ENGINE = InnoDB,
 PARTITION p20150525 VALUES LESS THAN (736109) ENGINE = InnoDB,
 PARTITION p20150526 VALUES LESS THAN (736110) ENGINE = InnoDB,
 PARTITION p20150527 VALUES LESS THAN (736111) ENGINE = InnoDB,
 PARTITION p20150528 VALUES LESS THAN (736112) ENGINE = InnoDB,
 PARTITION p20150529 VALUES LESS THAN (736113) ENGINE = InnoDB,
 PARTITION p20150530 VALUES LESS THAN (736114) ENGINE = InnoDB,
 PARTITION p20150531 VALUES LESS THAN (736115) ENGINE = InnoDB,
 PARTITION p20150601 VALUES LESS THAN (736116) ENGINE = InnoDB,
 PARTITION p20150602 VALUES LESS THAN (736117) ENGINE = InnoDB,
 PARTITION p20150603 VALUES LESS THAN (736118) ENGINE = InnoDB,
 PARTITION p20150604 VALUES LESS THAN (736119) ENGINE = InnoDB,
 PARTITION p20150605 VALUES LESS THAN (736120) ENGINE = InnoDB,
 PARTITION p20150606 VALUES LESS THAN (736121) ENGINE = InnoDB,
 PARTITION p20150607 VALUES LESS THAN (736122) ENGINE = InnoDB,
 PARTITION p20150608 VALUES LESS THAN (736123) ENGINE = InnoDB,
 PARTITION p20150609 VALUES LESS THAN (736124) ENGINE = InnoDB,
 PARTITION p20150610 VALUES LESS THAN (736125) ENGINE = InnoDB,
 PARTITION p20150611 VALUES LESS THAN (736126) ENGINE = InnoDB,
 PARTITION p20150612 VALUES LESS THAN (736127) ENGINE = InnoDB,
 PARTITION p20150613 VALUES LESS THAN (736128) ENGINE = InnoDB,
 PARTITION p20150614 VALUES LESS THAN (736129) ENGINE = InnoDB,
 PARTITION p20150615 VALUES LESS THAN (736130) ENGINE = InnoDB,
 PARTITION p20150616 VALUES LESS THAN (736131) ENGINE = InnoDB,
 PARTITION p20150617 VALUES LESS THAN (736132) ENGINE = InnoDB,
 PARTITION p20150618 VALUES LESS THAN (736133) ENGINE = InnoDB,
 PARTITION p20150619 VALUES LESS THAN (736134) ENGINE = InnoDB,
 PARTITION p20150620 VALUES LESS THAN (736135) ENGINE = InnoDB,
 PARTITION p20150621 VALUES LESS THAN (736136) ENGINE = InnoDB,
 PARTITION p20150622 VALUES LESS THAN (736137) ENGINE = InnoDB,
 PARTITION p20150623 VALUES LESS THAN (736138) ENGINE = InnoDB,
 PARTITION p20150624 VALUES LESS THAN (736139) ENGINE = InnoDB,
 PARTITION p20150625 VALUES LESS THAN (736140) ENGINE = InnoDB,
 PARTITION p20150626 VALUES LESS THAN (736141) ENGINE = InnoDB,
 PARTITION p20150627 VALUES LESS THAN (736142) ENGINE = InnoDB,
 PARTITION p20150628 VALUES LESS THAN (736143) ENGINE = InnoDB,
 PARTITION p20150629 VALUES LESS THAN (736144) ENGINE = InnoDB,
 PARTITION p20150630 VALUES LESS THAN (736145) ENGINE = InnoDB,
 PARTITION p20150701 VALUES LESS THAN (736146) ENGINE = InnoDB,
 PARTITION p20150702 VALUES LESS THAN (736147) ENGINE = InnoDB,
 PARTITION p20150703 VALUES LESS THAN (736148) ENGINE = InnoDB,
 PARTITION p20150704 VALUES LESS THAN (736149) ENGINE = InnoDB,
 PARTITION p20150705 VALUES LESS THAN (736150) ENGINE = InnoDB,
 PARTITION p20150706 VALUES LESS THAN (736151) ENGINE = InnoDB,
 PARTITION p20150707 VALUES LESS THAN (736152) ENGINE = InnoDB,
 PARTITION p20150708 VALUES LESS THAN (736153) ENGINE = InnoDB,
 PARTITION p20150709 VALUES LESS THAN (736154) ENGINE = InnoDB,
 PARTITION p20150710 VALUES LESS THAN (736155) ENGINE = InnoDB,
 PARTITION p20150711 VALUES LESS THAN (736156) ENGINE = InnoDB,
 PARTITION p20150712 VALUES LESS THAN (736157) ENGINE = InnoDB,
 PARTITION p20150713 VALUES LESS THAN (736158) ENGINE = InnoDB,
 PARTITION p20150714 VALUES LESS THAN (736159) ENGINE = InnoDB,
 PARTITION p20150715 VALUES LESS THAN (736160) ENGINE = InnoDB,
 PARTITION p20150716 VALUES LESS THAN (736161) ENGINE = InnoDB,
 PARTITION p20150717 VALUES LESS THAN (736162) ENGINE = InnoDB,
 PARTITION p20150718 VALUES LESS THAN (736163) ENGINE = InnoDB,
 PARTITION p20150719 VALUES LESS THAN (736164) ENGINE = InnoDB,
 PARTITION p20150720 VALUES LESS THAN (736165) ENGINE = InnoDB,
 PARTITION p20150721 VALUES LESS THAN (736166) ENGINE = InnoDB,
 PARTITION p20150722 VALUES LESS THAN (736167) ENGINE = InnoDB,
 PARTITION p20150723 VALUES LESS THAN (736168) ENGINE = InnoDB,
 PARTITION p20150724 VALUES LESS THAN (736169) ENGINE = InnoDB,
 PARTITION p20150725 VALUES LESS THAN (736170) ENGINE = InnoDB,
 PARTITION p20150726 VALUES LESS THAN (736171) ENGINE = InnoDB,
 PARTITION p20150727 VALUES LESS THAN (736172) ENGINE = InnoDB,
 PARTITION p20150728 VALUES LESS THAN (736173) ENGINE = InnoDB,
 PARTITION p20150729 VALUES LESS THAN (736174) ENGINE = InnoDB,
 PARTITION p20150730 VALUES LESS THAN (736175) ENGINE = InnoDB,
 PARTITION p20150731 VALUES LESS THAN (736176) ENGINE = InnoDB,
 PARTITION p20150801 VALUES LESS THAN (736177) ENGINE = InnoDB,
 PARTITION p20150802 VALUES LESS THAN (736178) ENGINE = InnoDB,
 PARTITION p20150803 VALUES LESS THAN (736179) ENGINE = InnoDB,
 PARTITION p20150804 VALUES LESS THAN (736180) ENGINE = InnoDB,
 PARTITION p20150805 VALUES LESS THAN (736181) ENGINE = InnoDB,
 PARTITION p20150806 VALUES LESS THAN (736182) ENGINE = InnoDB,
 PARTITION p20150807 VALUES LESS THAN (736183) ENGINE = InnoDB,
 PARTITION p20150808 VALUES LESS THAN (736184) ENGINE = InnoDB,
 PARTITION p20150809 VALUES LESS THAN (736185) ENGINE = InnoDB,
 PARTITION p20150810 VALUES LESS THAN (736186) ENGINE = InnoDB,
 PARTITION p20150811 VALUES LESS THAN (736187) ENGINE = InnoDB,
 PARTITION p20150812 VALUES LESS THAN (736188) ENGINE = InnoDB,
 PARTITION p20150813 VALUES LESS THAN (736189) ENGINE = InnoDB,
 PARTITION p20150814 VALUES LESS THAN (736190) ENGINE = InnoDB,
 PARTITION p20150815 VALUES LESS THAN (736191) ENGINE = InnoDB,
 PARTITION p20150816 VALUES LESS THAN (736192) ENGINE = InnoDB,
 PARTITION p20150817 VALUES LESS THAN (736193) ENGINE = InnoDB,
 PARTITION p20150818 VALUES LESS THAN (736194) ENGINE = InnoDB,
 PARTITION p20150819 VALUES LESS THAN (736195) ENGINE = InnoDB,
 PARTITION p20150820 VALUES LESS THAN (736196) ENGINE = InnoDB,
 PARTITION p20150821 VALUES LESS THAN (736197) ENGINE = InnoDB,
 PARTITION p20150822 VALUES LESS THAN (736198) ENGINE = InnoDB,
 PARTITION p20150823 VALUES LESS THAN (736199) ENGINE = InnoDB,
 PARTITION p20150824 VALUES LESS THAN (736200) ENGINE = InnoDB,
 PARTITION p20150825 VALUES LESS THAN (736201) ENGINE = InnoDB,
 PARTITION p20150826 VALUES LESS THAN (736202) ENGINE = InnoDB,
 PARTITION p20150827 VALUES LESS THAN (736203) ENGINE = InnoDB,
 PARTITION p20150828 VALUES LESS THAN (736204) ENGINE = InnoDB,
 PARTITION p20150829 VALUES LESS THAN (736205) ENGINE = InnoDB,
 PARTITION p20150830 VALUES LESS THAN (736206) ENGINE = InnoDB,
 PARTITION p20150831 VALUES LESS THAN (736207) ENGINE = InnoDB,
 PARTITION p20150901 VALUES LESS THAN (736208) ENGINE = InnoDB,
 PARTITION p20150902 VALUES LESS THAN (736209) ENGINE = InnoDB,
 PARTITION p20150903 VALUES LESS THAN (736210) ENGINE = InnoDB,
 PARTITION p20150904 VALUES LESS THAN (736211) ENGINE = InnoDB,
 PARTITION p20150905 VALUES LESS THAN (736212) ENGINE = InnoDB,
 PARTITION p20150906 VALUES LESS THAN (736213) ENGINE = InnoDB,
 PARTITION p20150907 VALUES LESS THAN (736214) ENGINE = InnoDB,
 PARTITION p20150908 VALUES LESS THAN (736215) ENGINE = InnoDB,
 PARTITION p20150909 VALUES LESS THAN (736216) ENGINE = InnoDB,
 PARTITION p20150910 VALUES LESS THAN (736217) ENGINE = InnoDB,
 PARTITION p20150911 VALUES LESS THAN (736218) ENGINE = InnoDB,
 PARTITION p20150912 VALUES LESS THAN (736219) ENGINE = InnoDB,
 PARTITION p20150913 VALUES LESS THAN (736220) ENGINE = InnoDB,
 PARTITION p20150914 VALUES LESS THAN (736221) ENGINE = InnoDB,
 PARTITION p20150915 VALUES LESS THAN (736222) ENGINE = InnoDB,
 PARTITION p20150916 VALUES LESS THAN (736223) ENGINE = InnoDB,
 PARTITION p20150917 VALUES LESS THAN (736224) ENGINE = InnoDB,
 PARTITION p20150918 VALUES LESS THAN (736225) ENGINE = InnoDB,
 PARTITION p20150919 VALUES LESS THAN (736226) ENGINE = InnoDB,
 PARTITION p20150920 VALUES LESS THAN (736227) ENGINE = InnoDB,
 PARTITION p20150921 VALUES LESS THAN (736228) ENGINE = InnoDB,
 PARTITION p20150922 VALUES LESS THAN (736229) ENGINE = InnoDB,
 PARTITION p20150923 VALUES LESS THAN (736230) ENGINE = InnoDB,
 PARTITION p20150924 VALUES LESS THAN (736231) ENGINE = InnoDB,
 PARTITION p20150925 VALUES LESS THAN (736232) ENGINE = InnoDB,
 PARTITION p20150926 VALUES LESS THAN (736233) ENGINE = InnoDB,
 PARTITION p20150927 VALUES LESS THAN (736234) ENGINE = InnoDB,
 PARTITION p20150928 VALUES LESS THAN (736235) ENGINE = InnoDB,
 PARTITION p20150929 VALUES LESS THAN (736236) ENGINE = InnoDB,
 PARTITION p20150930 VALUES LESS THAN (736237) ENGINE = InnoDB,
 PARTITION p20151001 VALUES LESS THAN (736238) ENGINE = InnoDB,
 PARTITION p20151002 VALUES LESS THAN (736239) ENGINE = InnoDB,
 PARTITION p20151003 VALUES LESS THAN (736240) ENGINE = InnoDB,
 PARTITION p20151004 VALUES LESS THAN (736241) ENGINE = InnoDB,
 PARTITION p20151005 VALUES LESS THAN (736242) ENGINE = InnoDB,
 PARTITION p20151006 VALUES LESS THAN (736243) ENGINE = InnoDB,
 PARTITION p20151007 VALUES LESS THAN (736244) ENGINE = InnoDB,
 PARTITION p20151008 VALUES LESS THAN (736245) ENGINE = InnoDB,
 PARTITION p20151009 VALUES LESS THAN (736246) ENGINE = InnoDB,
 PARTITION p20151010 VALUES LESS THAN (736247) ENGINE = InnoDB,
 PARTITION p20151011 VALUES LESS THAN (736248) ENGINE = InnoDB,
 PARTITION p20151012 VALUES LESS THAN (736249) ENGINE = InnoDB,
 PARTITION p20151013 VALUES LESS THAN (736250) ENGINE = InnoDB,
 PARTITION p20151014 VALUES LESS THAN (736251) ENGINE = InnoDB,
 PARTITION p20151015 VALUES LESS THAN (736252) ENGINE = InnoDB,
 PARTITION p20151016 VALUES LESS THAN (736253) ENGINE = InnoDB,
 PARTITION p20151017 VALUES LESS THAN (736254) ENGINE = InnoDB,
 PARTITION p20151018 VALUES LESS THAN (736255) ENGINE = InnoDB,
 PARTITION p20151019 VALUES LESS THAN (736256) ENGINE = InnoDB,
 PARTITION p20151020 VALUES LESS THAN (736257) ENGINE = InnoDB,
 PARTITION p20151021 VALUES LESS THAN (736258) ENGINE = InnoDB,
 PARTITION p20151022 VALUES LESS THAN (736259) ENGINE = InnoDB,
 PARTITION p20151023 VALUES LESS THAN (736260) ENGINE = InnoDB,
 PARTITION p20151024 VALUES LESS THAN (736261) ENGINE = InnoDB,
 PARTITION p20151025 VALUES LESS THAN (736262) ENGINE = InnoDB,
 PARTITION p20151026 VALUES LESS THAN (736263) ENGINE = InnoDB,
 PARTITION p20151027 VALUES LESS THAN (736264) ENGINE = InnoDB,
 PARTITION p20151028 VALUES LESS THAN (736265) ENGINE = InnoDB,
 PARTITION p20151029 VALUES LESS THAN (736266) ENGINE = InnoDB,
 PARTITION p20151030 VALUES LESS THAN (736267) ENGINE = InnoDB,
 PARTITION p20151031 VALUES LESS THAN (736268) ENGINE = InnoDB,
 PARTITION p20151101 VALUES LESS THAN (736269) ENGINE = InnoDB,
 PARTITION p20151102 VALUES LESS THAN (736270) ENGINE = InnoDB,
 PARTITION p20151103 VALUES LESS THAN (736271) ENGINE = InnoDB,
 PARTITION p20151104 VALUES LESS THAN (736272) ENGINE = InnoDB,
 PARTITION p20151105 VALUES LESS THAN (736273) ENGINE = InnoDB,
 PARTITION p20151106 VALUES LESS THAN (736274) ENGINE = InnoDB,
 PARTITION p20151107 VALUES LESS THAN (736275) ENGINE = InnoDB,
 PARTITION p20151108 VALUES LESS THAN (736276) ENGINE = InnoDB,
 PARTITION p20151109 VALUES LESS THAN (736277) ENGINE = InnoDB,
 PARTITION p20151110 VALUES LESS THAN (736278) ENGINE = InnoDB,
 PARTITION p20151111 VALUES LESS THAN (736279) ENGINE = InnoDB,
 PARTITION p20151112 VALUES LESS THAN (736280) ENGINE = InnoDB,
 PARTITION p20151113 VALUES LESS THAN (736281) ENGINE = InnoDB,
 PARTITION p20151114 VALUES LESS THAN (736282) ENGINE = InnoDB,
 PARTITION p20151115 VALUES LESS THAN (736283) ENGINE = InnoDB,
 PARTITION p20151116 VALUES LESS THAN (736284) ENGINE = InnoDB);


alter table irsf_detail add column irsf_number_range bigint(20) default 0;

SET FOREIGN_KEY_CHECKS = 1;

