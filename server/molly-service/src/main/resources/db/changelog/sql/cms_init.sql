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
INSERT INTO `cms_project` (`project_id`, `project_name`, `linkman`, `contact_number`, `area_code`, `address`,
                           `sort`, `password`, `status`, `dept_id`, `create_time`, `create_user`,
                           `last_update_time`, `last_update_user`)
VALUES
(10001, '默认项目', '李默认', '0755-1234567', 440307000000, '南湾街道 左右云创谷A栋1座', 1, '$2a$10$omnFqGPZ3/pMfctkVkxiQOYMUC8pp1iNsaJDI8vQmLdtIdAXvlS8K', 1, 10002, '2023-08-11 11:17:33', 19980817, '2023-08-11 10:12:24', 19980817),
(10002, '南北水调工程', '刘南北', '0755-1234568', 440307000000, '平湖街道 南北水调工程运营大楼', 1, '$2a$10$omnFqGPZ3/pMfctkVkxiQOYMUC8pp1iNsaJDI8vQmLdtIdAXvlS8K', 1, 10003, '2023-08-11 11:17:33', 19980817, '2023-08-11 10:12:24', 19980817),
(10003, '西气东输工程', '王西东', '0755-1234569', 440307000000, '龙城街道 西气东输起点部门', 1, '$2a$10$omnFqGPZ3/pMfctkVkxiQOYMUC8pp1iNsaJDI8vQmLdtIdAXvlS8K', 1, 10004, '2023-08-11 11:17:33', 19980817, '2023-08-11 10:12:24', 19980817);


-- ----------------------------
-- Records of cms_device
-- ----------------------------
INSERT INTO `cms_device` (`device_id`, `device_name`, `status`, `project_id`, `create_time`, `create_user`, `last_update_time`, `last_update_user`)
VALUES
(6569649088961, '喷气式飞机', 1, 10001, '2023-11-08 18:07:23', 19980817, '2023-11-08 18:07:23', 19980817),
(6569649088962, '大型运输机', 1, 10001, '2023-11-08 18:07:23', 19980817, '2023-11-08 18:07:23', 19980817),
(6569649151233, '挖掘机', 1, 10002, '2023-11-08 18:07:27', 19980817, '2023-11-08 18:07:27', 19980817),
(6569649151234, '抽水泵', 1, 10002, '2023-11-08 18:07:27', 19980817, '2023-11-08 18:07:27', 19980817),
(6569649151235, '燃气管道', 1, 10003, '2023-11-08 18:07:27', 19980817, '2023-11-08 18:07:27', 19980817),
(6569649151236, '塔吊', 1, 10003, '2023-11-08 18:07:27', 19980817, '2023-11-08 18:07:27', 19980817),
(6569649223207, '推土机', 1, 10003, '2023-11-08 18:07:31', 19980817, '2023-11-08 18:07:31', 19980817);


SET
FOREIGN_KEY_CHECKS = 1;
