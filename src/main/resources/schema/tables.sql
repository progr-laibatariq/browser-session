-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 03, 2026 at 07:56 PM
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
-- Database: `browser_session_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity_log`
--

CREATE TABLE `activity_log` (
                                `log_id` int(11) NOT NULL,
                                `session_id` int(11) NOT NULL,
                                `operation` enum('SESSION_CREATED','SESSION_UPDATED','TAB_OPENED','TAB_CLOSED','SESSION_CLOSED') NOT NULL,
                                `details` varchar(255) DEFAULT NULL,
                                `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ;

--
-- Dumping data for table `activity_log`
--

INSERT INTO `activity_log` (`log_id`, `session_id`, `operation`, `details`, `created_at`) VALUES
                                                                                              (1, 1, 'SESSION_CREATED', 'capacity=3', '2026-01-19 02:02:47'),
                                                                                              (2, 1, 'TAB_OPENED', 'url=https://youtube.com tabId=1', '2026-01-19 02:04:42'),
                                                                                              (3, 1, 'TAB_OPENED', 'url=https://github.com tabId=2', '2026-01-19 02:05:06'),
                                                                                              (4, 1, 'TAB_OPENED', 'url=https://github.com tabId=3', '2026-01-19 02:05:30'),
                                                                                              (5, 1, 'TAB_CLOSED', 'EXPIRED tabId=2', '2026-01-19 02:15:07'),
                                                                                              (6, 1, 'TAB_CLOSED', 'EXPIRED tabId=3', '2026-01-19 02:15:30'),
                                                                                              (7, 2, 'SESSION_CREATED', 'capacity=3', '2026-01-19 02:45:55'),
                                                                                              (8, 2, 'TAB_OPENED', 'url=https://a.com tabId=4', '2026-01-19 02:48:45'),
                                                                                              (9, 2, 'TAB_OPENED', 'url=https://b.com tabId=5', '2026-01-19 02:48:55'),
                                                                                              (10, 2, 'TAB_OPENED', 'url=https://c.com tabId=6', '2026-01-19 02:49:04'),
                                                                                              (11, 2, 'SESSION_UPDATED', 'ACCESS tabId=4', '2026-01-19 02:51:01'),
                                                                                              (12, 2, 'SESSION_UPDATED', 'UNDO Access tabId=4', '2026-01-19 02:52:28'),
                                                                                              (13, 2, 'SESSION_UPDATED', 'ACCESS tabId=4', '2026-01-19 02:52:56'),
                                                                                              (14, 2, 'TAB_OPENED', 'url=https://d.com tabId=7', '2026-01-19 02:56:01'),
                                                                                              (15, 2, 'TAB_OPENED', 'url=https://e.com tabId=8', '2026-01-19 02:58:41'),
                                                                                              (16, 2, 'TAB_CLOSED', 'EXPIRED tabId=6', '2026-01-19 02:59:05'),
                                                                                              (17, 2, 'TAB_CLOSED', 'EXPIRED tabId=4', '2026-01-19 03:02:56'),
                                                                                              (18, 2, 'TAB_CLOSED', 'EXPIRED tabId=7', '2026-01-19 03:06:01'),
                                                                                              (19, 2, 'TAB_CLOSED', 'EXPIRED tabId=8', '2026-01-19 03:08:41'),
                                                                                              (20, 2, 'TAB_OPENED', 'url=https://f.com tabId=9', '2026-01-19 03:20:34'),
                                                                                              (21, 2, 'TAB_OPENED', 'url=https://f.com tabId=10', '2026-01-19 03:21:05'),
                                                                                              (22, 2, 'TAB_OPENED', 'url=https://g.com tabId=11', '2026-01-19 03:21:52'),
                                                                                              (23, 2, 'TAB_OPENED', 'url=https://f.com tabId=12', '2026-01-19 03:23:37'),
                                                                                              (24, 2, 'TAB_CLOSED', 'EXPIRED tabId=12', '2026-01-19 03:33:38'),
                                                                                              (25, 2, 'TAB_OPENED', 'url=https://f.com tabId=13', '2026-01-19 03:34:11'),
                                                                                              (26, 2, 'TAB_OPENED', 'url=https://f.com tabId=14', '2026-01-19 03:34:14'),
                                                                                              (27, 2, 'TAB_CLOSED', 'LRU_EVICT tabId=11', '2026-01-19 03:34:16'),
                                                                                              (28, 2, 'TAB_OPENED', 'url=https://f.com tabId=15', '2026-01-19 03:34:16'),
                                                                                              (29, 2, 'TAB_CLOSED', 'LRU_EVICT tabId=13', '2026-01-19 03:34:19'),
                                                                                              (30, 2, 'TAB_OPENED', 'url=https://f.com tabId=16', '2026-01-19 03:34:19'),
                                                                                              (31, 2, 'TAB_CLOSED', 'LRU_EVICT tabId=14', '2026-01-19 03:34:22'),
                                                                                              (32, 2, 'TAB_OPENED', 'url=https://f.com tabId=17', '2026-01-19 03:34:22'),
                                                                                              (33, 3, 'SESSION_CREATED', 'capacity=3', '2026-01-19 03:40:55'),
                                                                                              (34, 3, 'TAB_OPENED', 'url=https://1.com tabId=18', '2026-01-19 03:42:09'),
                                                                                              (35, 3, 'TAB_OPENED', 'url=https://1.com tabId=19', '2026-01-19 03:42:18'),
                                                                                              (36, 3, 'TAB_OPENED', 'url=https://1.com tabId=20', '2026-01-19 03:42:21'),
                                                                                              (37, 3, 'TAB_CLOSED', 'LRU_EVICT tabId=18', '2026-01-19 03:42:24'),
                                                                                              (38, 3, 'TAB_OPENED', 'url=https://1.com tabId=21', '2026-01-19 03:42:24'),
                                                                                              (39, 4, 'SESSION_CREATED', 'capacity=4', '2026-01-21 00:54:12'),
                                                                                              (40, 4, 'TAB_OPENED', 'url=http://laiba.com tabId=22', '2026-01-21 00:54:41'),
                                                                                              (41, 5, 'SESSION_CREATED', 'capacity=5', '2026-01-22 15:24:46'),
                                                                                              (42, 5, 'TAB_OPENED', 'url=https:// gift.edu.pk tabId=23', '2026-01-22 15:25:19'),
                                                                                              (43, 5, 'TAB_CLOSED', 'EXPIRED tabId=23', '2026-01-22 15:35:20'),
                                                                                              (44, 3, 'SESSION_UPDATED', 'ACCESS tabId=19', '2026-01-22 23:30:19'),
                                                                                              (45, 3, 'SESSION_UPDATED', 'ACCESS tabId=20', '2026-01-22 23:30:22'),
                                                                                              (46, 3, 'SESSION_UPDATED', 'ACCESS tabId=21', '2026-01-22 23:30:25'),
                                                                                              (47, 3, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=21', '2026-01-22 23:30:27'),
                                                                                              (48, 6, 'SESSION_CREATED', 'capacity=5', '2026-01-22 23:30:31'),
                                                                                              (49, 3, 'TAB_OPENED', 'url=https://localhost tabId=24', '2026-01-22 23:31:11'),
                                                                                              (50, 3, 'SESSION_UPDATED', 'ACCESS tabId=19', '2026-01-22 23:31:22'),
                                                                                              (51, 3, 'SESSION_UPDATED', 'ACCESS tabId=19', '2026-01-22 23:31:27'),
                                                                                              (52, 3, 'SESSION_UPDATED', 'ACCESS tabId=20', '2026-01-22 23:31:32'),
                                                                                              (53, 3, 'SESSION_UPDATED', 'ACCESS tabId=24', '2026-01-22 23:31:38'),
                                                                                              (54, 3, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=19', '2026-01-22 23:34:25'),
                                                                                              (55, 3, 'SESSION_UPDATED', 'ACCESS tabId=20', '2026-01-22 23:34:29'),
                                                                                              (56, 3, 'SESSION_UPDATED', 'ACCESS tabId=24', '2026-01-22 23:34:33'),
                                                                                              (57, 3, 'TAB_OPENED', 'url=http://192.168.120.0 tabId=25', '2026-01-22 23:35:15'),
                                                                                              (58, 3, 'TAB_CLOSED', 'LRU_EVICT tabId=20', '2026-01-22 23:35:51'),
                                                                                              (59, 3, 'TAB_OPENED', 'url=https://daim.com tabId=26', '2026-01-22 23:36:27'),
                                                                                              (60, 3, 'TAB_CLOSED', 'LRU_EVICT tabId=24', '2026-01-22 23:36:47'),
                                                                                              (61, 3, 'TAB_OPENED', 'url=https://aiman.com tabId=27', '2026-01-22 23:36:47'),
                                                                                              (62, 3, 'TAB_CLOSED', 'LRU_EVICT tabId=25', '2026-01-22 23:36:59'),
                                                                                              (63, 3, 'TAB_OPENED', 'url=https://basit.com tabId=28', '2026-01-22 23:36:59'),
                                                                                              (64, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:37:38'),
                                                                                              (65, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:37:44'),
                                                                                              (66, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:37:46'),
                                                                                              (67, 3, 'SESSION_UPDATED', 'ACCESS tabId=28', '2026-01-22 23:37:50'),
                                                                                              (68, 3, 'SESSION_UPDATED', 'ACCESS tabId=27', '2026-01-22 23:37:58'),
                                                                                              (69, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:38:35'),
                                                                                              (70, 3, 'SESSION_UPDATED', 'UNDO Access tabId=26', '2026-01-22 23:41:33'),
                                                                                              (71, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:41:39'),
                                                                                              (72, 3, 'SESSION_UPDATED', 'UNDO Access tabId=26', '2026-01-22 23:41:47'),
                                                                                              (73, 3, 'SESSION_UPDATED', 'UNDO Access tabId=27', '2026-01-22 23:41:50'),
                                                                                              (74, 3, 'SESSION_UPDATED', 'ACCESS tabId=27', '2026-01-22 23:41:52'),
                                                                                              (75, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:41:53'),
                                                                                              (76, 3, 'TAB_CLOSED', 'EXPIRED tabId=28', '2026-01-22 23:47:51'),
                                                                                              (77, 3, 'SESSION_UPDATED', 'UNDO Access tabId=26', '2026-01-22 23:48:35'),
                                                                                              (78, 3, 'SESSION_UPDATED', 'ACCESS tabId=26', '2026-01-22 23:48:40'),
                                                                                              (79, 3, 'TAB_CLOSED', 'EXPIRED tabId=27', '2026-01-22 23:51:53'),
                                                                                              (80, 3, 'TAB_CLOSED', 'EXPIRED tabId=26', '2026-01-22 23:58:41'),
                                                                                              (81, 7, 'SESSION_CREATED', 'capacity=5', '2026-01-23 08:13:32'),
                                                                                              (82, 7, 'TAB_OPENED', 'url=https://laiba.com tabId=29', '2026-01-23 08:14:42'),
                                                                                              (83, 7, 'TAB_OPENED', 'url=https://aiman.com tabId=30', '2026-01-23 08:15:19'),
                                                                                              (84, 7, 'TAB_OPENED', 'url=http://daim.com tabId=31', '2026-01-23 08:15:35'),
                                                                                              (85, 7, 'TAB_OPENED', 'url=http://basit.pk tabId=32', '2026-01-23 08:16:14'),
                                                                                              (86, 7, 'TAB_OPENED', 'url=http://athar.pk tabId=33', '2026-01-23 08:16:44'),
                                                                                              (87, 7, 'SESSION_UPDATED', 'ACCESS tabId=33', '2026-01-23 08:16:54'),
                                                                                              (88, 7, 'SESSION_UPDATED', 'ACCESS tabId=29', '2026-01-23 08:16:59'),
                                                                                              (89, 7, 'SESSION_UPDATED', 'ACCESS tabId=30', '2026-01-23 08:17:09'),
                                                                                              (90, 7, 'TAB_CLOSED', 'LRU_EVICT tabId=31', '2026-01-23 08:17:48'),
                                                                                              (91, 7, 'TAB_OPENED', 'url=http://gift.edu.pk tabId=34', '2026-01-23 08:17:48'),
                                                                                              (92, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=34', '2026-01-23 08:18:24'),
                                                                                              (93, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=29', '2026-01-23 08:18:34'),
                                                                                              (94, 7, 'SESSION_UPDATED', 'UNDO CloseTab tabId=29', '2026-01-23 08:18:51'),
                                                                                              (95, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=29', '2026-01-23 08:18:58'),
                                                                                              (96, 7, 'SESSION_UPDATED', 'UNDO CloseTab tabId=29', '2026-01-23 08:19:01'),
                                                                                              (97, 7, 'SESSION_UPDATED', 'UNDO CloseTab tabId=34', '2026-01-23 08:19:07'),
                                                                                              (98, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=34', '2026-01-23 08:19:10'),
                                                                                              (99, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=29', '2026-01-23 08:19:11'),
                                                                                              (100, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=30', '2026-01-23 08:20:10'),
                                                                                              (101, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=32', '2026-01-23 08:20:11'),
                                                                                              (102, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=33', '2026-01-23 08:20:11'),
                                                                                              (103, 8, 'SESSION_CREATED', 'capacity=3', '2026-01-23 08:20:34'),
                                                                                              (104, 9, 'SESSION_CREATED', 'capacity=3', '2026-01-23 08:20:38'),
                                                                                              (105, 9, 'TAB_OPENED', 'url=https://Daim.com tabId=35', '2026-01-23 08:21:24'),
                                                                                              (106, 9, 'TAB_OPENED', 'url=https://talha.com tabId=36', '2026-01-23 08:21:51'),
                                                                                              (107, 9, 'TAB_OPENED', 'url=https://ali.com tabId=37', '2026-01-23 08:22:53'),
                                                                                              (108, 9, 'TAB_CLOSED', 'LRU_EVICT tabId=35', '2026-01-23 08:23:14'),
                                                                                              (109, 9, 'TAB_OPENED', 'url=https://abdullah tabId=38', '2026-01-23 08:23:14'),
                                                                                              (110, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=38', '2026-01-23 08:23:21'),
                                                                                              (111, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=37', '2026-01-23 08:23:24'),
                                                                                              (112, 9, 'TAB_OPENED', 'url=https://ali.com tabId=37', '2026-01-23 08:23:28'),
                                                                                              (113, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=37', '2026-01-23 08:23:35'),
                                                                                              (114, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=36', '2026-01-23 08:23:50'),
                                                                                              (115, 9, 'TAB_OPENED', 'url=https://talha.com tabId=36', '2026-01-23 08:23:54'),
                                                                                              (116, 9, 'TAB_OPENED', 'url=https://ali.com tabId=37', '2026-01-23 08:24:18'),
                                                                                              (117, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=37', '2026-01-23 08:24:26'),
                                                                                              (118, 9, 'SESSION_UPDATED', 'UNDO OpenTab tabId=36', '2026-01-23 08:24:35'),
                                                                                              (119, 9, 'TAB_OPENED', 'url=https://talha.com tabId=36', '2026-01-23 08:24:46'),
                                                                                              (120, 9, 'TAB_OPENED', 'url=https://ali.com tabId=37', '2026-01-23 08:24:58'),
                                                                                              (121, 9, 'TAB_CLOSED', 'LRU_EVICT tabId=35', '2026-01-23 08:24:59'),
                                                                                              (122, 9, 'TAB_OPENED', 'url=https://abdullah tabId=38', '2026-01-23 08:24:59'),
                                                                                              (123, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=36', '2026-01-23 08:25:52'),
                                                                                              (124, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=37', '2026-01-23 08:25:55'),
                                                                                              (125, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=38', '2026-01-23 08:25:57'),
                                                                                              (126, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=38', '2026-01-23 08:26:04'),
                                                                                              (127, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=37', '2026-01-23 08:26:08'),
                                                                                              (128, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=36', '2026-01-23 08:26:09'),
                                                                                              (129, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=36', '2026-01-23 08:26:15'),
                                                                                              (130, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=37', '2026-01-23 08:26:16'),
                                                                                              (131, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=38', '2026-01-23 08:26:17'),
                                                                                              (132, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=38', '2026-01-23 08:26:20'),
                                                                                              (133, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=37', '2026-01-23 08:26:21'),
                                                                                              (134, 9, 'SESSION_UPDATED', 'UNDO CloseTab tabId=36', '2026-01-23 08:26:23'),
                                                                                              (135, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=38', '2026-01-23 08:27:13'),
                                                                                              (136, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=36', '2026-01-23 08:27:17'),
                                                                                              (137, 9, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=37', '2026-01-23 08:27:18'),
                                                                                              (138, 7, 'TAB_OPENED', 'url=https://laiba.com tabId=39', '2026-01-23 11:40:49'),
                                                                                              (139, 7, 'TAB_OPENED', 'url=https://B.com tabId=40', '2026-01-23 11:41:04'),
                                                                                              (140, 7, 'TAB_OPENED', 'url=https://C.com tabId=41', '2026-01-23 11:41:12'),
                                                                                              (141, 7, 'TAB_OPENED', 'url=https://C.com tabId=42', '2026-01-23 11:41:19'),
                                                                                              (142, 7, 'SESSION_UPDATED', 'ACCESS tabId=39', '2026-01-23 11:41:24'),
                                                                                              (143, 7, 'TAB_CLOSED', 'EXPIRED tabId=40', '2026-01-23 11:51:05'),
                                                                                              (144, 7, 'TAB_CLOSED', 'EXPIRED tabId=41', '2026-01-23 11:51:12'),
                                                                                              (145, 7, 'TAB_CLOSED', 'EXPIRED tabId=42', '2026-01-23 11:51:20'),
                                                                                              (146, 7, 'TAB_CLOSED', 'EXPIRED tabId=39', '2026-01-23 11:51:25'),
                                                                                              (147, 7, 'TAB_OPENED', 'url=http://G.com tabId=43', '2026-01-23 14:47:06'),
                                                                                              (148, 7, 'TAB_OPENED', 'url=https://laiba.pk tabId=44', '2026-01-23 14:47:20'),
                                                                                              (149, 7, 'TAB_OPENED', 'url=https://zarnub.pk tabId=45', '2026-01-23 14:47:30'),
                                                                                              (150, 7, 'TAB_CLOSED', 'MANUAL_CLOSE tabId=43', '2026-01-23 14:58:06'),
                                                                                              (151, 10, 'SESSION_CREATED', 'capacity=4', '2026-01-23 17:29:37'),
                                                                                              (152, 10, 'TAB_OPENED', 'url=https://rida.com tabId=46', '2026-01-23 17:29:56'),
                                                                                              (153, 10, 'TAB_OPENED', 'url=http://noor.com tabId=47', '2026-01-23 17:30:06'),
                                                                                              (154, 10, 'TAB_OPENED', 'url=https://hadia.pk tabId=48', '2026-01-23 17:30:16'),
                                                                                              (155, 10, 'TAB_OPENED', 'url=https://kinza.pk tabId=49', '2026-01-23 17:30:39'),
                                                                                              (156, 10, 'SESSION_UPDATED', 'ACCESS tabId=46', '2026-01-23 17:30:52'),
                                                                                              (157, 10, 'TAB_CLOSED', 'LRU_EVICT tabId=47', '2026-01-23 17:31:31'),
                                                                                              (158, 10, 'TAB_OPENED', 'url=https://sara.pk tabId=50', '2026-01-23 17:31:31'),
                                                                                              (159, 10, 'SESSION_UPDATED', 'UNDO OpenTab tabId=50', '2026-01-23 17:31:55'),
                                                                                              (160, 10, 'TAB_CLOSED', 'LRU_EVICT tabId=47', '2026-01-23 17:31:58'),
                                                                                              (161, 10, 'TAB_OPENED', 'url=https://sara.pk tabId=50', '2026-01-23 17:31:59'),
                                                                                              (162, 10, 'TAB_CLOSED', 'EXPIRED tabId=48', '2026-01-23 17:40:16'),
                                                                                              (163, 10, 'TAB_CLOSED', 'EXPIRED tabId=49', '2026-01-23 17:40:40'),
                                                                                              (164, 10, 'TAB_CLOSED', 'EXPIRED tabId=46', '2026-01-23 17:40:53'),
                                                                                              (165, 10, 'TAB_CLOSED', 'EXPIRED tabId=50', '2026-01-23 17:41:59'),
                                                                                              (166, 10, 'TAB_OPENED', 'url=http://gift.edu.pk tabId=51', '2026-02-08 23:45:06'),
                                                                                              (167, 10, 'TAB_OPENED', 'url=https://lms.gift.edu.pk tabId=52', '2026-02-08 23:45:27'),
                                                                                              (168, 10, 'TAB_OPENED', 'url=https://laiba.pk tabId=53', '2026-02-08 23:45:41'),
                                                                                              (169, 10, 'TAB_CLOSED', 'EXPIRED tabId=51', '2026-02-08 23:55:06'),
                                                                                              (170, 10, 'TAB_CLOSED', 'EXPIRED tabId=52', '2026-02-08 23:55:27'),
                                                                                              (171, 10, 'TAB_CLOSED', 'EXPIRED tabId=53', '2026-02-08 23:55:42');

-- --------------------------------------------------------

--
-- Table structure for table `eviction_log`
--

CREATE TABLE `eviction_log` (
                                `eviction_id` int(11) NOT NULL,
                                `session_id` int(11) NOT NULL,
                                `tab_id` int(11) NOT NULL,
                                `reason` enum('LRU_LIMIT_REACHED','SESSION_EXPIRED','MANUAL_CLOSE') NOT NULL,
                                `evicted_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `eviction_log`
--

INSERT INTO `eviction_log` (`eviction_id`, `session_id`, `tab_id`, `reason`, `evicted_at`) VALUES
                                                                                               (5, 2, 11, 'LRU_LIMIT_REACHED', '2026-01-19 03:34:16'),
                                                                                               (6, 2, 13, 'LRU_LIMIT_REACHED', '2026-01-19 03:34:19'),
                                                                                               (7, 2, 14, 'LRU_LIMIT_REACHED', '2026-01-19 03:34:22'),
                                                                                               (8, 3, 18, 'LRU_LIMIT_REACHED', '2026-01-19 03:42:24'),
                                                                                               (9, 3, 21, 'MANUAL_CLOSE', '2026-01-22 23:30:27'),
                                                                                               (10, 3, 19, 'MANUAL_CLOSE', '2026-01-22 23:34:25'),
                                                                                               (11, 3, 20, 'LRU_LIMIT_REACHED', '2026-01-22 23:35:51'),
                                                                                               (12, 3, 24, 'LRU_LIMIT_REACHED', '2026-01-22 23:36:47'),
                                                                                               (13, 3, 25, 'LRU_LIMIT_REACHED', '2026-01-22 23:36:59'),
                                                                                               (14, 7, 31, 'LRU_LIMIT_REACHED', '2026-01-23 08:17:48'),
                                                                                               (15, 7, 34, 'MANUAL_CLOSE', '2026-01-23 08:18:24'),
                                                                                               (16, 7, 29, 'MANUAL_CLOSE', '2026-01-23 08:18:34'),
                                                                                               (17, 7, 29, 'MANUAL_CLOSE', '2026-01-23 08:18:58'),
                                                                                               (18, 7, 34, 'MANUAL_CLOSE', '2026-01-23 08:19:10'),
                                                                                               (19, 7, 29, 'MANUAL_CLOSE', '2026-01-23 08:19:11'),
                                                                                               (20, 7, 30, 'MANUAL_CLOSE', '2026-01-23 08:20:10'),
                                                                                               (21, 7, 32, 'MANUAL_CLOSE', '2026-01-23 08:20:11'),
                                                                                               (22, 7, 33, 'MANUAL_CLOSE', '2026-01-23 08:20:11'),
                                                                                               (23, 9, 35, 'LRU_LIMIT_REACHED', '2026-01-23 08:23:14'),
                                                                                               (24, 9, 35, 'LRU_LIMIT_REACHED', '2026-01-23 08:24:59'),
                                                                                               (25, 9, 36, 'MANUAL_CLOSE', '2026-01-23 08:25:52'),
                                                                                               (26, 9, 37, 'MANUAL_CLOSE', '2026-01-23 08:25:55'),
                                                                                               (27, 9, 38, 'MANUAL_CLOSE', '2026-01-23 08:25:57'),
                                                                                               (28, 9, 36, 'MANUAL_CLOSE', '2026-01-23 08:26:15'),
                                                                                               (29, 9, 37, 'MANUAL_CLOSE', '2026-01-23 08:26:16'),
                                                                                               (30, 9, 38, 'MANUAL_CLOSE', '2026-01-23 08:26:17'),
                                                                                               (31, 9, 38, 'MANUAL_CLOSE', '2026-01-23 08:27:13'),
                                                                                               (32, 9, 36, 'MANUAL_CLOSE', '2026-01-23 08:27:17'),
                                                                                               (33, 9, 37, 'MANUAL_CLOSE', '2026-01-23 08:27:18'),
                                                                                               (34, 7, 43, 'MANUAL_CLOSE', '2026-01-23 14:58:06'),
                                                                                               (35, 10, 47, 'LRU_LIMIT_REACHED', '2026-01-23 17:31:31'),
                                                                                               (36, 10, 47, 'LRU_LIMIT_REACHED', '2026-01-23 17:31:58');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
                            `session_id` int(11) NOT NULL,
                            `created_at` datetime NOT NULL DEFAULT current_timestamp(),
                            `last_active_at` datetime NOT NULL DEFAULT current_timestamp(),
                            `status` enum('ACTIVE','INACTIVE','EXPIRED') NOT NULL DEFAULT 'ACTIVE',
                            `max_capacity` int(11) NOT NULL,
                            `expires_at` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`session_id`, `created_at`, `last_active_at`, `status`, `max_capacity`, `expires_at`) VALUES
                                                                                                                  (1, '2026-01-19 02:02:47', '2026-01-19 02:05:30', 'ACTIVE', 3, '2026-01-19 02:35:30'),
                                                                                                                  (2, '2026-01-19 02:45:55', '2026-01-19 03:34:22', 'ACTIVE', 3, '2026-01-19 04:04:22'),
                                                                                                                  (3, '2026-01-19 03:40:55', '2026-01-22 23:48:40', 'ACTIVE', 3, '2026-01-23 00:18:40'),
                                                                                                                  (4, '2026-01-21 00:54:12', '2026-01-21 00:54:41', 'ACTIVE', 4, '2026-01-21 01:24:41'),
                                                                                                                  (5, '2026-01-22 15:24:46', '2026-01-22 15:25:19', 'ACTIVE', 5, '2026-01-22 15:55:19'),
                                                                                                                  (6, '2026-01-22 23:30:31', '2026-01-22 23:30:31', 'ACTIVE', 5, '2026-01-23 00:00:31'),
                                                                                                                  (7, '2026-01-23 08:13:32', '2026-01-23 14:47:30', 'ACTIVE', 5, '2026-01-23 15:17:30'),
                                                                                                                  (8, '2026-01-23 08:20:34', '2026-01-23 08:20:34', 'ACTIVE', 3, '2026-01-23 08:50:34'),
                                                                                                                  (9, '2026-01-23 08:20:38', '2026-01-23 08:24:59', 'ACTIVE', 3, '2026-01-23 08:54:59'),
                                                                                                                  (10, '2026-01-23 17:29:37', '2026-02-08 23:45:41', 'ACTIVE', 4, '2026-02-09 00:15:41');

