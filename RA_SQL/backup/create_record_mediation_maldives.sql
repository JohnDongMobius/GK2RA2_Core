CREATE TABLE `record_mediation_maldives` (
  `Service_Key` int(1) DEFAULT NULL,
  `Subscriber_Type` char(2) DEFAULT NULL,
  `Call_Type` char(1) DEFAULT NULL,
  `Charge_Type` char(1) DEFAULT NULL,
  `Roam_Flag` char(1) DEFAULT NULL,
  `Calling_Party_Number` char(20) DEFAULT NULL,
  `Called_Party_Number` char(20) DEFAULT NULL,
  `Roam_Area_Number` char(8) DEFAULT NULL,
  `Call_Begin_Time` datetime DEFAULT NULL,
  `Call_Duration` int(11) DEFAULT NULL,
  `Normal_fee` int(11) DEFAULT NULL,
  `Charge_Party_Indicator` char(1) DEFAULT NULL,
  `Calling_V_Area_Number` char(8) DEFAULT NULL,
  `Called_V_Area_Number` char(8) DEFAULT NULL,
  `Termination_Reason` int(1) DEFAULT NULL,
  `Normal_account_balance_after_call` int(11) DEFAULT NULL,
  `Service_Type` char(1) DEFAULT NULL,
  `Original_Called_Party` char(20) DEFAULT NULL,
  `Discount` int(11) DEFAULT NULL,
  `Promoted_fee_spent_in_calling` int(11) DEFAULT NULL,
  `Promoted_balance_after_call` int(11) DEFAULT NULL,
  `Sub_MSISDN` char(32) DEFAULT NULL,
  `Brand_ID` int(11) DEFAULT NULL,
  `CAC` char(11) DEFAULT NULL,
  `Payment` char(1) DEFAULT NULL,
  `Free_Chg_Time` int(11) DEFAULT NULL,
  `Grp_No` int(11) DEFAULT NULL,
  `Calling_Cell_ID` char(20) DEFAULT NULL,
  `Called_Cell_ID` char(20) DEFAULT NULL,
  `Call_Reference_Number` char(16) DEFAULT NULL,
  `Rec_Flag` char(16) DEFAULT NULL,
  `Airtime_Fee` char(11) DEFAULT NULL,
  `LDAccess_Fee` int(11) DEFAULT NULL,
  `Inter_Con_Fee` int(11) DEFAULT NULL,
  `Toll_Fee` int(11) DEFAULT NULL,
  `Flat_Fee` int(11) DEFAULT NULL,
  `Video_Minute_Used` int(11) DEFAULT NULL,
  `Free_Chg_Time2` char(11) DEFAULT NULL,
  `Free_Chg_Time3` char(11) DEFAULT NULL,
  `Reserve3` char(20) DEFAULT NULL,
  `Wait_Duration` int(11) DEFAULT NULL,
  `Bearer_Capability` int(11) DEFAULT NULL,
  `Reserve01` char(11) DEFAULT NULL,
  `Reserve02` char(11) DEFAULT NULL,
  `Reserve03` char(11) DEFAULT NULL,
  `Reserve04` char(11) DEFAULT NULL,
  `Reserve05` char(11) DEFAULT NULL,
  `Reserve06` char(11) DEFAULT NULL,
  `Reserve07` char(11) DEFAULT NULL,
  `Reserve08` char(11) DEFAULT NULL,
  `Reserve09` char(11) DEFAULT NULL,
  `Reserve10` char(11) DEFAULT NULL,
  `Reserve11` char(11) DEFAULT NULL,
  `Reserve12` char(11) DEFAULT NULL,
  `Reserve13` char(11) DEFAULT NULL,
  `Reserve14` char(11) DEFAULT NULL,
  `Reserve15` char(11) DEFAULT NULL,
  `Reserve16` char(11) DEFAULT NULL,
  `Reserve17` char(11) DEFAULT NULL,
  `Reserve18` char(11) DEFAULT NULL,
  `Reserve19` char(11) DEFAULT NULL,
  `Reserve20` char(11) DEFAULT NULL,
  KEY `Calling_Party_Number` (`Calling_Party_Number`),
  KEY `Called_Party_Number` (`Called_Party_Number`),
  KEY `Call_Begin_Time` (`Call_Begin_Time`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1
PARTITION BY RANGE  (TO_DAYS(Call_Begin_Time))
(
 PARTITION p20110928 VALUES LESS THAN (734774) ENGINE = InnoDB,
 PARTITION p20110929 VALUES LESS THAN (734775) ENGINE = InnoDB,
 PARTITION p20110930 VALUES LESS THAN (734776) ENGINE = InnoDB,
 PARTITION p20111001 VALUES LESS THAN (734777) ENGINE = InnoDB,
 PARTITION p20111002 VALUES LESS THAN (734778) ENGINE = InnoDB,
 PARTITION p20111003 VALUES LESS THAN (734779) ENGINE = InnoDB,
 PARTITION p20111004 VALUES LESS THAN (734780) ENGINE = InnoDB,
 PARTITION p20111005 VALUES LESS THAN (734781) ENGINE = InnoDB,
 PARTITION p20111006 VALUES LESS THAN (734782) ENGINE = InnoDB,
 PARTITION p20111007 VALUES LESS THAN (734783) ENGINE = InnoDB,
 PARTITION p20111008 VALUES LESS THAN (734784) ENGINE = InnoDB,
 PARTITION p20111009 VALUES LESS THAN (734785) ENGINE = InnoDB,
 PARTITION p20111010 VALUES LESS THAN (734786) ENGINE = InnoDB,
 PARTITION p20111011 VALUES LESS THAN (734787) ENGINE = InnoDB,
 PARTITION p20111012 VALUES LESS THAN (734788) ENGINE = InnoDB,
 PARTITION p20111013 VALUES LESS THAN (734789) ENGINE = InnoDB,
 PARTITION p20111014 VALUES LESS THAN (734790) ENGINE = InnoDB,
 PARTITION p20111015 VALUES LESS THAN (734791) ENGINE = InnoDB,
 PARTITION p20111016 VALUES LESS THAN (734792) ENGINE = InnoDB,
 PARTITION p20111017 VALUES LESS THAN (734793) ENGINE = InnoDB,
 PARTITION p20111018 VALUES LESS THAN (734794) ENGINE = InnoDB,
 PARTITION p20111019 VALUES LESS THAN (734795) ENGINE = InnoDB,
 PARTITION p20111020 VALUES LESS THAN (734796) ENGINE = InnoDB,
 PARTITION p20111021 VALUES LESS THAN (734797) ENGINE = InnoDB,
 PARTITION p20111022 VALUES LESS THAN (734798) ENGINE = InnoDB,
 PARTITION p20111023 VALUES LESS THAN (734799) ENGINE = InnoDB,
 PARTITION p20111024 VALUES LESS THAN (734800) ENGINE = InnoDB,
 PARTITION p20111025 VALUES LESS THAN (734801) ENGINE = InnoDB,
 PARTITION p20111026 VALUES LESS THAN (734802) ENGINE = InnoDB,
 PARTITION p20111027 VALUES LESS THAN (734803) ENGINE = InnoDB,
 PARTITION p20111028 VALUES LESS THAN (734804) ENGINE = InnoDB,
 PARTITION p20111029 VALUES LESS THAN (734805) ENGINE = InnoDB,
 PARTITION p20111030 VALUES LESS THAN (734806) ENGINE = InnoDB,
 PARTITION p20111031 VALUES LESS THAN (734807) ENGINE = InnoDB,
 PARTITION p20111101 VALUES LESS THAN (734808) ENGINE = InnoDB,
 PARTITION p20111102 VALUES LESS THAN (734809) ENGINE = InnoDB,
 PARTITION p20111103 VALUES LESS THAN (734810) ENGINE = InnoDB,
 PARTITION p20111104 VALUES LESS THAN (734811) ENGINE = InnoDB,
 PARTITION p20111105 VALUES LESS THAN (734812) ENGINE = InnoDB,
 PARTITION p20111106 VALUES LESS THAN (734813) ENGINE = InnoDB,
 PARTITION p20111107 VALUES LESS THAN (734814) ENGINE = InnoDB,
 PARTITION p20111108 VALUES LESS THAN (734815) ENGINE = InnoDB,
 PARTITION p20111109 VALUES LESS THAN (734816) ENGINE = InnoDB,
 PARTITION p20111110 VALUES LESS THAN (734817) ENGINE = InnoDB,
 PARTITION p20111111 VALUES LESS THAN (734818) ENGINE = InnoDB,
 PARTITION p20111112 VALUES LESS THAN (734819) ENGINE = InnoDB,
 PARTITION p20111113 VALUES LESS THAN (734820) ENGINE = InnoDB,
 PARTITION p20111114 VALUES LESS THAN (734821) ENGINE = InnoDB,
 PARTITION p20111115 VALUES LESS THAN (734822) ENGINE = InnoDB,
 PARTITION p20111116 VALUES LESS THAN (734823) ENGINE = InnoDB,
 PARTITION p20111117 VALUES LESS THAN (734824) ENGINE = InnoDB,
 PARTITION p20111118 VALUES LESS THAN (734825) ENGINE = InnoDB,
 PARTITION p20111119 VALUES LESS THAN (734826) ENGINE = InnoDB,
 PARTITION p20111120 VALUES LESS THAN (734827) ENGINE = InnoDB,
 PARTITION p20111121 VALUES LESS THAN (734828) ENGINE = InnoDB,
 PARTITION p20111122 VALUES LESS THAN (734829) ENGINE = InnoDB,
 PARTITION p20111123 VALUES LESS THAN (734830) ENGINE = InnoDB,
 PARTITION p20111124 VALUES LESS THAN (734831) ENGINE = InnoDB,
 PARTITION p20111125 VALUES LESS THAN (734832) ENGINE = InnoDB,
 PARTITION p20111126 VALUES LESS THAN (734833) ENGINE = InnoDB,
 PARTITION p20111127 VALUES LESS THAN (734834) ENGINE = InnoDB,
 PARTITION p20111128 VALUES LESS THAN (734835) ENGINE = InnoDB,
 PARTITION p20111129 VALUES LESS THAN (734836) ENGINE = InnoDB,
 PARTITION p20111130 VALUES LESS THAN (734837) ENGINE = InnoDB,
 PARTITION p20111201 VALUES LESS THAN (734838) ENGINE = InnoDB,
 PARTITION p20111202 VALUES LESS THAN (734839) ENGINE = InnoDB,
 PARTITION p20111203 VALUES LESS THAN (734840) ENGINE = InnoDB,
 PARTITION p20111204 VALUES LESS THAN (734841) ENGINE = InnoDB,
 PARTITION p20111205 VALUES LESS THAN (734842) ENGINE = InnoDB,
 PARTITION p20111206 VALUES LESS THAN (734843) ENGINE = InnoDB,
 PARTITION p20111207 VALUES LESS THAN (734844) ENGINE = InnoDB,
 PARTITION p20111208 VALUES LESS THAN (734845) ENGINE = InnoDB,
 PARTITION p20111209 VALUES LESS THAN (734846) ENGINE = InnoDB,
 PARTITION p20111210 VALUES LESS THAN (734847) ENGINE = InnoDB,
 PARTITION p20111211 VALUES LESS THAN (734848) ENGINE = InnoDB,
 PARTITION p20111212 VALUES LESS THAN (734849) ENGINE = InnoDB,
 PARTITION p20111213 VALUES LESS THAN (734850) ENGINE = InnoDB,
 PARTITION p20111214 VALUES LESS THAN (734851) ENGINE = InnoDB,
 PARTITION p20111215 VALUES LESS THAN (734852) ENGINE = InnoDB,
 PARTITION p20111216 VALUES LESS THAN (734853) ENGINE = InnoDB,
 PARTITION p20111217 VALUES LESS THAN (734854) ENGINE = InnoDB,
 PARTITION p20111218 VALUES LESS THAN (734855) ENGINE = InnoDB,
 PARTITION p20111219 VALUES LESS THAN (734856) ENGINE = InnoDB,
 PARTITION p20111220 VALUES LESS THAN (734857) ENGINE = InnoDB,
 PARTITION p20111221 VALUES LESS THAN (734858) ENGINE = InnoDB,
 PARTITION p20111222 VALUES LESS THAN (734859) ENGINE = InnoDB,
 PARTITION p20111223 VALUES LESS THAN (734860) ENGINE = InnoDB
);
