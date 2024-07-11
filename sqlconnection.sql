CREATE DATABASE  IF NOT EXISTS `project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `project`;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: project
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `honeyword_user_data`
--

DROP TABLE IF EXISTS `honeyword_user_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `honeyword_user_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(60) NOT NULL,
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(60) NOT NULL,
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `securityQuestion` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `securityAnswer` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `honeyword_user_data`
--

LOCK TABLES `honeyword_user_data` WRITE;
/*!40000 ALTER TABLE `honeyword_user_data` DISABLE KEYS */;
INSERT INTO `honeyword_user_data` VALUES (1,'Sejal','sejal12','$2a$10$k7jMMR4w0zrHKBCZSoXOwOTquwwHCSM4CbCNiT0gtHqWT.vvutjmC','sejalsharma3012@gmail.com','9730893768','2','HP'),(2,'Kuldeep ','sharmak30','$2a$10$tLPA3cvEbgRfsXsYXy28X.hMdK1PdEXCJM3U36v9CBUMpyJPRd7vu','sharmak30@gmail.com','9767303689','1','Red'),(3,'Ritu Sharma','ritu12','$2a$10$.l51869e9a9UjNJ6ya3W7.ZED78yQJqiBIvvLQEap55ThFz0gxduW','reetuk25@gmail.com','7350901785','2','HP');
/*!40000 ALTER TABLE `honeyword_user_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `honeywords`
--

