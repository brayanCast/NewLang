CREATE DATABASE  IF NOT EXISTS `newlang` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `newlang`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: newlang
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id_category` tinyint NOT NULL,
  `category_description` varchar(70) NOT NULL,
  PRIMARY KEY (`id_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Cultura');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_frequency`
--

DROP TABLE IF EXISTS `daily_frequency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_frequency` (
  `id_frequency` tinyint NOT NULL,
  `frequency_description` varchar(50) NOT NULL,
  `frequency` int NOT NULL,
  PRIMARY KEY (`id_frequency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_frequency`
--

LOCK TABLES `daily_frequency` WRITE;
/*!40000 ALTER TABLE `daily_frequency` DISABLE KEYS */;
INSERT INTO `daily_frequency` VALUES (1,'10 minutos al día',10);
/*!40000 ALTER TABLE `daily_frequency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expression`
--

DROP TABLE IF EXISTS `expression`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expression` (
  `id_expression` int NOT NULL AUTO_INCREMENT,
  `expression_english` varchar(150) NOT NULL,
  `expression_spanish` varchar(150) NOT NULL,
  `image_expression` longblob,
  `id_type` tinyint DEFAULT NULL,
  `id_level` tinyint DEFAULT NULL,
  `id_category` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_expression`),
  UNIQUE KEY `id_type` (`id_type`),
  UNIQUE KEY `id_level` (`id_level`),
  UNIQUE KEY `id_category` (`id_category`),
  CONSTRAINT `expression_ibfk_1` FOREIGN KEY (`id_type`) REFERENCES `word_expression_type` (`id_type`),
  CONSTRAINT `expression_ibfk_2` FOREIGN KEY (`id_level`) REFERENCES `level` (`id_level`),
  CONSTRAINT `expression_ibfk_3` FOREIGN KEY (`id_category`) REFERENCES `category` (`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expression`
--

LOCK TABLES `expression` WRITE;
/*!40000 ALTER TABLE `expression` DISABLE KEYS */;
INSERT INTO `expression` VALUES (1,'Hands down','Sin duda alguna',_binary '0',1,1,1);
/*!40000 ALTER TABLE `expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_routine`
--

DROP TABLE IF EXISTS `learning_routine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_routine` (
  `id_routine` int NOT NULL AUTO_INCREMENT,
  `name_routine` varchar(100) NOT NULL,
  `word_quantity` int NOT NULL,
  `expression_quantity` int NOT NULL,
  `id_user` int DEFAULT NULL,
  `id_level` tinyint DEFAULT NULL,
  `id_frequency` tinyint DEFAULT NULL,
  `id_category` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_routine`),
  UNIQUE KEY `id_level` (`id_level`),
  UNIQUE KEY `id_frequency` (`id_frequency`),
  KEY `id_user` (`id_user`),
  KEY `id_category` (`id_category`),
  CONSTRAINT `learning_routine_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  CONSTRAINT `learning_routine_ibfk_2` FOREIGN KEY (`id_level`) REFERENCES `level` (`id_level`),
  CONSTRAINT `learning_routine_ibfk_3` FOREIGN KEY (`id_frequency`) REFERENCES `daily_frequency` (`id_frequency`),
  CONSTRAINT `learning_routine_ibfk_4` FOREIGN KEY (`id_category`) REFERENCES `category` (`id_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_routine`
--

LOCK TABLES `learning_routine` WRITE;
/*!40000 ALTER TABLE `learning_routine` DISABLE KEYS */;
/*!40000 ALTER TABLE `learning_routine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `level` (
  `id_level` tinyint NOT NULL,
  `level_description` varchar(50) NOT NULL,
  PRIMARY KEY (`id_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'Principiante A0');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `name_user` varchar(70) NOT NULL,
  `email` varchar(70) NOT NULL,
  `password` varchar(70) NOT NULL,
  `id_user_type` int DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_user_type` (`id_user_type`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_user_type`) REFERENCES `user_type` (`id_user_type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Miguel de Cervantes Saavedra','miguel.decervantes@gmail.com','password',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_administrator`
--

DROP TABLE IF EXISTS `user_administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_administrator` (
  `id_number` int NOT NULL,
  `id_user` int DEFAULT NULL,
  PRIMARY KEY (`id_number`),
  UNIQUE KEY `id_user` (`id_user`),
  CONSTRAINT `user_administrator_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_administrator`
--

LOCK TABLES `user_administrator` WRITE;
/*!40000 ALTER TABLE `user_administrator` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type`
--

DROP TABLE IF EXISTS `user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_type` (
  `id_user_type` int NOT NULL,
  `type_user` varchar(30) NOT NULL,
  PRIMARY KEY (`id_user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,'user'),(2,'user administrator');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word`
--

DROP TABLE IF EXISTS `word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `word` (
  `id_word` int NOT NULL AUTO_INCREMENT,
  `word_english` varchar(150) NOT NULL,
  `word_spanish` varchar(150) NOT NULL,
  `image_word` longblob,
  `id_type` tinyint DEFAULT NULL,
  `id_level` tinyint DEFAULT NULL,
  `id_category` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_word`),
  UNIQUE KEY `id_type` (`id_type`),
  UNIQUE KEY `id_level` (`id_level`),
  UNIQUE KEY `id_category` (`id_category`),
  CONSTRAINT `word_ibfk_1` FOREIGN KEY (`id_type`) REFERENCES `word_expression_type` (`id_type`),
  CONSTRAINT `word_ibfk_2` FOREIGN KEY (`id_level`) REFERENCES `level` (`id_level`),
  CONSTRAINT `word_ibfk_3` FOREIGN KEY (`id_category`) REFERENCES `category` (`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word`
--

LOCK TABLES `word` WRITE;
/*!40000 ALTER TABLE `word` DISABLE KEYS */;
INSERT INTO `word` VALUES (1,'Through','Mediante',_binary '0',1,1,1);
/*!40000 ALTER TABLE `word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word_expression_type`
--

DROP TABLE IF EXISTS `word_expression_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `word_expression_type` (
  `id_type` tinyint NOT NULL,
  `name_type` varchar(30) NOT NULL,
  PRIMARY KEY (`id_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word_expression_type`
--

LOCK TABLES `word_expression_type` WRITE;
/*!40000 ALTER TABLE `word_expression_type` DISABLE KEYS */;
INSERT INTO `word_expression_type` VALUES (1,'Expresión');
/*!40000 ALTER TABLE `word_expression_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-29 19:01:21
