/*
 Navicat Premium Data Transfer

 Source Server         : my-mysql
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : molly_master

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 15/12/2022 10:30:32
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of cms_project
-- ----------------------------
INSERT INTO `cms_project` (`project_id`, `project_name`, `sort`, `leader`, `leader_mobile`, `address`,
                           `lng`, `lat`, `password`, `status`, `create_time`, `create_user`,
                           `last_update_time`, `last_update_user`)
VALUES (10001, '默认项目', 1, '李默认', '15684949999', '广东省/深圳市/龙岗区 平朗路8号 左右云创谷',
        22.656982, 114.137067, '123456', 1, '2023-08-11 11:17:33', 19980817,
        '2023-08-11 10:12:24', 19980817);


SET
FOREIGN_KEY_CHECKS = 1;
