#PRODUCT ID,SUB-PRODUCT ID,SERVICE CODE A,SERVICE CODE B,,

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `product_id`
-- ----------------------------
DROP TABLE IF EXISTS `product_id`;
CREATE TABLE `product_id` (
	`id` int(15) NOT NULL AUTO_INCREMENT,
  `product_ID` varchar(30) DEFAULT "",
  `sub_product_ID` varchar(50) DEFAULT "",
  `s_num_code` varchar(50) DEFAULT "",
  `o_num_code` varchar(50) DEFAULT "",
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_s_num_code` (`s_num_code`),
  KEY `idx_o_num_code` (`o_num_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `rate_plan`
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

#load data infile "/Users/anmyandy/Workspaces/Workspace4Operators/Niger/doc/Niger_Retail%20Revenue%20Report%20Config%20Doc%20V0.5-3_productId.csv" into table product_id fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set product_ID=@col1,sub_product_ID=@col2,s_num_code=@col3,o_num_code=@col4;

load data infile "/tmp/Niger_Retail%20Revenue%20Report%20Config%20Doc%20V0.5-3_productId.csv" into table product_id fields terminated by ',' enclosed by '"' lines terminated by '\r' (@col1,@col2,@col3,@col4) set product_ID=@col1,sub_product_ID=@col2,s_num_code=@col3,o_num_code=@col4;

#set rate_plan.product_id when it's blank.
update rate_plan as a, product_id as b set a.product_id=b.product_id where a.rate_plan_code=b.sub_product_ID and a.s_num_code=b.s_num_code and a.o_num_code=b.o_num_code;
update rate_plan as a, product_id as b set a.product_id=b.product_id where a.rate_plan_code=b.sub_product_ID and a.product_id='';
update rate_plan set product_id='INTERNATIONAL' where product_id='' and rate_plan_code like '%INT%';
update rate_plan set product_id='NIGER_SHORT_NUMBER' where product_id='' and rate_plan_code like '%FREEPHONE';
update rate_plan set product_id='NIGER_SHORT_NUMBER' where product_id='' and rate_plan_code like '%UNIVERSAL%';
update rate_plan set product_id='NIGER_SHORT_NUMBER' where product_id='' and rate_plan_code like '%FAX%';


#drop this table after update.
DROP TABLE IF EXISTS `product_id`;

#verify result
select count(*) from rate_plan where product_id='';
select * from rate_plan limit 1;
