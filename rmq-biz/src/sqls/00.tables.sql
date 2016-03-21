CREATE TABLE `rmq_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '主题名',
  `memo` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  `validity` tinyint(2) NOT NULL DEFAULT '1' COMMENT '有效性 1-有效 0-无效',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_Name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `rmq_message_record` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `message_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '消息ID',
  `topic` varchar(64) NOT NULL DEFAULT '' COMMENT '主题名',
  `producer_ip` varchar(16) NOT NULL DEFAULT '' COMMENT '发布者IP',
  `birth_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '消息产生时间',
  `content` text NOT NULL COMMENT '消息内容字符串',
  `validity` tinyint(2) NOT NULL DEFAULT '1' COMMENT '有效性 1-有效 0-无效',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IX_Topic` (`topic`),
  KEY `IX_ProducerIp` (`producer_ip`),
  KEY `IX_BirthTime` (`birth_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;