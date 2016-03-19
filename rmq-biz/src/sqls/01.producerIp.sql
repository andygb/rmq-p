ALTER TABLE `rmq_message_record` CHANGE `producer_id` `producer_ip` VARCHAR(16)
 CHARACTER SET utf8
 COLLATE utf8_general_ci
 NOT NULL
 DEFAULT ''
 COMMENT '发布者IP'

ALTER TABLE `rmq_message_record` ADD `birth_time_unix` BIGINT(20) NOT NULL  DEFAULT 0 AFTER `birth_time`;
UPDATE `rmq_message_record` SET birth_time_unix = unix_timestamp(birth_time) * 1000;
ALTER TABLE `rmq_message_record` DROP COLUMN `birth_time`;
ALTER TABLE `rmq_message_record` CHANGE `birth_time_unix` `birth_time` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '消息产生时间';

ALTER TABLE `rmq_message_record` ADD KEY `IX_ProducerIp` (`producer_ip`);
ALTER TABLE `rmq_message_record` ADD KEY `IX_Topic` (`topic`);
ALTER TABLE `rmq_message_record` ADD KEY `IX_BirthTime` (`birth_time`);