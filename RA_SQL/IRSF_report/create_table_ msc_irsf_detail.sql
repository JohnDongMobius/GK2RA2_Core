/*
 Navicat Premium Data Transfer

 Source Server         : Maldives472_2 10.213.2.54 34722
 Source Server Type    : MySQL
 Source Server Version : 50508
 Source Host           : 10.213.2.54
 Source Database       : fraud_472_2

 Target Server Type    : MySQL
 Target Server Version : 50508
 File Encoding         : utf-8

 Date: 05/12/2016 17:46:03 PM
 
 private long id;
	private String trafficDate;
	private String trafficHour;
	private long sMsisdn;
	private long sImsi;
	private String tapcode;
	private int numberOfCalls;
	private long totalDuration;
	private int ruleId;
	
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ra_msc_irsf_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ra_msc_irsf_detail`;
CREATE TABLE `ra_msc_irsf_detail` (
  `traffic_date` date DEFAULT NULL,
  `call_time` datetime DEFAULT NULL,
  `s_msisdn` bigint(20) DEFAULT 0,
  `s_imsi` bigint(20) DEFAULT 0,
  `call_type` tinyint(2) DEFAULT 0,
  `o_msisdn` bigint(20) DEFAULT 0,
  `duration` bigint(20) DEFAULT 0,
  `rule_id` tinyint(2) DEFAULT 0
)
ENGINE=InnoDB AUTO_INCREMENT=2401112483 DEFAULT CHARSET=latin1
PARTITION BY RANGE (TO_DAYS(call_time))
(PARTITION p20160214 VALUES LESS THAN (736374) ENGINE = InnoDB,
 PARTITION p20160215 VALUES LESS THAN (736375) ENGINE = InnoDB,
 PARTITION p20160216 VALUES LESS THAN (736376) ENGINE = InnoDB,
 PARTITION p20160217 VALUES LESS THAN (736377) ENGINE = InnoDB,
 PARTITION p20160218 VALUES LESS THAN (736378) ENGINE = InnoDB,
 PARTITION p20160219 VALUES LESS THAN (736379) ENGINE = InnoDB,
 PARTITION p20160220 VALUES LESS THAN (736380) ENGINE = InnoDB,
 PARTITION p20160221 VALUES LESS THAN (736381) ENGINE = InnoDB,
 PARTITION p20160222 VALUES LESS THAN (736382) ENGINE = InnoDB,
 PARTITION p20160223 VALUES LESS THAN (736383) ENGINE = InnoDB,
 PARTITION p20160224 VALUES LESS THAN (736384) ENGINE = InnoDB,
 PARTITION p20160225 VALUES LESS THAN (736385) ENGINE = InnoDB,
 PARTITION p20160226 VALUES LESS THAN (736386) ENGINE = InnoDB,
 PARTITION p20160227 VALUES LESS THAN (736387) ENGINE = InnoDB,
 PARTITION p20160228 VALUES LESS THAN (736388) ENGINE = InnoDB,
 PARTITION p20160229 VALUES LESS THAN (736389) ENGINE = InnoDB,
 PARTITION p20160301 VALUES LESS THAN (736390) ENGINE = InnoDB,
 PARTITION p20160302 VALUES LESS THAN (736391) ENGINE = InnoDB,
 PARTITION p20160303 VALUES LESS THAN (736392) ENGINE = InnoDB,
 PARTITION p20160304 VALUES LESS THAN (736393) ENGINE = InnoDB,
 PARTITION p20160305 VALUES LESS THAN (736394) ENGINE = InnoDB,
 PARTITION p20160306 VALUES LESS THAN (736395) ENGINE = InnoDB,
 PARTITION p20160307 VALUES LESS THAN (736396) ENGINE = InnoDB,
 PARTITION p20160308 VALUES LESS THAN (736397) ENGINE = InnoDB,
 PARTITION p20160309 VALUES LESS THAN (736398) ENGINE = InnoDB,
 PARTITION p20160310 VALUES LESS THAN (736399) ENGINE = InnoDB,
 PARTITION p20160311 VALUES LESS THAN (736400) ENGINE = InnoDB,
 PARTITION p20160312 VALUES LESS THAN (736401) ENGINE = InnoDB,
 PARTITION p20160313 VALUES LESS THAN (736402) ENGINE = InnoDB,
 PARTITION p20160314 VALUES LESS THAN (736403) ENGINE = InnoDB,
 PARTITION p20160315 VALUES LESS THAN (736404) ENGINE = InnoDB,
 PARTITION p20160316 VALUES LESS THAN (736405) ENGINE = InnoDB,
 PARTITION p20160317 VALUES LESS THAN (736406) ENGINE = InnoDB,
 PARTITION p20160318 VALUES LESS THAN (736407) ENGINE = InnoDB,
 PARTITION p20160319 VALUES LESS THAN (736408) ENGINE = InnoDB,
 PARTITION p20160320 VALUES LESS THAN (736409) ENGINE = InnoDB,
 PARTITION p20160321 VALUES LESS THAN (736410) ENGINE = InnoDB,
 PARTITION p20160322 VALUES LESS THAN (736411) ENGINE = InnoDB,
 PARTITION p20160323 VALUES LESS THAN (736412) ENGINE = InnoDB,
 PARTITION p20160324 VALUES LESS THAN (736413) ENGINE = InnoDB,
 PARTITION p20160325 VALUES LESS THAN (736414) ENGINE = InnoDB,
 PARTITION p20160326 VALUES LESS THAN (736415) ENGINE = InnoDB,
 PARTITION p20160327 VALUES LESS THAN (736416) ENGINE = InnoDB,
 PARTITION p20160328 VALUES LESS THAN (736417) ENGINE = InnoDB,
 PARTITION p20160329 VALUES LESS THAN (736418) ENGINE = InnoDB,
 PARTITION p20160330 VALUES LESS THAN (736419) ENGINE = InnoDB,
 PARTITION p20160331 VALUES LESS THAN (736420) ENGINE = InnoDB,
 PARTITION p20160401 VALUES LESS THAN (736421) ENGINE = InnoDB,
 PARTITION p20160402 VALUES LESS THAN (736422) ENGINE = InnoDB,
 PARTITION p20160403 VALUES LESS THAN (736423) ENGINE = InnoDB,
 PARTITION p20160404 VALUES LESS THAN (736424) ENGINE = InnoDB,
 PARTITION p20160405 VALUES LESS THAN (736425) ENGINE = InnoDB,
 PARTITION p20160406 VALUES LESS THAN (736426) ENGINE = InnoDB,
 PARTITION p20160407 VALUES LESS THAN (736427) ENGINE = InnoDB,
 PARTITION p20160408 VALUES LESS THAN (736428) ENGINE = InnoDB,
 PARTITION p20160409 VALUES LESS THAN (736429) ENGINE = InnoDB,
 PARTITION p20160410 VALUES LESS THAN (736430) ENGINE = InnoDB,
 PARTITION p20160411 VALUES LESS THAN (736431) ENGINE = InnoDB,
 PARTITION p20160412 VALUES LESS THAN (736432) ENGINE = InnoDB,
 PARTITION p20160413 VALUES LESS THAN (736433) ENGINE = InnoDB,
 PARTITION p20160414 VALUES LESS THAN (736434) ENGINE = InnoDB,
 PARTITION p20160415 VALUES LESS THAN (736435) ENGINE = InnoDB,
 PARTITION p20160416 VALUES LESS THAN (736436) ENGINE = InnoDB,
 PARTITION p20160417 VALUES LESS THAN (736437) ENGINE = InnoDB,
 PARTITION p20160418 VALUES LESS THAN (736438) ENGINE = InnoDB,
 PARTITION p20160419 VALUES LESS THAN (736439) ENGINE = InnoDB,
 PARTITION p20160420 VALUES LESS THAN (736440) ENGINE = InnoDB,
 PARTITION p20160421 VALUES LESS THAN (736441) ENGINE = InnoDB,
 PARTITION p20160422 VALUES LESS THAN (736442) ENGINE = InnoDB,
 PARTITION p20160423 VALUES LESS THAN (736443) ENGINE = InnoDB,
 PARTITION p20160424 VALUES LESS THAN (736444) ENGINE = InnoDB,
 PARTITION p20160425 VALUES LESS THAN (736445) ENGINE = InnoDB,
 PARTITION p20160426 VALUES LESS THAN (736446) ENGINE = InnoDB,
 PARTITION p20160427 VALUES LESS THAN (736447) ENGINE = InnoDB,
 PARTITION p20160428 VALUES LESS THAN (736448) ENGINE = InnoDB,
 PARTITION p20160429 VALUES LESS THAN (736449) ENGINE = InnoDB,
 PARTITION p20160430 VALUES LESS THAN (736450) ENGINE = InnoDB,
 PARTITION p20160501 VALUES LESS THAN (736451) ENGINE = InnoDB,
 PARTITION p20160502 VALUES LESS THAN (736452) ENGINE = InnoDB,
 PARTITION p20160503 VALUES LESS THAN (736453) ENGINE = InnoDB,
 PARTITION p20160504 VALUES LESS THAN (736454) ENGINE = InnoDB,
 PARTITION p20160505 VALUES LESS THAN (736455) ENGINE = InnoDB,
 PARTITION p20160506 VALUES LESS THAN (736456) ENGINE = InnoDB,
 PARTITION p20160507 VALUES LESS THAN (736457) ENGINE = InnoDB,
 PARTITION p20160508 VALUES LESS THAN (736458) ENGINE = InnoDB,
 PARTITION p20160509 VALUES LESS THAN (736459) ENGINE = InnoDB,
 PARTITION p20160510 VALUES LESS THAN (736460) ENGINE = InnoDB,
 PARTITION p20160511 VALUES LESS THAN (736461) ENGINE = InnoDB,
 PARTITION p20160512 VALUES LESS THAN (736462) ENGINE = InnoDB,
 PARTITION p20160513 VALUES LESS THAN (736463) ENGINE = InnoDB,
 PARTITION p20160514 VALUES LESS THAN (736464) ENGINE = InnoDB,
 PARTITION p20160515 VALUES LESS THAN (736465) ENGINE = InnoDB,
 PARTITION p20160516 VALUES LESS THAN (736466) ENGINE = InnoDB,
 PARTITION p20160517 VALUES LESS THAN (736467) ENGINE = InnoDB,
 PARTITION p20160518 VALUES LESS THAN (736468) ENGINE = InnoDB,
 PARTITION p20160519 VALUES LESS THAN (736469) ENGINE = InnoDB,
 PARTITION p20160520 VALUES LESS THAN (736470) ENGINE = InnoDB,
 PARTITION p20160521 VALUES LESS THAN (736471) ENGINE = InnoDB,
 PARTITION p20160522 VALUES LESS THAN (736472) ENGINE = InnoDB,
 PARTITION p20160523 VALUES LESS THAN (736473) ENGINE = InnoDB,
 PARTITION p20160524 VALUES LESS THAN (736474) ENGINE = InnoDB,
 PARTITION p20160525 VALUES LESS THAN (736475) ENGINE = InnoDB,
 PARTITION p20160526 VALUES LESS THAN (736476) ENGINE = InnoDB,
 PARTITION p20160527 VALUES LESS THAN (736477) ENGINE = InnoDB,
 PARTITION p20160528 VALUES LESS THAN (736478) ENGINE = InnoDB,
 PARTITION p20160529 VALUES LESS THAN (736479) ENGINE = InnoDB,
 PARTITION p20160530 VALUES LESS THAN (736480) ENGINE = InnoDB,
 PARTITION p20160531 VALUES LESS THAN (736481) ENGINE = InnoDB,
 PARTITION p20160601 VALUES LESS THAN (736482) ENGINE = InnoDB,
 PARTITION p20160602 VALUES LESS THAN (736483) ENGINE = InnoDB,
 PARTITION p20160603 VALUES LESS THAN (736484) ENGINE = InnoDB,
 PARTITION p20160604 VALUES LESS THAN (736485) ENGINE = InnoDB,
 PARTITION p20160605 VALUES LESS THAN (736486) ENGINE = InnoDB,
 PARTITION p20160606 VALUES LESS THAN (736487) ENGINE = InnoDB,
 PARTITION p20160607 VALUES LESS THAN (736488) ENGINE = InnoDB,
 PARTITION p20160608 VALUES LESS THAN (736489) ENGINE = InnoDB,
 PARTITION p20160609 VALUES LESS THAN (736490) ENGINE = InnoDB,
 PARTITION p20160610 VALUES LESS THAN (736491) ENGINE = InnoDB,
 PARTITION p20160611 VALUES LESS THAN (736492) ENGINE = InnoDB,
 PARTITION p20160612 VALUES LESS THAN (736493) ENGINE = InnoDB,
 PARTITION p20160613 VALUES LESS THAN (736494) ENGINE = InnoDB,
 PARTITION p20160614 VALUES LESS THAN (736495) ENGINE = InnoDB,
 PARTITION p20160615 VALUES LESS THAN (736496) ENGINE = InnoDB,
 PARTITION p20160616 VALUES LESS THAN (736497) ENGINE = InnoDB,
 PARTITION p20160617 VALUES LESS THAN (736498) ENGINE = InnoDB,
 PARTITION p20160618 VALUES LESS THAN (736499) ENGINE = InnoDB,
 PARTITION p20160619 VALUES LESS THAN (736500) ENGINE = InnoDB,
 PARTITION p20160620 VALUES LESS THAN (736501) ENGINE = InnoDB,
 PARTITION p20160621 VALUES LESS THAN (736502) ENGINE = InnoDB,
 PARTITION p20160622 VALUES LESS THAN (736503) ENGINE = InnoDB,
 PARTITION p20160623 VALUES LESS THAN (736504) ENGINE = InnoDB,
 PARTITION p20160624 VALUES LESS THAN (736505) ENGINE = InnoDB,
 PARTITION p20160625 VALUES LESS THAN (736506) ENGINE = InnoDB,
 PARTITION p20160626 VALUES LESS THAN (736507) ENGINE = InnoDB,
 PARTITION p20160627 VALUES LESS THAN (736508) ENGINE = InnoDB,
 PARTITION p20160628 VALUES LESS THAN (736509) ENGINE = InnoDB,
 PARTITION p20160629 VALUES LESS THAN (736510) ENGINE = InnoDB,
 PARTITION p20160630 VALUES LESS THAN (736511) ENGINE = InnoDB,
 PARTITION p20160701 VALUES LESS THAN (736512) ENGINE = InnoDB,
 PARTITION p20160702 VALUES LESS THAN (736513) ENGINE = InnoDB,
 PARTITION p20160703 VALUES LESS THAN (736514) ENGINE = InnoDB,
 PARTITION p20160704 VALUES LESS THAN (736515) ENGINE = InnoDB,
 PARTITION p20160705 VALUES LESS THAN (736516) ENGINE = InnoDB,
 PARTITION p20160706 VALUES LESS THAN (736517) ENGINE = InnoDB,
 PARTITION p20160707 VALUES LESS THAN (736518) ENGINE = InnoDB,
 PARTITION p20160708 VALUES LESS THAN (736519) ENGINE = InnoDB,
 PARTITION p20160709 VALUES LESS THAN (736520) ENGINE = InnoDB,
 PARTITION p20160710 VALUES LESS THAN (736521) ENGINE = InnoDB,
 PARTITION p20160711 VALUES LESS THAN (736522) ENGINE = InnoDB,
 PARTITION p20160712 VALUES LESS THAN (736523) ENGINE = InnoDB,
 PARTITION p20160713 VALUES LESS THAN (736524) ENGINE = InnoDB,
 PARTITION p20160714 VALUES LESS THAN (736525) ENGINE = InnoDB,
 PARTITION p20160715 VALUES LESS THAN (736526) ENGINE = InnoDB,
 PARTITION p20160716 VALUES LESS THAN (736527) ENGINE = InnoDB,
 PARTITION p20160717 VALUES LESS THAN (736528) ENGINE = InnoDB,
 PARTITION p20160718 VALUES LESS THAN (736529) ENGINE = InnoDB,
 PARTITION p20160719 VALUES LESS THAN (736530) ENGINE = InnoDB,
 PARTITION p20160720 VALUES LESS THAN (736531) ENGINE = InnoDB,
 PARTITION p20160721 VALUES LESS THAN (736532) ENGINE = InnoDB,
 PARTITION p20160722 VALUES LESS THAN (736533) ENGINE = InnoDB,
 PARTITION p20160723 VALUES LESS THAN (736534) ENGINE = InnoDB,
 PARTITION p20160724 VALUES LESS THAN (736535) ENGINE = InnoDB,
 PARTITION p20160725 VALUES LESS THAN (736536) ENGINE = InnoDB,
 PARTITION p20160726 VALUES LESS THAN (736537) ENGINE = InnoDB,
 PARTITION p20160727 VALUES LESS THAN (736538) ENGINE = InnoDB,
 PARTITION p20160728 VALUES LESS THAN (736539) ENGINE = InnoDB,
 PARTITION p20160729 VALUES LESS THAN (736540) ENGINE = InnoDB,
 PARTITION p20160730 VALUES LESS THAN (736541) ENGINE = InnoDB,
 PARTITION p20160731 VALUES LESS THAN (736542) ENGINE = InnoDB,
 PARTITION p20160801 VALUES LESS THAN (736543) ENGINE = InnoDB,
 PARTITION p20160802 VALUES LESS THAN (736544) ENGINE = InnoDB,
 PARTITION p20160803 VALUES LESS THAN (736545) ENGINE = InnoDB,
 PARTITION p20160804 VALUES LESS THAN (736546) ENGINE = InnoDB,
 PARTITION p20160805 VALUES LESS THAN (736547) ENGINE = InnoDB,
 PARTITION p20160806 VALUES LESS THAN (736548) ENGINE = InnoDB,
 PARTITION p20160807 VALUES LESS THAN (736549) ENGINE = InnoDB,
 PARTITION p20160808 VALUES LESS THAN (736550) ENGINE = InnoDB,
 PARTITION p20160809 VALUES LESS THAN (736551) ENGINE = InnoDB,
 PARTITION p20160810 VALUES LESS THAN (736552) ENGINE = InnoDB,
 PARTITION p20160811 VALUES LESS THAN (736553) ENGINE = InnoDB);
 