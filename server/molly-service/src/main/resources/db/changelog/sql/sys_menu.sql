/*
 Navicat Premium Data Transfer

 Source Server         : 188-mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : 192.168.0.188:3306
 Source Schema         : molly_master

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 08/11/2023 11:25:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (10001, '系统管理', 0, 'sys', 'Layout', 'sys', '/sys', 8, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-06-30 10:28:20', 19980817);
INSERT INTO `sys_menu` VALUES (10002, '系统监控', 0, 'monitor', 'Layout', 'monitor', '/monitor', 10, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-06-30 10:28:13', 19980817);
INSERT INTO `sys_menu` VALUES (10003, '权限管理', 0, 'pre', 'Layout', 'pre', '/pre', 6, 0, 1, 2, 0, '2021-07-09 12:30:00', 19980817, '2022-06-30 10:28:30', 19980817);
INSERT INTO `sys_menu` VALUES (10004, '用户管理', 10003, 'pre:user', 'pre/user/index', 'pre_user', '/pre/user/index', 1, 0, 1, 2, 0, '2021-07-09 12:30:00', 19980817, '2022-03-29 11:30:48', 19980817);
INSERT INTO `sys_menu` VALUES (10005, '角色管理', 10003, 'pre:role', 'pre/role/index', 'pre_role', '/pre/role/index', 2, 0, 1, 2, 0, '2021-07-09 12:30:00', 19980817, '2021-07-22 18:38:15', 19980817);
INSERT INTO `sys_menu` VALUES (10006, '菜单管理', 10003, 'pre:menu', 'pre/menu/index', 'pre_perm', '/pre/menu/index', 3, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-06-27 20:40:07', 19980817);
INSERT INTO `sys_menu` VALUES (10007, '部门管理', 10003, 'pre:dept', 'pre/dept/index', 'pre_dept', '/pre/dept/index', 4, 0, 1, 2, 0, '2021-07-09 12:30:00', 19980817, '2021-07-27 16:46:09', 19980817);
INSERT INTO `sys_menu` VALUES (10008, '字典管理', 10001, 'sys:dict', 'sys/dict/index', 'sys_dictionary', '/sys/dict', 3, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-06-30 12:10:37', 19980817);
INSERT INTO `sys_menu` VALUES (10009, '参数设置', 10001, 'sys:config', 'sys/config/index', 'config', '/sys/config', 4, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-05-07 17:48:19', 19980817);
INSERT INTO `sys_menu` VALUES (10010, '通知公告', 10001, 'sys:notice', 'sys/notice/index', 'notice', '/sys/notice', 5, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2021-10-09 10:39:14', 19980817);
INSERT INTO `sys_menu` VALUES (10011, '服务监控', 10002, 'monitor:server', 'monitor/server/index', 'server_monitor', '/monitor/server', 1, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-05-26 17:07:01', 19980817);
INSERT INTO `sys_menu` VALUES (10012, '系统API', 10001, 'sys:swagger', 'sys/swagger/index', 'interface', '/sys/swagger', 6, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-05-26 17:07:26', 19980817);
INSERT INTO `sys_menu` VALUES (10013, '操作日志', 10002, 'monitor:operlog', 'monitor/operlog/index', 'sys_oper_log', '/monitor/operlog', 3, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2021-10-26 17:22:22', 19980817);
INSERT INTO `sys_menu` VALUES (10014, '登录日志', 10002, 'monitor:logininfor', 'monitor/logininfor/index', 'sys_login_log', '/monitor/logininfor', 4, 0, 1, 1, 0, '2021-07-09 12:30:00', 19980817, '2022-04-02 11:03:47', 19980817);
INSERT INTO `sys_menu` VALUES (10015, '租户模板', 10001, 'sys:template', 'sys/template/index', 'template', '/sys/template', 1, 0, 1, 1, 0, '2021-08-17 14:02:42', 19980817, '2021-08-17 14:03:32', 19980817);
INSERT INTO `sys_menu` VALUES (10016, '租户管理', 10001, 'sys:tenant', 'sys/tenant/index', 'peoples', '/sys/tenant', 2, 0, 1, 1, 0, '2021-08-24 16:20:54', 19980817, '2021-08-24 16:21:30', 19980817);
INSERT INTO `sys_menu` VALUES (10017, '新增', 10004, 'pre_user:create', '#', '', '', 2, 1, 1, 2, 0, '2022-06-25 15:01:56', 19980817, '2022-06-25 15:01:56', 19980817);
INSERT INTO `sys_menu` VALUES (10018, '视图', 10004, 'pre_user:view', '#', '', '', 1, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10019, '修改', 10004, 'pre_user:update', '#', '', '', 3, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10020, '删除', 10004, 'pre_user:delete', '#', '', '', 4, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10021, '重置密码', 10004, 'pre_user:reset:password', '#', '', '', 5, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10023, '视图', 10005, 'pre_role:view', '#', '', '', 1, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10024, '新增', 10005, 'pre_role:create', '#', '', '', 2, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10025, '修改', 10005, 'pre_role:update', '#', '', '', 3, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10026, '删除', 10005, 'pre_role:delete', '#', '', '', 4, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10027, '修改权限', 10005, 'pre_role:update:permissions', '#', '', '', 5, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10028, '视图', 10007, 'pre_dept:view', '#', '', '', 1, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10029, '新增', 10007, 'pre_dept:create', '#', '', '', 2, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10030, '修改', 10007, 'pre_dept:update', '#', '', '', 3, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10031, '删除', 10007, 'pre_dept:delete', '#', '', '', 4, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10032, '视图', 10006, 'pre_menu:view', '#', '', '', 1, 1, 1, 1, 0, '2022-06-25 15:03:42', 19980817, '2022-06-27 20:39:13', 19980817);
INSERT INTO `sys_menu` VALUES (10033, '新增', 10006, 'pre_menu:create', '#', '', '', 2, 1, 1, 1, 0, '2022-06-25 15:03:42', 19980817, '2022-06-27 20:39:43', 19980817);
INSERT INTO `sys_menu` VALUES (10034, '修改', 10006, 'pre_menu:update', '#', '', '', 3, 1, 1, 1, 0, '2022-06-25 15:03:42', 19980817, '2022-06-27 20:39:53', 19980817);
INSERT INTO `sys_menu` VALUES (10035, '删除', 10006, 'pre_menu:delete', '#', '', '', 4, 1, 1, 1, 0, '2022-06-25 15:03:42', 19980817, '2022-06-27 20:39:25', 19980817);
INSERT INTO `sys_menu` VALUES (10036, '字典数据', 10001, 'sys:dict:data', 'sys/dict/dictData', '#', '/sys/dict/data', 7, 0, 0, 1, 0, '2022-06-27 20:36:39', 19980817, '2022-06-30 12:10:50', 19980817);
INSERT INTO `sys_menu` VALUES (10038, '数据统计', 0, 'statistics', 'Layout', 'statistics', '/statistics', 2, 0, 1, 2, 0, '2022-07-30 18:17:42', 19980817, '2023-11-07 15:19:13', 19980817);
INSERT INTO `sys_menu` VALUES (10039, '能耗统计', 10038, 'statistics:energy', 'statistics/energy/index', 'statistics_energy', '/statistics/energy', 5, 0, 1, 2, 0, '2022-07-30 18:18:27', 19980817, '2022-07-30 18:18:27', 19980817);
INSERT INTO `sys_menu` VALUES (10040, '趋势分析', 10038, 'statistics:trend', 'statistics/trend/index', 'statistics_trend', '/statistics/trend', 10, 0, 1, 2, 0, '2022-07-30 18:19:12', 19980817, '2022-07-30 18:19:12', 19980817);
INSERT INTO `sys_menu` VALUES (10041, '设备管理', 0, 'device', 'Layout', 'device', '/device', 3, 0, 1, 2, 0, '2022-07-30 18:19:45', 19980817, '2022-07-30 18:19:45', 19980817);
INSERT INTO `sys_menu` VALUES (10042, '设备列表', 10041, 'device:list', 'device/list/index', 'device_list', '/device/list', 5, 0, 1, 2, 0, '2022-07-30 18:20:24', 19980817, '2022-07-30 18:20:24', 19980817);
INSERT INTO `sys_menu` VALUES (10043, '设备规划', 10041, 'device:deploy', 'device/deploy/index', 'device_deploy', '/device/deploy', 10, 0, 1, 2, 0, '2022-07-30 18:20:56', 19980817, '2023-10-26 10:52:14', 19980817);
INSERT INTO `sys_menu` VALUES (10044, '项目管理', 0, 'project', 'Layout', 'project', '/project', 3, 0, 1, 2, 0, '2022-07-30 18:19:45', 19980817, '2022-07-30 18:19:45', 19980817);
INSERT INTO `sys_menu` VALUES (10045, '项目列表', 10044, 'project:list', 'project/list/index', 'project_list', '/project/list', 5, 0, 1, 2, 0, '2022-07-30 18:20:24', 19980817, '2022-07-30 18:20:24', 19980817);
INSERT INTO `sys_menu` VALUES (10046, '新增', 10045, 'cms_project:create', '#', '', '', 2, 1, 1, 2, 0, '2022-06-25 15:01:56', 19980817, '2022-06-25 15:01:56', 19980817);
INSERT INTO `sys_menu` VALUES (10047, '视图', 10045, 'cms_project:view', '#', '', '', 1, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10048, '修改', 10045, 'cms_project:update', '#', '', '', 3, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10049, '删除', 10045, 'cms_project:delete', '#', '', '', 4, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10050, '重置密码', 10045, 'cms_project:reset:password', '#', '', '', 5, 1, 1, 2, 0, '2022-06-25 15:03:42', 19980817, '2022-06-25 15:03:42', 19980817);
INSERT INTO `sys_menu` VALUES (10051, '用户关联租户', 10004, 'pre_user:link:tenant', '', '', '', 14, 1, 1, 1, 0, '2023-10-26 11:22:19', 19980817, '2023-10-26 11:22:42', 19980817);
INSERT INTO `sys_menu` VALUES (10052, '外链', 0, 'external:links', '#', 'link', '/link', 1, 0, 1, 2, 0, '2023-11-07 15:18:21', 19980817, '2023-11-07 15:18:21', 19980817);
INSERT INTO `sys_menu` VALUES (10053, 'GitHub', 10052, 'GitHub', '#', 'github', 'https://github.com/thousmile/molly-multi-tenant', 5, 0, 1, 2, 0, '2023-11-07 15:50:29', 19980817, '2023-11-07 15:50:55', 19980817);
INSERT INTO `sys_menu` VALUES (10054, '文档', 10052, 'link:doc', '#', 'doc', 'https://github.com/thousmile/molly-multi-tenant', 20, 0, 1, 2, 0, '2023-11-07 16:02:42', 19980817, '2023-11-07 16:02:42', 19980817);

SET FOREIGN_KEY_CHECKS = 1;
