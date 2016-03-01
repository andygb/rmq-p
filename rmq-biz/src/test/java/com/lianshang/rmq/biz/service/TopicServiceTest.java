package com.lianshang.rmq.biz.service;

import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.biz.BaseTest;
import org.junit.Test;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TopicServiceTest extends BaseTest {

	@Autowired
	private TopicService topicService;

	@Test
	public void test() {

		Topic topic =  mockAddData();

		Integer idAdd = topicService.add(topic);
		assert idAdd > 0;
		topic.setId(idAdd);

		topic = topicService.loadByName(topic.getName());
		System.out.println(topic);

		topic = mockUpdateData(topic);
		Integer idUpdate = topicService.updateByName(topic);
		assert idUpdate > 0;

		int queryCount = topicService.queryCount();
		assert queryCount > 0;

		List<Topic> queryList  = topicService.query(1, 20);
		assert queryList.size() > 0;
		System.out.println(queryList);

	}

	private Topic mockAddData() {

		Topic topic = new Topic();
		topic.setId(1137680873);
		topic.setName("rqlaycffjzbifkcflesunptgfiiinriorfio");
        topic.setMemo("备注");

		return topic;
	}

	private Topic mockUpdateData(Topic topic) {

		Topic topicUpdate = new Topic();
		topicUpdate.setId(topic.getId());
		topicUpdate.setName(topic.getName());
        topicUpdate.setMemo("备注B");

		return topicUpdate;
	}

}
