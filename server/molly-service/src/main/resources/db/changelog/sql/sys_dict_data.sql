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

 Date: 15/12/2022 10:30:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (10001, 1, '男', '1', 'sys_user_sex', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10002, 2, '女', '0', 'sys_user_sex', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10003, 3, '未知', '2', 'sys_user_sex', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2021-07-27 16:29:10');
INSERT INTO `sys_dict_data` VALUES (10004, 1, '显示', '1', 'sys_show_hide', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10005, 2, '隐藏', '0', 'sys_show_hide', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10006, 1, '正常', '1', 'sys_normal_disable', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10007, 2, '停用', '0', 'sys_normal_disable', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10008, 1, '正常', '1', 'sys_job_status', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10009, 2, '暂停', '0', 'sys_job_status', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10010, 1, '默认', 'DEFAULT', 'sys_job_group', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10011, 2, '系统', 'SYSTEM', 'sys_job_group', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10012, 1, '是', '1', 'sys_yes_no', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10013, 2, '否', '0', 'sys_yes_no', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10014, 1, '通知', '1', 'sys_notice_type', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10015, 2, '公告', '2', 'sys_notice_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10016, 1, '正常', '1', 'sys_notice_status', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10017, 2, '关闭', '0', 'sys_notice_status', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10018, 1, '新增', '1', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10019, 2, '修改', '2', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10020, 3, '删除', '3', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10021, 4, '授权', '4', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10022, 5, '导出', '5', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10023, 6, '导入', '6', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10024, 7, '强退', '7', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10025, 8, '生成代码', '8', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10026, 9, '清空数据', '9', 'sys_oper_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10027, 1, '成功', '0', 'sys_common_status', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10028, 2, '失败', '1', 'sys_common_status', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10029, 0, '离线', '0', 'sys_fault_type', 0, 19980817, '2020-07-01 11:31:39', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10030, 1, '开关异常', '1', 'sys_fault_type', 0, 19980817, '2020-07-01 11:32:16', 19980817, '2020-07-01 11:33:33');
INSERT INTO `sys_dict_data` VALUES (10031, 2, '电流异常', '2', 'sys_fault_type', 0, 19980817, '2020-07-01 11:32:38', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10032, 3, '电压异常', '3', 'sys_fault_type', 0, 19980817, '2020-07-01 11:33:02', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10033, 1, '菜单', '0', 'sys_menu_type', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10034, 2, '按钮', '1', 'sys_menu_type', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10035, 1, '全部用户', '2', 'sys_menu_target', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10036, 2, '租户用户', '0', 'sys_menu_target', 1, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10037, 3, '平台用户', '1', 'sys_menu_target', 0, 19980817, '2018-03-16 11:33:00', 19980817, '2018-03-16 11:33:00');
INSERT INTO `sys_dict_data` VALUES (10038, 1, '年', 'year', 'statistics_query_type', 0, 19980817, '2022-07-09 14:05:44', 19980817, '2022-07-09 14:06:33');
INSERT INTO `sys_dict_data` VALUES (10039, 3, '月', 'month', 'statistics_query_type', 0, 19980817, '2022-07-09 14:06:06', 19980817, '2022-07-09 14:06:06');
INSERT INTO `sys_dict_data` VALUES (10040, 4, '周', 'week', 'statistics_query_type', 0, 19980817, '2022-07-09 14:06:14', 19980817, '2022-07-09 14:06:14');
INSERT INTO `sys_dict_data` VALUES (10041, 5, '日', 'date', 'statistics_query_type', 1, 19980817, '2022-07-09 14:06:21', 19980817, '2022-07-14 11:21:15');



SET FOREIGN_KEY_CHECKS = 1;
