-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 09, 2024 at 06:42 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlthanhvien`
--

-- --------------------------------------------------------

--
-- Table structure for table `lichsuxoa`
--

CREATE TABLE `lichsuxoa` (
                             `id` int(11) NOT NULL,
                             `MaTT` int(11) NOT NULL,
                             `delete_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `lichsuxoa`
--

INSERT INTO `lichsuxoa` (`id`, `MaTT`, `delete_at`) VALUES
    (1, 10, '2024-05-10 07:03:44');

-- --------------------------------------------------------

--
-- Table structure for table `thanhvien`
--

CREATE TABLE `thanhvien`
(
    `MaTV`                 bigint(20)   NOT NULL,
    `HoTen`                varchar(100) NOT NULL,
    `Khoa`                 varchar(100) DEFAULT NULL,
    `Nganh`                varchar(100) DEFAULT NULL,
    `SDT`                  varchar(15)  DEFAULT NULL,
    `Email`                varchar(25)  NOT NULL,
    `Password`             varchar(10)  DEFAULT NULL,
    `reset_password_token` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `thanhvien`
--

INSERT INTO `thanhvien` (`MaTV`, `HoTen`, `Khoa`, `Nganh`, `SDT`, `Email`, `Password`, `reset_password_token`) VALUES
                                                                                                                   (1120150184, 'Trần Thị Nữ', 'GDTH', 'GDTH', '1111111111', '', NULL, NULL),
                                                                                                                   (1121530087, 'Trần Thiếu Nam', 'CNTT', 'QLGD', '1111111112', '', NULL, NULL),
                                                                                                                   (1123330257, 'Ngô Tuyết Nhi', 'QTKD', 'QTKD', '1111111113', '', NULL, NULL),
                                                                                                                   (2147483647, 'Nguyễn Văn Nam', 'CNTT', 'HTTT', '123456789', '', NULL, NULL),
                                                                                                                   (3121410111, 'Nguyễn Tiến Dũng', 'CNTT', 'KTPM', '0812535278', 'dungboi1029@gmail.com', '12345', NULL),
                                                                                                                   (3121410116, 'Đinh Quang Duy', 'CNTT', 'KTPM', '0812535279', 'ntd3121410111@gmail.com', '12345', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `thietbi`
--

CREATE TABLE `thietbi`
(
    `MaTB`   bigint(20)   NOT NULL,
    `TenTB`  varchar(100) NOT NULL,
    `MoTaTB` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `thietbi`
--

INSERT INTO `thietbi` (`MaTB`, `TenTB`, `MoTaTB`) VALUES
                                                      (1000001, 'Micro', 'Micro không dây MS2023'),
                                                      (1000002, 'Micro', 'Micro không dây MS2024'),
                                                      (1000003, 'Bảng điện tử', 'Bản điện tử trình chiếu');

-- --------------------------------------------------------

--
-- Table structure for table `thongtinsd`
--

CREATE TABLE `thongtinsd`
(
    `MaTT`     int(11)    NOT NULL,
    `MaTV`     bigint(20) NOT NULL,
    `MaTB`     bigint(20)  DEFAULT NULL,
    `TGVao`    datetime    DEFAULT NULL,
    `TGMuon`   datetime    DEFAULT NULL,
    `TGTra`    datetime    DEFAULT NULL,
    `TGDatcho` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `thongtinsd`
--

INSERT INTO `thongtinsd` (`MaTT`, `MaTV`, `MaTB`, `TGVao`, `TGMuon`, `TGTra`, `TGDatcho`) VALUES
                                                                                              (1, 1120150184, NULL, '2024-03-05 09:00:00', NULL, NULL, NULL),
                                                                                              (2, 1123330257, 1000001, NULL, '2024-02-12 10:00:32', '2024-02-12 14:00:00', NULL),
                                                                                              (3, 1121530087, NULL, '2024-03-05 09:00:00', NULL, NULL, NULL),
                                                                                              (4, 2147483647, NULL, '2024-03-10 09:00:00', NULL, NULL, NULL),
                                                                                              (6, 3121410111, 1000002, NULL, '2024-05-09 06:36:58', NULL, NULL);

--
-- Triggers `thongtinsd`
--
DELIMITER $$
CREATE TRIGGER `auto_delete` AFTER INSERT ON `thongtinsd` FOR EACH ROW BEGIN
    IF NEW.TGDatcho IS NOT NULL THEN
        INSERT INTO `lichsuxoa` (`MaTT`, `delete_at`)
        VALUES (NEW.MaTT, DATE_ADD(NEW.TGDatcho, INTERVAL 1 HOUR));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `xuly`
--

CREATE TABLE `xuly`
(
    `MaXL`        int(11)    NOT NULL,
    `MaTV`        bigint(20) NOT NULL,
    `HinhThucXL`  varchar(250) DEFAULT NULL,
    `SoTien`      int(11)      DEFAULT NULL,
    `NgayXL`      datetime     DEFAULT NULL,
    `TrangThaiXL` int(11)      DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `xuly`
--

INSERT INTO `xuly` (`MaXL`, `MaTV`, `HinhThucXL`, `SoTien`, `NgayXL`, `TrangThaiXL`) VALUES
                                                                                         (1, 1121530087, 'Khóa thẻ 1 tháng', NULL, '2023-09-12 08:00:00', 0),
                                                                                         (2, 2147483647, 'Khóa thẻ 2 tháng', NULL, '2023-09-12 08:00:00', 0),
                                                                                         (3, 1123330257, 'Bồi thường mất tài sản', 300000, '2023-09-12 08:00:00', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `lichsuxoa`
--
ALTER TABLE `lichsuxoa`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `thanhvien`
--
ALTER TABLE `thanhvien`
    ADD PRIMARY KEY (`MaTV`);

--
-- Indexes for table `thietbi`
--
ALTER TABLE `thietbi`
    ADD PRIMARY KEY (`MaTB`);

--
-- Indexes for table `thongtinsd`
--
ALTER TABLE `thongtinsd`
    ADD PRIMARY KEY (`MaTT`),
    ADD KEY `MaTV` (`MaTV`,`MaTB`),
    ADD KEY `MaTB` (`MaTB`);

--
-- Indexes for table `xuly`
--
ALTER TABLE `xuly`
    ADD PRIMARY KEY (`MaXL`),
    ADD KEY `MaTV` (`MaTV`),
    ADD KEY `MaTV_2` (`MaTV`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `lichsuxoa`
--
ALTER TABLE `lichsuxoa`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `thongtinsd`
--
ALTER TABLE `thongtinsd`
    MODIFY `MaTT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `xuly`
--
ALTER TABLE `xuly`
    MODIFY `MaXL` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `thongtinsd`
--
ALTER TABLE `thongtinsd`
    ADD CONSTRAINT `thongtinsd_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`) ON DELETE CASCADE,
    ADD CONSTRAINT `thongtinsd_ibfk_2` FOREIGN KEY (`MaTB`) REFERENCES `thietbi` (`MaTB`) ON DELETE CASCADE;

--
-- Constraints for table `xuly`
--
ALTER TABLE `xuly`
    ADD CONSTRAINT `xuly_ibfk_1` FOREIGN KEY (`MaTV`) REFERENCES `thanhvien` (`MaTV`) ON DELETE CASCADE;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `auto_delete_thongtinsd` ON SCHEDULE EVERY 1 MINUTE STARTS '2024-05-09 21:10:07' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE t FROM thongtinsd t JOIN lichsuxoa sd ON t.MaTT = sd.MaTT
    WHERE sd.delete_at <= NOW();
    DELETE FROM lichsuxoa WHERE delete_at <= NOW();
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
