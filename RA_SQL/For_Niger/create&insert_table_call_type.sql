drop table if exists  call_type;
CREATE TABLE `call_type` (
  `call_type_id` tinyint(6) unsigned NOT NULL,
  `call_type_value` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`call_type_id`),
  KEY `Index_call_type_1` (`call_type_id`),
  KEY `Index_call_type_2` (`call_type_value`)
) ENGINE=MyISAM;

LOCK TABLES `call_type` WRITE;
/*!40000 ALTER TABLE `call_type` DISABLE KEYS */;
INSERT INTO `call_type` VALUES (0,'default'),(1,'moc'),(2,'mtc'),(3,'emergenc'),(6,'mocOACSU'),(8,'inCallMo'),(9,'inCallMo'),(10,'sSRegist'),(11,'sSErasur'),(12,'sSActiva'),(13,'sSDeacti'),(14,'sSInterr'),(15,'sSUnstru'),(19,'moDPAD'),(26,'roaming'),(27,'transit'),(29,'callForw'),(30,'mtcSMS'),(31,'mocSMS'),(32,'emergenc'),(33,'sSInvoca'),(34,'roaAttem'),(35,'mtLocati'),(36,'mtLocati'),(37,'moLocati'),(38,'moLocati'),(39,'niLocati'),(40,'niLocati'),(43,'tIR'),(44,'voiceGro'),(45,'tIRAttem'),(46,'voiceGro'),(59,'processU'),(60,'unstruct'),(61,'unstruct'),(65,'mocAttem'),(66,'mtcAttem'),(67,'emyAttem'),(93,'cfAttemp'),(127,'sUBOG'),(126,'sUBIC'),(110,'pBXIC'),(146,'146'),(145,'145');

INSERT INTO `call_type` VALUES (101, 'MOC On-net'),(102, 'MOC Off-net'),(103,'MOC International'),(104,'MOC SGSN'),(105,'MOC GGSN'),(201,'MTC On-net'),(202,'MTC Off-net'),(203,'MTC International'),(161,'Roaming Inbound'),(162,'Roaming Outbound'),(111,'MOC SMS On-net'),(112,'MOC SMS Off-net'),(113,'MOC SMS International'),(211,'MTC SMS On-net'),(212,'MTC SMS Off-net'),(213,'MTC SMS International'),(84,'Premium Rate Services');

/*!40000 ALTER TABLE `call_type` ENABLE KEYS */;
UNLOCK TABLES;

