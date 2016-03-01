package com.lianshang.rmq.biz.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.biz.entity.TopicEntity;
import com.lianshang.rmq.biz.mapper.TopicMapper;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class TopicServiceImpl implements TopicService {

	@Autowired
	private TopicMapper topicMapper;

	@Override
	public Integer add(Topic topic) {

		if (!validAdd(topic)) {
			return null;
		}

		TopicEntity topicEntity = dto2entity(topic);
		topicMapper.insert(topicEntity);
		return topicEntity.getId();
	}


    @Override
    public Topic loadByName(String name) {
        TopicEntity topicEntity = topicMapper.loadByName(name);

        return entity2dto(topicEntity);
    }


    @Override
	public Integer updateByName(Topic topic) {

		if (!validUpdate(topic)) {
			return null;
		}

		TopicEntity topicEntity = dto2entity(topic);
		topicMapper.updateByName(topicEntity);
		return topicEntity.getId();
	}


	@Override
	public List<Topic> query(int pageNo, int pageSize) {

		List<TopicEntity> entityList = topicMapper.query((pageNo-1)*pageSize, pageSize);
		if (entityList == null) {
			return null;
		}

		List<Topic> list = new ArrayList<>();
		for (TopicEntity topicEntity : entityList) {
			list.add(entity2dto(topicEntity));
		}
		return list;
	}

	@Override
	public int queryCount() {

		return topicMapper.queryCount();
	}

    @Override
    public List<Topic> getByStep(int lastId, int limit) {
        List<TopicEntity> entityList = topicMapper.getByStep(lastId, limit);

        return Lists.transform(entityList, new Function<TopicEntity, Topic>() {
            @Override
            public Topic apply(TopicEntity input) {
                return entity2dto(input);
            }
        });
    }

    private boolean validAdd(Topic topic) {

		if (topic == null) {
			return false;
		}

		if (topic.getName() == null) {
			return false;
		}

		return true;
	}

	private boolean validUpdate(Topic topic) {

		if (topic == null) {
			return false;
		}

		if (topic.getName() == null) {
			return false;
		}

		return true;
	}

	private TopicEntity dto2entity(Topic topic) {

		if (topic == null) {
			return null;
		}

		TopicEntity topicEntity = new TopicEntity();
		topicEntity.setId(topic.getId());
		topicEntity.setName(topic.getName());
        topicEntity.setMemo(topic.getMemo());
		return topicEntity;
	}

	private Topic entity2dto(TopicEntity topicEntity) {

		if (topicEntity == null) {
			return null;
		}

		Topic topic = new Topic();
		topic.setId(topicEntity.getId());
		topic.setName(topicEntity.getName());
        topic.setMemo(topicEntity.getMemo());
		return topic;
	}

}
