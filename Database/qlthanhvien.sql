-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: qlthanhvien
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `thanhvien`
--

DROP TABLE IF EXISTS `thanhvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thanhvien` (
  `MaTV` bigint NOT NULL,
  `HoTen` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `Khoa` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Nganh` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `SDT` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Email` varchar(25) COLLATE utf8mb4_general_ci NOT NULL,
  `Password` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`MaTV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thanhvien`
--

LOCK TABLES `thanhvien` WRITE;
/*!40000 ALTER TABLE `thanhvien` DISABLE KEYS */;
INSERT INTO `thanhvien` VALUES (1120150184,'Trần Thị Nữ','GDTH','GDTH','1111111111','',NULL),(1121530087,'Trần Thiếu Nam','CNTT','QLGD','1111111112','',NULL),(1123330257,'Ngô Tuyết Nhi','QTKD','QTKD','1111111113','',NULL),(2147483647,'Nguyễn Văn Nam','CNTT','HTTT','123456789','',NULL);
/*!40000 ALTER TABLE `thanhvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thietbi`
--

DROP TABLE IF EXISTS `thietbi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thietbi` (
  `MaTB` bigint NOT NULL,
  `TenTB` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `MoTaTB` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`MaTB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thietbi`
--

LOCK TABLES `thietbi` WRITE;
/*!40000 ALTER TABLE `thietbi` DISABLE KEYS */;
INSERT INTO `thietbi` VALUES (1000001,'Micro','Micro không dây MS2023'),(1000002,'Micro','Micro không dây MS2024'),(1000003,'Bảng điện tử','Bản điện tử trình chiếu');
/*!40000 ALTER TABLE `thietbi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thongtinsd`
--

DROP TABLE IF EXISTS `thongtinsd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thongtinsd` (
  `MaTT` int NOT NULL AUTO_INCREMENT,
  `MaTV` bigint NOT NULL,
  `MaTB` bigint DEFAULT NULL,
  `TGVao` datetime DEFAULT NULL,
  `TGMuon` datetime DEFAULT NULL,
  `TGTra` datetime DEFAULT NULL,
  `TGDatcho` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`MaTT`),
  KEY `MaTV` (`MaTV`,`MaTB`),
  KEY `MaTB` (`MaTB`),
  CONSTRAINT `thongtinsd_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`) ON DELETE CASCADE,
  CONSTRAINT `thongtinsd_ibfk_2` FOREIGN KEY (`MaTB`) REFERENCES `thietbi` (`MaTB`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thongtinsd`
--

LOCK TABLES `thongtinsd` WRITE;
/*!40000 ALTER TABLE `thongtinsd` DISABLE KEYS */;
INSERT INTO `thongtinsd` VALUES (1,1120150184,NULL,'2024-03-05 09:00:00',NULL,NULL,NULL),(2,1123330257,1000001,NULL,'2024-02-12 10:00:32','2024-02-12 14:00:00',NULL),(3,1121530087,NULL,'2024-03-05 09:00:00',NULL,NULL,NULL),(4,2147483647,NULL,'2024-03-10 09:00:00',NULL,NULL,NULL);
/*!40000 ALTER TABLE `thongtinsd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xuly`
--

DROP TABLE IF EXISTS `xuly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xuly` (
  `MaXL` int NOT NULL AUTO_INCREMENT,
  `MaTV` bigint NOT NULL,
  `HinhThucXL` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `SoTien` int DEFAULT NULL,
  `NgayXL` datetime DEFAULT NULL,
  `TrangThaiXL` int DEFAULT NULL,
  PRIMARY KEY (`MaXL`),
  KEY `MaTV` (`MaTV`),
  KEY `MaTV_2` (`MaTV`),
  CONSTRAINT `xuly_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xuly`
--

LOCK TABLES `xuly` WRITE;
/*!40000 ALTER TABLE `xuly` DISABLE KEYS */;
INSERT INTO `xuly` VALUES (1,1121530087,'Khóa thẻ 1 tháng',NULL,'2023-09-12 08:00:00',0),(2,2147483647,'Khóa thẻ 2 tháng',NULL,'2023-09-12 08:00:00',0),(3,1123330257,'Bồi thường mất tài sản',300000,'2023-09-12 08:00:00',0);
/*!40000 ALTER TABLE `xuly` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-04 14:16:18
