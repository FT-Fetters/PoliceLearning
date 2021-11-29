
CREATE DATABASE `policelearning`;

USE `policelearning`;

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `introduce` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `catalogue` json DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

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
  `explaination` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `crime` varchar(255) DEFAULT NULL,
  `keyword` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Data for the table `law` */

insert  into `law`(`id`,`lawtype`,`title`,`content`,`explaination`,`crime`,`keyword`) values 
(1,'法律类型','《具体的法律》','条文内容','释义阐明','罪名精析','[{\"关键词1\": \"解释\"}, {\"关键词2\": \"解释\"}]'),
(2,'税务法','《个人所得税法》','条文内容','释义阐明','罪名精析','[{\"关键词1\": \"解释\"}, {\"关键词2\": \"解释\"}, {\"关键词3\": \"解释\"}]');

/*Table structure for table `lawtype` */

DROP TABLE IF EXISTS `lawtype`;

CREATE TABLE `lawtype` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `lawtype` varchar(20) NOT NULL,
  `title` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*Data for the table `lawtype` */

insert  into `lawtype`(`id`,`lawtype`,`title`) values 
(1,'税务法','[{\"name\": \"《个人所得税法》\"}, {\"name\": \"《企业所得税法》\"}]');