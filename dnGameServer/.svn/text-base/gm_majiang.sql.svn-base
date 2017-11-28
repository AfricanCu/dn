-- MySQL dump 10.13  Distrib 5.5.37, for linux2.6 (x86_64)
--
-- Host: localhost    Database: gm_tools_system
-- ------------------------------------------------------
-- Server version	5.5.37-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gm_majiang` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gm_majiang`;
--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop` (
  `shopId` int(11) NOT NULL AUTO_INCREMENT,
  `rmb` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `pics` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `place` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  `kind` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `feture` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '特性',
  `startSeal` int(11) DEFAULT '0' COMMENT '起售',
  `maxSeal` int(11) DEFAULT '0' COMMENT '库存',
  `detail` text CHARACTER SET utf8 COLLATE utf8_bin,
  `evaluate` mediumtext CHARACTER SET utf8 COLLATE utf8_bin,
  `dbca` mediumtext CHARACTER SET utf8 COLLATE utf8_bin,
  `star` int(11) DEFAULT NULL COMMENT '星级',
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  PRIMARY KEY (`shopId`),
  KEY `name` (`name`(255))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'400.00','11.gif;12.gif;13.gif;14.gif;15.gif','金桂花','江苏 盐城 东台 新街镇 ','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">高度:300厘米 冠幅:250厘米 土球:40公分 </a><span style=\"color:#ff0000;\"><strong></strong></span>',10,5000,'常绿灌木或小乔木科，为温带树种，叶对生，多呈椭圆或长椭圆形，树叶叶面光滑，革质，叶边缘有锯齿。花簇生，花冠分裂至基乳有乳白、黄、橙红等色。桂花味辛，可入药。有化痰、止咳、生津、止牙痛等功效。 桂花味香，持久，可制糕点、糖果，并可<a target=\"_blank\" href=\"http://baike.baidu.com/item/%E9%85%BF%E9%85%92\">酿酒</a>。','554545dfas<u>d<span style=\"color:#33ff33;\">fasfasdfas</span></u>fadfasf','{}',0,'2017-03-31 00:00:00','2017-09-02 00:00:00'),(3,'1.00','31;32;33','丹桂','江苏 宿迁 沭阳 颜集镇','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">地径:3厘米</a>',100,10000,'<p>一：彩色苗木：美人梅、黄金槐、金叶高杆女贞、红天竹,红叶小檗、<strong><a href=\"http://www.huamu.com/fenlei/10886_jinyenvzhen.html\" target=\"_blank\">金叶女贞</a></strong>、洒金柏、紫叶女贞、金叶瓜子黄杨、 红花檵木、等。</p><p>二：常青苗木：雪松、白皮松、五针松、黑松、北京桧、蜀桧、西安桧、万峰桧、龙柏、侧柏、铺地柏、沙地柏、千头柏、绒柏、香柏、龙柏球、 花柏球、蜀桧球、大叶黄杨、小叶黄杨、雀舌黄杨、<strong><a href=\"http://www.huamu.com/fenlei/10683_dayenvzhen.html\" target=\"_blank\">大叶女贞</a></strong>、十大功劳、海桐、石楠、法国冬青、棕榈、剑麻等</p><p>三：落叶树类：国槐、垂槐、重柳、<strong><a href=\"http://www.huamu.com/fenlei/23945_jinsichuiliu.html\" target=\"_blank\">金丝垂柳</a></strong>、曲柳、直生柳、合欢、垂榆、意杨系列、马褂木、栾树、七叶树、喜树、乌桕、青桐、法梧、青枫、五角枫、无患子、银杏、水杉、火炬、臭椿、枸桔、流苏等。</p><p>四：花木类：紫荆、紫薇、连翘、迎春、黄馨、木槿、江南槐、樱花、红梅、绿梅、垂梅、银芽柳、贴梗海棠、垂丝海棠、麻叶绣球、广（红、白、紫、黄）玉兰、大花月季（二百种）丰花月季（北京红帽、黄帽子，曼姆等）、花（果）石榴、腊梅、桂花、榆叶梅、珍珠梅、棣棠、火棘、栀子花、金丝桃、锦带花、丁香、结香等。</p><p>五：藤蔓地被类：紫藤、常青藤、木香、蔷薇、扶芳藤、凌宵、<strong><a href=\"http://www.huamu.com/fenlei/11242_wuyedijin.html\" target=\"_blank\">五叶地锦</a></strong>等。另有各种盆景、盆花、美人蕉、金边麦冬、银边万年青、红花酢浆草、葱兰、玉簪、萱草、等绿化花卉苗木.</p><p>六：竹子类：青竹，淡竹，枣园竹，刚竹，金镶玉竹，黄竹，黄桃竹，罗汉竹，翠竹、紫竹等千亩欲买低价苗，请到基地来，我地货源充足、规格齐全、质优价廉。量大送货上门，帮助兴建苗圃</p><p>并提供技术指导，代办航空、铁路邮托运。彻底解决您的后顾之忧。欢迎各地客商前来我处实地看苗订货。来前请先电话联系，以便接送.我们的经营理念：诚信为本，公平实力竞争和优质服务</p><p><span style=\"color:#FF0000;\">同样的品种，给予您最优惠的价格15151181127</span></p>','554545','{}',0,'2017-06-13 00:00:00','2017-07-01 00:00:00'),(4,'15.00','41.gif;42.gif;43.gif','彩叶桂','安徽 阜阳 颍州 ','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">冠幅:30厘米 </a><br />',50,5000,'<p>彩叶桂是金桂的变异品种，是变异品种的一朵奇葩，作为<strong><a href=\"http://www.huamu.com/fenlei/10684_guihua.html\" target=\"_blank\">桂花</a></strong>的新品种其叶色一年四季变化多样， 精彩纷呈，初春嫩芽为紫红玫红，后转为桃红，进而转金黄色至白色。</p>','554545','{}',0,'2017-04-12 00:00:00','2017-07-21 00:00:00'),(5,'2800.00','51;52;53','12公分圆冠桂花','广东 揭阳 普宁 洪阳镇 ','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">米径:12厘米</a>',4,4000,'12公分桂花，圆冠。一级树形','554545','{}',0,'2017-04-30 00:00:00','2017-07-31 00:00:00'),(6,'19.50','61;62;63;64;65',' 桂花','江苏 宿迁','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">高度:200厘米 米径:2厘米 地径:3厘米 冠幅:50厘米</a>',8000,500000,'基地直销，<strong><a href=\"http://www.huamu.com/fenlei/10684_guihua.html\" target=\"_blank\">桂花</a></strong>，1-20公分，咨询电话号码18036408961',NULL,'{}',0,'2017-05-13 00:00:00','2017-06-27 00:00:00'),(7,'2.00','71.gif',' 桂花小苗','浏阳市','绿化苗木  常绿乔木 桂花','<a style=\"color:red;\">地径:1厘米 </a>',1000,100000,'高20厘米小苗，专业化培育，适合南方湿润地带生长，',NULL,'{}',0,'2017-06-13 00:00:00','2017-08-23 00:00:00');
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `belonguser` varchar(50) DEFAULT NULL,
  `menulist` text,
  `dbca` text,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123','MD5:18xp18xr18xt','123','{\"menulist\":[\"20000\",\"20001\",\"20002\",\"20003\",\"20004\",\"10000\",\"10001\",\"10002\",\"10003\",\"10004\",\"10005\",\"10006\",\"10007\"]}','{\"1\":0,\"4\":\"[{\\\"nextAdd\\\":\\\"222\\\",\\\"_cmbCity\\\":\\\"长沙市\\\",\\\"_cmbProvince\\\":\\\"湖南\\\",\\\"_cmbArea\\\":\\\"芙蓉区\\\"}]\"}'),('321','MD5:18xt18xr18xp','admin','{}','{}'),('admin','MD5:18xp18xr18xt',NULL,'{}','{\"1\":0}');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behave`
