SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `pet_template`;
CREATE TABLE `pet_template` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `icon` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `frame` int DEFAULT NULL,
  `op` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `pet_template` VALUES (1, 'White Zeus', 703, 4, 77, '[[1, 150], [10, 42], [11, 200], [53, 120], [63, 120]]');
INSERT INTO `pet_template` VALUES (2, 'Black Zeus', 704, 4, 78, '[[46,150],[13,150],[56,150],[51,150],[78,100]]');
INSERT INTO `pet_template` VALUES (3, 'Tiểu Merry', 705, 5, 79, '[[67, 1000], [68, 1000], [72, 500], [25, 100], [12, 26]]');
INSERT INTO `pet_template` VALUES (4, 'Tiểu Sunny', 706, 5, 80, '[[1,120],[4,100],[17,150],[53,100],[67,500]]');
INSERT INTO `pet_template` VALUES (5, 'Capybara', 709, 5, 83, '[[56, 200], [53, 150], [14, 30], [47, 100], [54, 80]]');
INSERT INTO `pet_template` VALUES (6, 'Loopy', 710, 5, 84, '[[62,150],[27,150],[25,120],[72,800],[68,800]]');
INSERT INTO `pet_template` VALUES (7, 'Lucci Mini', 711, 5, 85, '[[1, 150], [10, 42], [11, 200], [13, 150], [51, 120]]');
INSERT INTO `pet_template` VALUES (8, 'Choper giáng sinh', 712, 5, 809, '[[17,150],[56,120],[23,150],[47,100],[67,800]]');
INSERT INTO `pet_template` VALUES (9, 'Rắn Quý Tỵ', 713, 5, 810, '[[12, 40], [13, 150], [14, 30], [59, 80], [52, 120]]');
INSERT INTO `pet_template` VALUES (10, 'Ma xà', 712, 2, 807, '[[1,130],[13,130],[51,150],[75,80],[56,100]]');
INSERT INTO `pet_template` VALUES (11, 'Thủy Quái', 713, 2, 808, '[[56, 180], [53, 130], [26, 150], [14, 30], [59, 70]]');
INSERT INTO `pet_template` VALUES (12, 'Thần Hỏa Prometheus', 702, 4, 111, '[[53,110],[63,110],[56,100],[51,150],[11,150]]');
INSERT INTO `pet_template` VALUES (13, 'Pokemon Lửa', -1, 0, 39, '[[1, 120], [10, 28], [11, 150], [0, 500]]');
INSERT INTO `pet_template` VALUES (14, 'Pokemon Cây', -1, 0, 40, '[[17,120],[56,100],[47,100],[15,3000]]');
INSERT INTO `pet_template` VALUES (15, 'Pokemon Đá', -1, 0, 41, '[[4,120],[26,120],[53,100],[3,400]]');
INSERT INTO `pet_template` VALUES (16, 'Pokemon Nước', -1, 0, 42, '[[17,100],[18,100],[27,120],[59,60]]');
INSERT INTO `pet_template` VALUES (17, 'Pokemon Điện', -1, 0, 43, '[[25, 120], [10, 33], [12, 26], [75, 60]]');
INSERT INTO `pet_template` VALUES (18, 'Meow Two Thức Tỉnh', -1, 0, 45, '[[46,200],[1,200],[63,180],[53,180],[11,250],[56,180]]');
INSERT INTO `pet_template` VALUES (19, 'Dơi', -1, 0, 50, '[[59, 80], [12, 26], [13, 100], [0, 400]]');
INSERT INTO `pet_template` VALUES (20, 'Hồn Ma Trắng', -1, 0, 51, '[[12, 32], [51, 120], [49, 100], [25, 80]]');
INSERT INTO `pet_template` VALUES (21, 'Hồn Ma Vàng', -1, 0, 52, '[[12, 40], [52, 120], [63, 100], [25, 100]]');
INSERT INTO `pet_template` VALUES (22, 'Chó Đốm', -1, 0, 53, '[[67,600],[68,600],[1,80],[17,80]]');
INSERT INTO `pet_template` VALUES (23, 'Chó Vàng', -1, 0, 54, '[[72,800],[67,600],[62,100],[5,10]]');
INSERT INTO `pet_template` VALUES (24, 'Chó Cảnh', -1, 0, 55, '[[68,800],[19,150],[20,100],[23,100]]');
INSERT INTO `pet_template` VALUES (25, 'Husky', -1, 0, 56, '[[25, 100], [12, 26], [1, 100], [10, 22]]');
INSERT INTO `pet_template` VALUES (26, 'Merry', 705, 0, 79, '[[67,800],[68,800],[17,100],[4,80]]');
INSERT INTO `pet_template` VALUES (27, 'Sunny', 706, 0, 80, '[[1,120],[13,120],[56,100],[53,100]]');
INSERT INTO `pet_template` VALUES (28, 'Capybara', 709, 0, 83, '[[56, 150], [53, 100], [14, 24], [47, 80]]');
INSERT INTO `pet_template` VALUES (29, 'PipiPopa', 710, 0, 84, '[[62, 120], [67, 700], [72, 600], [12, 26]]');
INSERT INTO `pet_template` VALUES (30, 'Báo Đốm', 711, 0, 85, '[[1, 130], [10, 33], [13, 120], [25, 80]]');
INSERT INTO `pet_template` VALUES (31, 'Chopper Giáng Sinh', 712, 0, 86, '[[17,120],[23,120],[47,80],[67,700]]');
INSERT INTO `pet_template` VALUES (32, 'Rắn Tết', 713, 0, 87, '[[72, 700], [12, 32], [13, 100], [62, 100]]');
INSERT INTO `pet_template` VALUES (33, 'Quạ Đen', 714, 0, 88, '[[51, 120], [10, 28], [12, 26], [1, 80]]');
INSERT INTO `pet_template` VALUES (34, 'Thỏ Ngọc', -1, 0, 89, '[[25, 100], [12, 32], [18, 100], [19, 100]]');
INSERT INTO `pet_template` VALUES (35, 'Thỏ Sát Thủ', 716, 0, 90, '[[1, 140], [10, 39], [11, 180], [46, 100]]');
INSERT INTO `pet_template` VALUES (36, 'Chopper Halloween', 717, 0, 91, '[[59,70],[11,150],[75,60],[47,80]]');
INSERT INTO `pet_template` VALUES (37, 'Bánh Gấu', 718, 0, 92, '[[17,120],[23,150],[4,100],[26,100]]');
INSERT INTO `pet_template` VALUES (38, 'Rồng Tết', 719, 0, 93, '[[1,200],[46,180],[53,180],[63,180],[56,180],[54,80]]');
INSERT INTO `pet_template` VALUES (39, 'Mã Đáo Thành Công', 720, 0, 94, '[[25,120],[72,800],[67,800],[1,100],[62,100]]');
INSERT INTO `pet_template` VALUES (40, 'Homie Zeus', -1, 0, 98, '[[1, 150], [10, 42], [13, 120], [78, 80], [56, 100]]');
INSERT INTO `pet_template` VALUES (41, 'Cào Cào', -1, 0, 203, '[[25, 80], [12, 21], [68, 500]]');
INSERT INTO `pet_template` VALUES (42, 'Dế Mèn', -1, 0, 204, '[[14, 24], [17, 80], [67, 500]]');
INSERT INTO `pet_template` VALUES (43, 'Quạ Đen', -1, 0, 205, '[[51,100],[13,100],[1,80]]');
INSERT INTO `pet_template` VALUES (44, 'Thỏ Sát Thủ', -1, 0, 206, '[[10, 33], [11, 150], [52, 100]]');
INSERT INTO `pet_template` VALUES (45, 'Bánh Gấu Giáng Sinh', -1, 0, 208, '[[17,100],[19,120],[47,80],[72,500]]');
INSERT INTO `pet_template` VALUES (46, 'Ngựa Sát Thủ', -1, 0, 209, '[[46,120],[13,120],[51,100],[1,100]]');
INSERT INTO `pet_template` VALUES (47, 'Meow Two', -1, 0, 210, '[[1,160],[13,150],[25,120],[27,150],[46,120]]');
INSERT INTO `pet_template` VALUES (48, 'Solgaleo', -1, 0, 211, '[[1, 180], [56, 160], [53, 160], [63, 160], [10, 42]]');
INSERT INTO `pet_template` VALUES (49, 'Dusk Mane Necrozma', -1, 0, 212, '[[46,180],[57,120],[13,160],[51,160],[63,160],[53,150]]');
INSERT INTO `pet_template` VALUES (50, 'Thần Tình Yêu', -1, 0, 976, '[[56,150],[53,120],[25,120],[69,120],[19,150]]');
INSERT INTO `pet_template` VALUES (51, 'Luffy Mèo', 605, 0, 977, '[[1, 180], [10, 45], [17, 160], [13, 150], [46, 140]]');
INSERT INTO `pet_template` VALUES (52, 'Linh Vật WorldCup', 604, 0, 979, '[[12, 40], [25, 140], [67, 800], [72, 800], [62, 120]]');
INSERT INTO `pet_template` VALUES (53, 'Tuần Lộc', -1, 0, 984, '[[17,140],[23,140],[47,100],[67,800],[19,120]]');
INSERT INTO `pet_template` VALUES (54, 'Thỏ Ngọc', -1, 0, 985, '[[25, 120], [12, 40], [56, 120], [53, 100], [69, 100]]');
INSERT INTO `pet_template` VALUES (55, 'Chopper', -1, 0, 998, '[[56,180],[53,150],[47,120],[23,180],[19,200],[54,60]]');
