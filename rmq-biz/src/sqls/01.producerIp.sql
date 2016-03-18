ALTER TABLE `rmq_message_record` CHANGE `producer_id` `producer_ip` VARCHAR(16)
 CHARACTER SET utf8
 COLLATE utf8_general_ci
 NOT NULL
 DEFAULT ''
 COMMENT '发布者IP'