--

DROP TABLE IF EXISTS `user_behave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_behave` (
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `log` varchar(2048) NOT NULL COMMENT '日志记录',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '日志时间',
  `type` varchar(50) NOT NULL DEFAULT '0' COMMENT '日志类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behave`
--

LOCK TABLES `user_behave` WRITE;
/*!40000 ALTER TABLE `user_behave` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behave` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-20 23:06:40

DROP TABLE IF EXISTS `platform_statistics`;
CREATE TABLE `platform_statistics` (
  `dataTime` datetime NOT NULL COMMENT '数据时间',
  `dau` int(11) DEFAULT '0' COMMENT '日活跃',
  `wau` int(11) DEFAULT '0' COMMENT '周活跃',
  `mau` int(11) DEFAULT '0' COMMENT '月活跃',
  `total` int(11) DEFAULT '0' COMMENT '总用户数',
  `register` int(11) DEFAULT '0' COMMENT '新增玩家数',
  `credit` int(11) DEFAULT '0' COMMENT '充值金额',
  `sumcredit` int(11) DEFAULT '0' COMMENT '充值总额',
  PRIMARY KEY (`dataTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `adddiamond` */

DROP TABLE IF EXISTS `adddiamond`;

CREATE TABLE `adddiamond` (
  `proxy` varchar(32) NOT NULL COMMENT '代理ID',
  `uid` bigint(20) NOT NULL COMMENT '玩家ID',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `diamond` int(11) DEFAULT NULL COMMENT '加钻石数',
  `chargeTime` datetime DEFAULT NULL COMMENT '加钻时间',
  KEY `proxy` (`proxy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `minus_credits` */

DROP TABLE IF EXISTS `minus_credits`;

CREATE TABLE `minus_credits` (
  `username` varchar(50) DEFAULT NULL,
  `uid` varchar(50) DEFAULT NULL COMMENT '代理id',
  `credits` int(11) DEFAULT NULL COMMENT '积分变化',
  `log` tinytext,
  `operaTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `proxy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proxy` (
  `uid` varchar(32) NOT NULL,
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `invitation` int(11) DEFAULT '0' COMMENT '邀请码',
  `parent` int(11) DEFAULT '0' COMMENT '介绍人邀请码',
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `weChat` varchar(255) DEFAULT NULL COMMENT '微信',
  `bankId` int(11) DEFAULT '0' COMMENT '银行',
  `bankCard` varchar(255) DEFAULT NULL COMMENT '银行卡号',
  `sessionCode` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `head` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '玩家头像',
  `registerTime` datetime DEFAULT NULL COMMENT '注册时间',
  `loginTime` datetime DEFAULT NULL,
  `logoutTime` datetime DEFAULT NULL COMMENT '玩家登出时间',
  `diamond` int(11) DEFAULT '0' COMMENT '钻石数',
  `credits` int(11) DEFAULT '0' COMMENT '积分',
  `dbca` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*Table structure for table `proxy_charge` */

DROP TABLE IF EXISTS `proxy_charge`;

CREATE TABLE `proxy_charge` (
  `orderId` varchar(64) NOT NULL,
  `uid` varchar(32) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `platId` int(11) DEFAULT NULL,
  `diamond` int(11) NOT NULL DEFAULT '0',
  `money` varchar(255) DEFAULT NULL,
  `shopId` int(11) DEFAULT NULL,
  `chargeTime` datetime DEFAULT NULL,
  `orderTime` datetime DEFAULT NULL,
  PRIMARY KEY (`orderId`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
