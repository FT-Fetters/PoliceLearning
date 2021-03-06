/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.24 : Database - policelearning
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`policelearning` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `policelearning`;

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `introduce` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `catalogue` text,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `course` */

insert  into `course`(`id`,`name`,`introduce`,`catalogue`,`type`) values 
(1,'测试课程','用于测试的课程','[{\"name\": \"第一课\"}, {\"name\": \"第二课\"}]','修改类型测试'),
(2,'宪法','宪法介绍视频','[{\"name\": \"第一课\"}, {\"name\": \"第二课\"}]','法律'),
(3,'交通法','交通法介绍视频','[{\"name\": \"第一课\"}, {\"name\": \"第二课\"}]','法律'),
(4,'警宣','警察宣传视频','[{\"name\": \"第一课\"}, {\"name\": \"第二课\"}]','宣传');

/*Table structure for table `information` */

DROP TABLE IF EXISTS `information`;

CREATE TABLE `information` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `content` text NOT NULL,
  `other` char(20) NOT NULL,
  `views` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `information` */

insert  into `information`(`id`,`title`,`date`,`content`,`other`,`views`) values 
(1,'测试','2021-11-20','测试','测试',0000000000);

/*Table structure for table `law` */

DROP TABLE IF EXISTS `law`;

CREATE TABLE `law` (
  `id` int NOT NULL,
  `lawtype` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `explaination` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `crime` varchar(255) DEFAULT NULL,
  `keyword` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `law` */

insert  into `law`(`id`,`lawtype`,`title`,`content`,`explaination`,`crime`,`keyword`) values 
(1,'法律类型','《具体的法律》','条文内容','释义阐明','罪名精析','[{\"关键词1\": \"解释\"}, {\"关键词2\": \"解释\"}]'),
(2,'税务法','《个人所得税法》','条文内容','释义阐明','罪名精析','[{\"关键词1\": \"解释\"}, {\"关键词2\": \"解释\"}, {\"关键词3\": \"解释\"}]');

/*Table structure for table `lawtype` */

DROP TABLE IF EXISTS `lawtype`;

CREATE TABLE `lawtype` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `lawtype` varchar(20) NOT NULL,
  `title` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `lawtype` */

insert  into `lawtype`(`id`,`lawtype`,`title`) values 
(1,'税务法','[{\"name\": \"《个人所得税法》\"}, {\"name\": \"《企业所得税法》\"}]');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
