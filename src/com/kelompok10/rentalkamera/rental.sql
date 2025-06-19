-- Perintah untuk menghapus database jika sudah ada, agar tidak terjadi error
DROP DATABASE IF EXISTS rental_kamera;

-- Membuat database baru dengan nama 'rental_kamera'
CREATE DATABASE rental_kamera;

-- Memilih database 'rental_kamera' untuk digunakan pada perintah selanjutnya
USE rental_kamera;

-- --------------------------------------------------------

--
-- Struktur tabel untuk `users`
--
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Menambahkan data awal untuk admin agar bisa login pertama kali
--
INSERT INTO `users` (`username`, `email`, `password`, `role`) VALUES
('admin', 'admin@gmail.com', 'admin123', 'admin');

-- --------------------------------------------------------

--
-- Struktur tabel untuk `cameras`
--
CREATE TABLE `cameras` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `harga_rental` double NOT NULL,
  `stok` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Menambahkan contoh data kamera untuk pengujian
--
INSERT INTO `cameras` (`brand`, `type`, `harga_rental`, `stok`) VALUES
('Canon', 'EOS 60D', 150000, 5),
('Sony', 'A7 III', 250000, 3),
('Nikon', 'D850', 200000, 4);