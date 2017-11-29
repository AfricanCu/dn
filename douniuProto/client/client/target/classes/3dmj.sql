/*
SQLyog Trial v10.51 
MySQL - 5.5.25 : Database - threemj
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`threemj` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `threemj`;

/*Table structure for table `battleback` */

DROP TABLE IF EXISTS `battleback`;

CREATE TABLE `battleback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14562 DEFAULT CHARSET=utf8;

/*Table structure for table `charge` */

DROP TABLE IF EXISTS `charge`;

CREATE TABLE `charge` (
  `orderId` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `username` bigint(20) DEFAULT NULL COMMENT '玩家唯一id',
  `diamond` int(11) DEFAULT NULL COMMENT '充值钻石数',
  `apple_receipt` varchar(256) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态0待处理1处理',
  `chargeTime` datetime DEFAULT NULL COMMENT '充值时间',
  `updateTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`orderId`),
  KEY `apple_receipt` (`apple_receipt`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `gamerecord` */

DROP TABLE IF EXISTS `gamerecord`;

CREATE TABLE `gamerecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1700 DEFAULT CHARSET=utf8;

/*Table structure for table `guild` */

DROP TABLE IF EXISTS `guild`;

CREATE TABLE `guild` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '公会ID',
  `name` varchar(256) DEFAULT NULL COMMENT '公会名称',
  `master` varchar(512) DEFAULT NULL COMMENT '会长信息',
  `notice` varchar(512) DEFAULT NULL COMMENT '公告',
  `playType` blob COMMENT '玩法',
  `registerTime` datetime NOT NULL COMMENT '注册时间',
  `serverId` int(11) DEFAULT NULL COMMENT '最近登录服务器id',
  `member` text COMMENT '成员数据',
  `apply` text COMMENT '申请数据',
  `gameRecord` blob COMMENT '游戏记录',
  `dbca` text COMMENT '扩展',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000485 DEFAULT CHARSET=utf8;

/*Table structure for table `room` */

DROP TABLE IF EXISTS `room`;

CREATE TABLE `room` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `serverId` int(11) DEFAULT NULL,
  `masterUid` bigint(20) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `back` blob COMMENT '房间备份数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `uid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '玩家ID',
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '昵称',
  `puid` varchar(128) NOT NULL COMMENT '平台提供的用户id',
  `deviceId` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '设备id',
  `platId` int(11) NOT NULL COMMENT '平台id',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `sessionCode` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '登陆游戏session',
  `head` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '玩家头像',
  `sex` int(11) DEFAULT '2' COMMENT '性别1男2女',
  `registerTime` datetime DEFAULT NULL COMMENT '注册时间',
  `loginTime` datetime DEFAULT NULL COMMENT '登陆时间',
  `logoutTime` datetime DEFAULT NULL COMMENT '玩家登出时间',
  `serverId` int(11) DEFAULT '0' COMMENT '玩家所在服务器id',
  `roomId` int(11) DEFAULT '0' COMMENT '玩家所在房间id',
  `myGuild` varchar(4048) DEFAULT NULL COMMENT '我的俱乐部列表',
  `joinGuild` varchar(4048) DEFAULT NULL COMMENT '加入的俱乐部列表',
  `diamond` tinytext COMMENT '钻石数',
  `expires_in` int(11) DEFAULT NULL COMMENT '封号状态',
  `accessTime` datetime DEFAULT NULL COMMENT '微信access时间',
  `refreshTime` datetime DEFAULT NULL COMMENT '微信登录刷新时间',
  `gameRecord` blob COMMENT '游戏记录',
  `dbca` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '扩展',
  `myAgency` int(11) DEFAULT '0' COMMENT '我的代理',
  `district` int(11) DEFAULT NULL COMMENT '区域',
  PRIMARY KEY (`uid`),
  KEY `puid` (`puid`)
) ENGINE=InnoDB AUTO_INCREMENT=11063 DEFAULT CHARSET=utf8;

/* Procedure structure for procedure `myproc` */

/*!50003 DROP PROCEDURE IF EXISTS  `myproc` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `myproc`()
begin 
declare num int; 
set num = 1; 
while num < 9999 do 
insert into room(id,serverId,masterUid) values(num,0,NULL); set num=num+1;
end while;
end */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
