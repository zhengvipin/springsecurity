/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.18 : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `hibernate_sequence` */

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values (1);
insert  into `hibernate_sequence`(`next_val`) values (1);

/*Table structure for table `role` */

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`id`,`name`) values (1,'ROLE_ADMIN');

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `enabled` int(11) DEFAULT NULL,
  `last_password_reset_date` datetime DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`enabled`,`last_password_reset_date`,`login_date`,`password`,`username`) values (1,1,'2018-06-01 08:40:33','2018-06-27 08:40:36','$2a$10$FVHcEwybPACEmLlEdDKS1.XBIuGwz1cD3bKlsRpXhxRd8dLlR0vla','admin');

/*Table structure for table `user_roles` */

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `user_roles` */

insert  into `user_roles`(`user_id`,`roles_id`) values (1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
