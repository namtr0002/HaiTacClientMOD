/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 100432
 Source Host           : localhost:3306
 Source Schema         : haitacgalaxy

 Target Server Type    : MySQL
 Target Server Version : 100432
 File Encoding         : 65001

 Date: 18/05/2026 19:39:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_index` smallint NULL DEFAULT NULL,
  `id_2` smallint NULL DEFAULT NULL,
  `icon` smallint NULL DEFAULT NULL,
  `typeSkill` tinyint NULL DEFAULT NULL,
  `typeBuff` tinyint NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `typeEffSkill` smallint NULL DEFAULT NULL,
  `range` smallint NULL DEFAULT NULL,
  `rangeLan` smallint NULL DEFAULT NULL,
  `nTarget` tinyint NULL DEFAULT NULL,
  `damage` int NULL DEFAULT NULL,
  `manaLost` smallint NULL DEFAULT NULL,
  `timeDelay` int NULL DEFAULT NULL,
  `nKick` tinyint NULL DEFAULT NULL,
  `info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Lv_RQ` tinyint NULL DEFAULT NULL,
  `percentLv` smallint NULL DEFAULT NULL,
  `typeDevil` tinyint NULL DEFAULT NULL,
  `option` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EffSpec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LvDevilSkill` tinyint NULL DEFAULT NULL,
  `phanTramDevilSkill` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4111 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
