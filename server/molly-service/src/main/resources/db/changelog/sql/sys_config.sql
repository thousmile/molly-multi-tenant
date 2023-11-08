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
-- Records of sys_config
-- ----------------------------

INSERT INTO `sys_config` VALUES (10001, '用户管理-账号初始密码', 'user_default_password', '123456', 1, '2021-07-14 11:48:16', 19980817, '2021-07-14 11:48:16', 19980817);
INSERT INTO `sys_config` VALUES (10002, '租户默认logo地址', 'default_tenant_logo', 'https://oss.mhtled.com/avatar/275753151968186368.png', 1, '2021-07-16 14:28:41', 19980817, '2022-09-19 14:47:26', 19980817);
INSERT INTO `sys_config` VALUES (10003, '租户默认角色名称', 'default_role_name', '操作员', 1, '2021-07-16 17:26:56', 19980817, '2021-07-16 17:27:06', 19980817);
INSERT INTO `sys_config` VALUES (10004, '获取法定节假日的接口', 'get_holiday_url', 'https://timor.tech/api/holiday/year/%s?type=N&week=Y', 1, '2022-05-16 11:50:20', 19980817, '2022-05-16 11:50:25', 19980817);


SET FOREIGN_KEY_CHECKS = 1;
