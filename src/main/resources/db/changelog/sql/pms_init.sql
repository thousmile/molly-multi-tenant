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
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of pms_dept
-- ----------------------------
INSERT INTO `pms_dept` (`dept_id`, `parent_id`, `dept_name`, `leader`, `leader_mobile`, `ancestors`, `sort`,
                        `description`, `create_time`, `create_user`, `last_update_time`, `last_update_user`)
VALUES (10001, 0, '集团', '张集团', '15066666666', '0', 1, '集团',
        '2022-06-10 23:07:51', 19980817, '2022-06-10 23:07:51', 19980817);


-- ----------------------------
-- Records of pms_role
-- ----------------------------
INSERT INTO `pms_role` (`role_id`, `role_name`, `sort`, `description`, `create_time`, `create_user`,
                        `last_update_time`, `last_update_user`)
VALUES (10001, '管理员', 1, '管理员', '2022-06-10 23:11:09', 19980817, '2022-06-10 23:11:09',
        19980817);


-- ----------------------------
-- Records of  pms_user
-- ----------------------------
INSERT INTO `pms_user` (`user_id`, `avatar`, `username`, `mobile`, `email`, `nickname`, `password`,
                        `gender`, `dept_id`, `status`, `admin_flag`, `expired`, `create_time`,
                        `create_user`, `last_update_time`, `last_update_user`)
VALUES (19980817, 'https://oss.mhtled.com/image/mhtled_logo.png', 'admin', '15066666666',
        'admin@qq.com', '超级管理员', '$2a$10$jU7Bg8YLLeF.eoGOzSuZE./ns.lctGOQBX0RTXvhy3HXv8RikUaka', 1,
        10001, 1, 1, NULL, '2021-05-14 11:17:33', 19980817, '2021-12-17 10:12:24', 19980817);


-- ----------------------------
-- Records of pms_user_role
-- ----------------------------
INSERT INTO `pms_user_role` (`user_id`, `role_id`)
VALUES (19980817, 10001);


SET FOREIGN_KEY_CHECKS = 1;