DROP TABLE IF EXISTS `honeywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `honeywords` (
  `Sno` int(11) NOT NULL AUTO_INCREMENT,
  `honeyword` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Sno`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `honeywords_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `honeyword_user_data` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `honeywords`
--

LOCK TABLES `honeywords` WRITE;
/*!40000 ALTER TABLE `honeywords` DISABLE KEYS */;
INSERT INTO `honeywords` VALUES (1,'$2a$10$xIlquVXwE2rsgWCTVINl3Of9N7oPqvMYSWmbhCCufcja26El6AQOa',1),(2,'$2a$10$0Y4DFBx9K0/sMTv33HzdRuKdrnyatLDM5rqx/IWDUIOihxD5BFwSm',1),(3,'$2a$10$zY247d9rDZzQlrF6Wj.8hOAAEX6Dm6UZvQjsnGbwgtFoXEa9PdyKa',1),(4,'$2a$10$jM4dW2OrQl2U0W/BexD0..bmeyaW1xclYv3JG4aCJDZcV183AuNQy',1),(5,'$2a$10$bL6yAMwhM3txJT7Wr/3usODlP8936wUscvb0YWNyxrmUiL2dNAVAy',1),(6,'$2a$10$NdzxzfjkvrUBItQz3yvloOufaFqdkKQJrjdYOzOjOaHWvrxupcxeK',1),(7,'$2a$10$7V1YRYPR3CFWm5e3/fJB.udAtrQo7BNQmCD13YVXSTW4hDiGa2T9i',1),(8,'$2a$10$v.z/a28Pc2pZa5xNuEPLB..DTaYqhjXzTQngKG8R9OXY/j668gFMS',1),(9,'$2a$10$ij7NVdMZnKsR5bpMshim2ug4S0toXHmNrEiQIa9Gv6ZW5HTepHKkq',1),(10,'$2a$10$WV.XQnLtcuKO3TW4k3b8reTvJkB3lKciBSeGdZ1JNA.0z1rq7kYue',1),(11,'$2a$10$zd4B.7x.nj8MX3rty00c1eI7DqTYELgNiyRaAhG35xCGh8qeILa3e',2),(12,'$2a$10$yFR49cHQ1KH6bEyS.1mNYu1uMsGw//VQR3Ns.7v.LTrWkaqgcNjFm',2),(13,'$2a$10$02iAdcCklBzCDFSGzeXEV.kAjPIpl.BmrVIGhl/fAPNdgdd7CaswS',2),(14,'$2a$10$521HdljQZL6R0Tcy9eyku.sRgUIbtkZEeGvrY.DOOjqtBVuw/pojO',2),(15,'$2a$10$Wb1AUHHrrV1TcRcM1SwWQelEKqW6JQu07IGHM5FPVwDqyAwTkYRNi',2),(16,'$2a$10$hkBBp7kX131Fcuh22GpxSe6A82C65i18A6FPoSxXVId.oP.l.g.Ba',2),(17,'$2a$10$YLXvufK8JKWa4xsDpbXzb.ilsA.2Cuzhre/7WAGAZzZGmh2Tuqt2y',2),(18,'$2a$10$lJG9ZjpOvAIfxichaWW42OVX2HuAMAa280bOmHEypR8nfCuyD5.cq',2),(19,'$2a$10$OSSjX6070506FmYTzjumZO8P6C.NeeZJneGVQumjlI2Tf34DbGWeK',2),(20,'$2a$10$tHr51iNYAFShXxMCMvWtnOudO7J9vzW8E6uirDYoIkIwW9pZmjm9a',2),(21,'$2a$10$2CbqKoTwmztHBRmzUC.Xb.jt9vUzbASMDIk3.ya0Gf8r/Tq8..mHm',3),(22,'$2a$10$tD1Ik2Zyq/4VUfQYHMBtIeoIBjXD8vyJD.1X9JcdISY6fjrMVLhdC',3),(23,'$2a$10$iu/vB2NJc5VqAK9G7JCgV.7S14tk5liuzWYcytmqY5pH.TtEPl3M6',3),(24,'$2a$10$ZcKgYIvRnq1Lxamr4M1iWOSJPceO8m71ueJLnqf6t0p6nxGp3NSzC',3),(25,'$2a$10$nieaaSf76gD7KZmr/mTdyOhQRjPNFlNR2p/yrIcZKjIE1xi2gAgQa',3),(26,'$2a$10$Z.3mzgVBJ8LYqnDgjW3jnu05yC93B.h5YCQcPc7RbvPqWu0hAjyPC',3),(27,'$2a$10$szipS.7.BUFoCtCGnILkA.4XntmUUrhJMvc6dMkfpnExC4zTMNA5u',3),(28,'$2a$10$pNkQURFRixGOYayWZbcGq.bcngaKKyjNlFEnmU4bphUaZgSI9n8Ie',3),(29,'$2a$10$c5keeatNIMlKAEWRqINUruiggJ.Lrt3lzEPeBwi0h3aLUoAxKCYSG',3),(30,'$2a$10$Cfi6AwdnH46qvqGUW.fhIu65CBBo4KgGxVPQeaOAeZ/pXoRLc0qhO',3),(41,'$2a$10$Fu7vsP00Wr4BfiMr76cxGeFU2Pv7IpEfQ9Ylm0EJ2RfHxCi9rLVbC',3),(42,'$2a$10$/xWp2.a7d6yGbAYWxL1UQelnmJnjv47/C6ECoy1WWx0eJjDgOW036',3),(43,'$2a$10$SR6QBuX5BmOxftGh1cbAK.um7QVoRfrUDVmhT2zB0R0foYWfh/Qsm',3),(44,'$2a$10$XhBfeCJ8ry.TNzFfcanfXO4.xJOyJyOAiJhYOQShN99qrPlKNwSqi',3),(45,'$2a$10$g/Ix27e4bIbZKDHr0W0wO.F9uy1Z1e84wddNJDI9qte9rVt6t85Sa',3),(46,'$2a$10$CPWvfB5B.M/eUDqy6oRobOMgnoNfAtf9OBeR3vqHYtpz0WXrGArJy',3),(47,'$2a$10$aBB/3xdg.J74DBheQddOYOdApN7s0VUEY42OD1vejDpoVZYj11GBy',3),(48,'$2a$10$E0yBxdCOHi3f.I3sHQVpdurkbzjIwGJJcGhbyBbhrMCKy53KklmPu',3),(49,'$2a$10$4JRWA/oa3cdlD80keBomUe2H/U5g5zZhXnj.CxkM9mwYD7Q4G5RCi',3),(50,'$2a$10$tqQIvmYY3YX9K3deZ6AsEeTxM8abK07HMDHBq/T9jxOyhTLM95Xi6',3);
/*!40000 ALTER TABLE `honeywords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `password_reset_tokens` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiration` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `honeyword_user_data` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
INSERT INTO `password_reset_tokens` VALUES (12,1,'2c260344-ab62-47d6-a154-215bd55e2b4d','2024-05-28 22:08:47'),(13,2,'345baf41-33fa-43bd-9eb2-61fb77bc18eb','2024-05-29 15:54:36'),(14,3,'0b70a6b3-9cea-48ff-a5d6-16fd1ac45ffc','2024-05-30 15:36:25'),(15,1,'4dad4152-3932-4640-adb5-db391134b12f','2024-05-30 17:00:11'),(16,1,'2d8e1e9f-d2df-4ccf-80bc-26ac29165d40','2024-05-30 17:24:30'),(17,2,'194d0b1c-f307-45db-aa29-8610cb3b286c','2024-06-01 06:27:23');
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-01 12:36:47
