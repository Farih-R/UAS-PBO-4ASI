-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 08, 2025 at 11:19 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_rentalps`
--

-- --------------------------------------------------------

--
-- Table structure for table `jenis_ps`
--

CREATE TABLE `jenis_ps` (
  `id_jenisps` int(11) NOT NULL,
  `jenis_ps` varchar(10) NOT NULL,
  `jam_sewa` int(11) NOT NULL,
  `diskon` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id_menu` int(11) NOT NULL,
  `nama_penyewa` varchar(100) NOT NULL,
  `jenis_makanan` varchar(100) NOT NULL,
  `jumlah_makanan` varchar(100) DEFAULT NULL,
  `diskon` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `penyewa`
--

CREATE TABLE `penyewa` (
  `id_penyewa` int(11) NOT NULL,
  `nama_penyewa` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_transaksi`
--

CREATE TABLE `riwayat_transaksi` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jenis_ps` varchar(10) NOT NULL,
  `jam` int(11) NOT NULL,
  `biaya_ps` int(11) NOT NULL,
  `menu` text DEFAULT NULL,
  `biaya_menu` int(11) DEFAULT NULL,
  `total_bayar` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `riwayat_transaksi`
--

INSERT INTO `riwayat_transaksi` (`id`, `nama`, `jenis_ps`, `jam`, `biaya_ps`, `menu`, `biaya_menu`, `total_bayar`) VALUES
(1, 'rakha', 'PS3', 2, 10000, 'Es Kopi', 6000, 16000),
(3, 'Zeva', 'PS3', 2, 10000, 'Mie Instan, Es Teh', 13000, 23000),
(4, 'Igul', 'PS3', 3, 15000, 'Mie Instan, Es Teh', 13000, 28000),
(5, 'abrar', 'PS4', 3, 24000, 'Mie Instan, Es Teh', 13000, 37000),
(6, 'alan', 'PS4', 5, 40000, 'Mie Instan, Snack', 15000, 55000),
(7, 'Farhan', 'PS3', 2, 10000, 'Mie Instan, Es Teh', 13000, 23000),
(8, 'Farih', 'PS4', 3, 24000, 'Mie Instan, Es Teh, Snack, Es Kopi', 26000, 50000),
(9, 'Farhan', 'PS3', 2, 10000, 'Mie Instan, Es Teh', 13000, 18000),
(11, 'rakha', 'PS4', 2, 16000, 'Mie Instan x1, Snack x1', 15000, 26000),
(12, 'zeva', 'PS3', 2, 10000, 'Es Kopi x1', 6000, 16000),
(13, 'Rakha', 'PS3', 5, 25000, 'Mie x1, Es Teh x1', 13000, 38000),
(14, 'Igul', 'PS3', 3, 15000, 'Mie x1, Es Teh x1', 13000, 28000),
(17, 'adit', 'PS4', 5, 40000, 'Es Kopi x2', 12000, 52000),
(18, 'adit', 'PS4', 5, 40000, 'Es Kopi x2', 12000, 52000),
(19, 'Edik', 'PS3', 3, 15000, 'Es Kopi x1', 6000, 21000),
(20, 'syam', 'PS3', 3, 15000, 'Es Teh x1, Es Kopi x1', 11000, 26000),
(21, 'rakha', 'PS3', 2, 10000, '-', 0, 10000),
(22, 'arya', 'PS4', 2, 16000, 'Es Kopi x1', 6000, 22000),
(23, 'Nala', 'PS5', 9, 108000, 'Mie x1, Es Teh x2', 18000, 113400),
(24, 'ghozali', 'PS5', 2, 24000, 'Es Kopi x1', 6000, 30000),
(25, 'Rizky', 'PS4', 3, 24000, 'Es Kopi x1', 6000, 30000),
(26, 'cokro', 'PS3', 3, 15000, 'Es Teh x1', 5000, 20000),
(27, 'syam', 'PS4', 2, 16000, 'Es Kopi x1', 6000, 22000),
(28, 'abid zeva', 'PS3', 2, 10000, 'Es Kopi x1', 6000, 16000),
(29, 'zeva abid', 'PS4', 3, 24000, 'Es Teh x1', 5000, 29000),
(30, 'aditya tazkia', 'PS3', 2, 10000, 'Es Teh x1, Es Kopi x1', 11000, 21000),
(31, 'aulia mufid', 'PS5', 2, 24000, 'Mie x1', 8000, 32000),
(32, 'aulia mufid', 'PS3', 2, 10000, '-', 0, 10000),
(33, 'adita mufid', 'PS4', 2, 16000, '-', 0, 16000),
(34, 'aditya aulia', 'PS4', 2, 16000, 'Es Kopi x1', 6000, 22000),
(35, 'zeva ghozali', 'PS3', 2, 10000, 'Es Teh x2', 10000, 20000),
(36, 'farih rahmatullah', 'PS3', 2, 10000, 'Es Teh x1', 5000, 15000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jenis_ps`
--
ALTER TABLE `jenis_ps`
  ADD PRIMARY KEY (`id_jenisps`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id_menu`);

--
-- Indexes for table `penyewa`
--
ALTER TABLE `penyewa`
  ADD PRIMARY KEY (`id_penyewa`);

--
-- Indexes for table `riwayat_transaksi`
--
ALTER TABLE `riwayat_transaksi`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jenis_ps`
--
ALTER TABLE `jenis_ps`
  MODIFY `id_jenisps` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id_menu` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `penyewa`
--
ALTER TABLE `penyewa`
  MODIFY `id_penyewa` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `riwayat_transaksi`
--
ALTER TABLE `riwayat_transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
