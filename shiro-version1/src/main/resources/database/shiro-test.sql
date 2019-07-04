/*
Navicat MySQL Data Transfer

Source Server         : Users
Source Server Version : 50087
Source Host           : localhost:3306
Source Database       : shiro-test

Target Server Type    : MYSQL
Target Server Version : 50087
File Encoding         : 65001

Date: 2019-07-04 16:19:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for shiro_menu
-- ----------------------------
DROP TABLE IF EXISTS `shiro_menu`;
CREATE TABLE `shiro_menu` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `menu_name` varchar(50) default NULL COMMENT '资源名称',
  `menu_url` varchar(100) default NULL COMMENT '资源路径',
  `parent_id` bigint(255) default NULL COMMENT '资源的父资源',
  `menu_sort` int(20) default NULL COMMENT '资源排序',
  `permission` varchar(255) default NULL COMMENT '//这个将资源细分到每一个按钮这样就可以将之前创建的权限表（要保存增删改查）的去掉',
  `menu_type` int(2) default NULL COMMENT '资源的类型 是菜单还是按钮 1是 菜单 2是 按钮',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_menu
-- ----------------------------
INSERT INTO `shiro_menu` VALUES ('6', null, null, null, null, null, null, null, null, '用户菜单', '/user/query.html', '0', '100', 'menu:query', '1');
INSERT INTO `shiro_menu` VALUES ('7', null, null, null, null, null, null, null, null, '角色菜单', '/role/query.html', '0', '200', 'menu:query', '1');
INSERT INTO `shiro_menu` VALUES ('8', null, null, null, null, null, null, null, null, '资源菜单', '/menu/query.html', '0', '300', 'menu:query', '1');

-- ----------------------------
-- Table structure for shiro_menu_permission_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_menu_permission_role`;
CREATE TABLE `shiro_menu_permission_role` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `role_id` bigint(20) default NULL,
  `menu_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_menu_permission_role
-- ----------------------------
INSERT INTO `shiro_menu_permission_role` VALUES ('6', null, null, null, null, null, null, null, null, '12', '6');
INSERT INTO `shiro_menu_permission_role` VALUES ('7', null, null, null, null, null, null, null, null, '12', '7');
INSERT INTO `shiro_menu_permission_role` VALUES ('8', null, null, null, null, null, null, null, null, '12', '8');

-- ----------------------------
-- Table structure for shiro_permissions
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permissions`;
CREATE TABLE `shiro_permissions` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `permissions` varchar(50) default NULL COMMENT '权限名',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_permissions
-- ----------------------------
INSERT INTO `shiro_permissions` VALUES ('1', null, null, null, null, null, null, null, null, '额热群无若');

-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `role_name` varchar(20) default NULL COMMENT '角色名',
  `role` varchar(50) default NULL COMMENT '角色标识程序中判断使用,如"admin",这个是唯一的:',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_role
-- ----------------------------
INSERT INTO `shiro_role` VALUES ('12', null, null, null, null, null, null, null, null, '添加角色', 'role');

-- ----------------------------
-- Table structure for shiro_user
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user`;
CREATE TABLE `shiro_user` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `user_name` varchar(50) default NULL COMMENT '用户姓名',
  `login_name` varchar(50) default NULL COMMENT '登录名',
  `password` varchar(50) default NULL COMMENT '密码',
  `tel` varchar(30) default NULL COMMENT '电话',
  `address` varchar(255) default NULL COMMENT '地址',
  `id_card` varchar(50) default NULL COMMENT '身份证号',
  `sex` int(2) default NULL COMMENT '性别',
  `birthday` datetime default NULL COMMENT '出生日期',
  `age` int(10) default NULL COMMENT '年龄',
  `e_mail` varchar(50) default NULL COMMENT '邮箱',
  `salt` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_user
-- ----------------------------
INSERT INTO `shiro_user` VALUES ('7', null, null, null, null, null, null, null, null, 'D鞍山', 'admin', 'd3c59d25033dbf980d29554025c23a75', '12345667738', 'string', '6728289229292929', '1', '2019-06-10 08:24:51', '0', 'string', '8d78869f470951332959580424d4bf4f');

-- ----------------------------
-- Table structure for shiro_user_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role` (
  `id` bigint(11) NOT NULL auto_increment,
  `flag` int(11) default NULL,
  `del` int(11) default NULL,
  `creator_id` bigint(20) default NULL,
  `creator_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `create_time` datetime default NULL,
  `opertor_name` varchar(255) character set utf8 collate utf8_bin default NULL,
  `opertor_id` bigint(20) default NULL,
  `oper_time` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `role_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_user_role
-- ----------------------------
INSERT INTO `shiro_user_role` VALUES ('2', null, null, null, null, null, null, null, null, '7', '12');
