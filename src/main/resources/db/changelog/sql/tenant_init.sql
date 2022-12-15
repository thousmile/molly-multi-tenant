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
-- Records of  sys_template
-- ----------------------------

INSERT INTO `sys_template` (`id`, `name`, `description`, `create_time`, `create_user`, `last_update_time`,
                            `last_update_user`)
VALUES (10001, '全部模块', '全部模块', '2022-06-10 22:58:39', 19980817, '2022-06-10 22:58:39',
        19980817);


-- ----------------------------
-- Records of sys_tenant
-- ----------------------------

INSERT INTO `sys_tenant` (`tenant_id`, `create_time`, `create_user`, `last_update_time`,
                          `last_update_user`, `logo`, `name`, `email`, `linkman`,
                          `contact_number`, `area_code`, `address`, `status`, `expired`)
VALUES ('master', '2022-12-12 10:07:26', 19980817, '2022-12-12 10:07:32', 19980817,
        'https://images.xaaef.com/molly_master_logo.png', 'master', 'master@qq.com', 'master',
        '0755-1234567', 440307000000, '平湖街道 城市山海大厦', 1, '6666-12-12 00:00:00');


-- ----------------------------
-- Records of sys_tenant_template
-- ----------------------------

INSERT INTO `sys_tenant_template` (`tenant_id`, `template_id`)
VALUES ('master', 10001);


SET FOREIGN_KEY_CHECKS = 1;
