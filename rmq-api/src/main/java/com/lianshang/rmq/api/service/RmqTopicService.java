package com.lianshang.rmq.api.service;

import com.lianshang.rmq.api.dto.RmqTopic;
import java.util.List;

public interface RmqTopicService {

	Integer add(RmqTopic rmqTopic);

	RmqTopic load(Integer id);

    RmqTopic loadByName(String name);

	Integer update(RmqTopic rmqTopic);

	List<RmqTopic> query(int pageNo, int pageSize);

	int queryCount();

}
