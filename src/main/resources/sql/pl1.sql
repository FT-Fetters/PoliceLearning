/*
SQLyog Enterprise v13.1.1 (64 bit)
MySQL - 8.0.23-commercial : Database - policelearning
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`policelearning` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `policelearning`;

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `introduce` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `catalogue` json DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `view` int DEFAULT NULL,
  `istop` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `information` */

insert  into `information`(`id`,`title`,`date`,`content`,`view`,`istop`) values 
(1,'资讯的标题','2021-11-20','资讯的内容',0,NULL),
(2,'河南灾后3个月：粮食减产 房子要修 因疫情无法外出打工','2021-12-03','郭国勤50岁出头，穿着一件西装外套，戴一副白手套，足蹬运动鞋，小跑着推着一辆红色手推车，在一块花生田里撒球形颗粒的白色化肥。“看看，这是最好的大蒜专用肥！”他自豪地向我介绍撒化肥的专用推车、大蒜播种机，展示出他的专业和投入。我问他之前大堤上的那些蒜是怎么卖的。“没办法，两块钱卖了。本来想等等卖两块五——往年能到那价——可今年收蒜的时候水淹了路，大车不愿意来”，他皱起眉说，“7万多斤蒜，每斤少卖五角，少赚3万多元”。\r\n\r\n郭国勤50岁出头，穿着一件西装外套，戴一副白手套，足蹬运动鞋，小跑着推着一辆红色手推车，在一块花生田里撒球形颗粒的白色化肥。“看看，这是最好的大蒜专用肥！”他自豪地向我介绍撒化肥的专用推车、大蒜播种机，展示出他的专业和投入。我问他之前大堤上的那些蒜是怎么卖的。“没办法，两块钱卖了。本来想等等卖两块五——往年能到那价——可今年收蒜的时候水淹了路，大车不愿意来”，他皱起眉说，“7万多斤蒜，每斤少卖五角，少赚3万多元”。\r\n\r\n',6958,NULL);

/*Table structure for table `law` */

DROP TABLE IF EXISTS `law`;

CREATE TABLE `law` (
  `id` int NOT NULL,
  `lawtype` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `explaination` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `crime` varchar(255) DEFAULT NULL,
  `keyword` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `lawtype` */

insert  into `lawtype`(`id`,`lawtype`,`title`) values 
(1,'税务法','[{\"name\": \"《个人所得税法》\"}, {\"name\": \"《企业所得税法》\"}]');

/*Table structure for table `rule` */

DROP TABLE IF EXISTS `rule`;

CREATE TABLE `rule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `view` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `rule` */

insert  into `rule`(`id`,`title`,`content`,`view`,`date`) values 
(1,'新规的名称','新规的内容',2569,NULL),
(2,'地下水管理条例','《条例》提出，除特殊情形外，禁止开采难以更新的地下水，在禁止开采区内禁止取用地下水，在限制开采区内禁止新增取用地下水并逐步削减地下水取水量。建立地下水污染防治重点区划定制度。强化对污染地下水行为的管控，禁止以逃避监管的方式排放水污染物，禁止利用无防渗漏措施的沟渠、坑塘等输送或者贮存含有毒污染物的废水等行为。',4695,NULL),
(3,'机动车发票使用新规','国家税务总局、工业和信息化部、公安部共同制定了《机动车发票使用办法》，自5月1日起试行，7月1日起正式施行。销售机动车开具机动车销售统一发票时，应遵循以下规则：按照“一车一票”原则开具机动车销售统一发票，即一辆机动车只能开具一张机动车销售统一发票；机动车销售统一发票的“纳税人识别号/统一社会信用代码/身份证明号码”栏，销售方根据消费者实际情况填写；消费者丢失机动车销售统一发票，无法办理车辆购置税纳税申报或者机动车注册登记的，应向销售方申请重新开具机动车销售统一发票；机动车销售统一发票打印内容出现压线或者出格的，若内容清晰完整，无需退还重新开具。',2569,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
