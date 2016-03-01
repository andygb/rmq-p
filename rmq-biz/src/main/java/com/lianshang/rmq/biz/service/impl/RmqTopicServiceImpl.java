package com.lianshang.rmq.biz.service.impl;

import com.lianshang.rmq.api.dto.RmqTopic;
import com.lianshang.rmq.api.service.RmqTopicService;
import com.lianshang.rmq.biz.entity.RmqTopicEntity;
import com.lianshang.rmq.biz.mapper.RmqTopicMapper;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

public class RmqTopicServiceImpl implements RmqTopicService {

	@Autowired
	private RmqTopicMapper rmqTopicMapper;

	@Override
	public Integer add(RmqTopic rmqTopic) {

		if (!validAdd(rmqTopic)) {
			return null;
		}

		RmqTopicEntity rmqTopicEntity = dto2entity(rmqTopic);
		rmqTopicMapper.insert(rmqTopicEntity);
		return rmqTopicEntity.getId();
	}


	@Override
	public RmqTopic load(Integer id) {

		RmqTopicEntity rmqTopicEntity = rmqTopicMapper.load(id);
		return entity2dto(rmqTopicEntity);
	}

    @Override
    public RmqTopic loadByName(String name) {
        RmqTopicEntity rmqTopicEntity = rmqTopicMapper.loadByName(name);

        return entity2dto(rmqTopicEntity);
    }


    @Override
	public Integer update(RmqTopic rmqTopic) {

		if (!validUpdate(rmqTopic)) {
			return null;
		}

		RmqTopicEntity rmqTopicEntity = dto2entity(rmqTopic);
		rmqTopicMapper.update(rmqTopicEntity);
		return rmqTopicEntity.getId();
	}


	@Override
	public List<RmqTopic> query(int pageNo, int pageSize) {

		List<RmqTopicEntity> entityList = rmqTopicMapper.query((pageNo-1)*pageSize, pageSize);
		if (entityList == null) {
			return null;
		}

		List<RmqTopic> list = new ArrayList<>();
		for (RmqTopicEntity rmqTopicEntity : entityList) {
			list.add(entity2dto(rmqTopicEntity));
		}
		return list;
	}

	@Override
	public int queryCount() {

		return rmqTopicMapper.queryCount();
	}

	private boolean validAdd(RmqTopic rmqTopic) {

		if (rmqTopic == null) {
			return false;
		}

		if (rmqTopic.getName() == null) {
			return false;
		}

		return true;
	}

	private boolean validUpdate(RmqTopic rmqTopic) {

		if (rmqTopic == null) {
			return false;
		}

		if (rmqTopic.getId() == null) {
			return false;
		}

		return true;
	}

	private RmqTopicEntity dto2entity(RmqTopic rmqTopic) {

		if (rmqTopic == null) {
			return null;
		}

		RmqTopicEntity rmqTopicEntity = new RmqTopicEntity();
		rmqTopicEntity.setId(rmqTopic.getId());
		rmqTopicEntity.setName(rmqTopic.getName());
        rmqTopicEntity.setMemo(rmqTopic.getMemo());
		return rmqTopicEntity;
	}

	private RmqTopic entity2dto(RmqTopicEntity rmqTopicEntity) {

		if (rmqTopicEntity == null) {
			return null;
		}

		RmqTopic rmqTopic = new RmqTopic();
		rmqTopic.setId(rmqTopicEntity.getId());
		rmqTopic.setName(rmqTopicEntity.getName());
        rmqTopic.setMemo(rmqTopicEntity.getMemo());
		return rmqTopic;
	}

}
