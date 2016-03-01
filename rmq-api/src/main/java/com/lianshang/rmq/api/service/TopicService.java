package com.lianshang.rmq.api.service;

import com.lianshang.rmq.api.dto.Topic;
import java.util.List;

public interface TopicService {

	Integer add(Topic topic);

    Topic loadByName(String name);

	Integer updateByName(Topic topic);

	List<Topic> query(int pageNo, int pageSize);

	int queryCount();

    List<Topic> getByStep(int lastId, int limit);

}
