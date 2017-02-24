CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `url` varchar(200) NOT NULL COMMENT '工程的git地址',
  `current_tag` varchar(45) DEFAULT NULL COMMENT '该工程的当前最大tag号',
  `create_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=13000033 DEFAULT CHARSET=utf8;

CREATE TABLE `instance_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mainClass` varchar(100) NOT NULL,
  `mainArgs` varchar(100) DEFAULT NULL,
  `jvmArgs` varchar(200) NOT NULL,
  `sourceName` varchar(200) NOT NULL,
  `serverName` varchar(60) DEFAULT NULL,
  `remark` varchar(30) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Instance_Group_serverName_uindex` (`serverName`)
) ENGINE=InnoDB AUTO_INCREMENT=13000033  DEFAULT CHARSET=utf8;