-- --------------------------------------------------------

--
-- Table structure for table `tabs`
--

CREATE TABLE `tabs` (
                        `tab_id` int(11) NOT NULL,
                        `session_id` int(11) NOT NULL,
                        `created_at` datetime NOT NULL DEFAULT current_timestamp(),
                        `url` varchar(255) NOT NULL,
                        `is_active` tinyint(1) NOT NULL DEFAULT 1,
                        `last_access_at` datetime NOT NULL DEFAULT current_timestamp(),
                        `expires_at` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `tabs`
--

INSERT INTO `tabs` (`tab_id`, `session_id`, `created_at`, `url`, `is_active`, `last_access_at`, `expires_at`) VALUES
                                                                                                                  (15, 2, '2026-01-19 03:34:16', 'https://f.com', 0, '2026-01-19 03:34:16', '2026-01-19 03:44:16'),
                                                                                                                  (16, 2, '2026-01-19 03:34:19', 'https://f.com', 0, '2026-01-19 03:34:19', '2026-01-19 03:44:19'),
                                                                                                                  (17, 2, '2026-01-19 03:34:22', 'https://f.com', 1, '2026-01-19 03:34:22', '2026-01-19 03:44:22'),
                                                                                                                  (22, 4, '2026-01-21 00:54:41', 'http://laiba.com', 1, '2026-01-21 00:54:41', '2026-01-21 01:04:41'),
                                                                                                                  (44, 7, '2026-01-23 14:47:19', 'https://laiba.pk', 0, '2026-01-23 14:47:19', '2026-01-23 14:57:19'),
                                                                                                                  (45, 7, '2026-01-23 14:47:30', 'https://zarnub.pk', 1, '2026-01-23 14:47:30', '2026-01-23 14:57:30');

-- --------------------------------------------------------

--
-- Table structure for table `tab_access_log`
--

CREATE TABLE `tab_access_log` (
                                  `access_id` int(11) NOT NULL,
                                  `tab_id` int(11) NOT NULL,
                                  `access_time` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tab_access_log`
--

INSERT INTO `tab_access_log` (`access_id`, `tab_id`, `access_time`) VALUES
                                                                        (18, 15, '2026-01-19 03:34:16'),
                                                                        (19, 16, '2026-01-19 03:34:19'),
                                                                        (20, 17, '2026-01-19 03:34:22'),
                                                                        (25, 22, '2026-01-21 00:54:41'),
                                                                        (76, 44, '2026-01-23 14:47:19'),
                                                                        (77, 45, '2026-01-23 14:47:30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity_log`
--
ALTER TABLE `activity_log`
    ADD PRIMARY KEY (`log_id`),
  ADD KEY `fk_activitylog_session` (`session_id`);

--
-- Indexes for table `eviction_log`
--
ALTER TABLE `eviction_log`
    ADD PRIMARY KEY (`eviction_id`),
  ADD KEY `fk_eviction_session` (`session_id`),
  ADD KEY `idx_eviction_tab_id` (`tab_id`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
    ADD PRIMARY KEY (`session_id`),
  ADD KEY `idx_sessions_expires` (`expires_at`);

--
-- Indexes for table `tabs`
--
ALTER TABLE `tabs`
    ADD PRIMARY KEY (`tab_id`),
  ADD KEY `idx_tabs_session_active` (`session_id`,`is_active`),
  ADD KEY `idx_tabs_expires` (`expires_at`);

--
-- Indexes for table `tab_access_log`
--
ALTER TABLE `tab_access_log`
    ADD PRIMARY KEY (`access_id`),
  ADD KEY `idx_accesslog_tab_time` (`tab_id`,`access_time`),
  ADD KEY `idx_accesslog_time` (`access_time`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity_log`
--
ALTER TABLE `activity_log`
    MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `eviction_log`
--
ALTER TABLE `eviction_log`
    MODIFY `eviction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
    MODIFY `session_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tabs`
--
ALTER TABLE `tabs`
    MODIFY `tab_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tab_access_log`
--
ALTER TABLE `tab_access_log`
    MODIFY `access_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `activity_log`
--
ALTER TABLE `activity_log`
    ADD CONSTRAINT `fk_activitylog_session` FOREIGN KEY (`session_id`) REFERENCES `sessions` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `eviction_log`
--
ALTER TABLE `eviction_log`
    ADD CONSTRAINT `fk_eviction_session` FOREIGN KEY (`session_id`) REFERENCES `sessions` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tabs`
--
ALTER TABLE `tabs`
    ADD CONSTRAINT `fk_tabs_session` FOREIGN KEY (`session_id`) REFERENCES `sessions` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tab_access_log`
--
ALTER TABLE `tab_access_log`
    ADD CONSTRAINT `fk_accesslog_tab` FOREIGN KEY (`tab_id`) REFERENCES `tabs` (`tab_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
