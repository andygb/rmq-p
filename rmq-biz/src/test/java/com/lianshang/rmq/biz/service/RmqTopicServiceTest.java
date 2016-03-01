package com.lianshang.rmq.biz.service;

import java.util.Date;
import com.google.common.base.MoreObjects;
import com.lianshang.rmq.api.dto.RmqTopic;
import com.lianshang.rmq.api.service.RmqTopicService;
import com.lianshang.rmq.biz.BaseTest;
import org.junit.Test;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class RmqTopicServiceTest extends BaseTest {

	@Autowired
	private RmqTopicService rmqTopicService;

	@Test
	public void test() {

		RmqTopic rmqTopic =  mockAddData();

		Integer idAdd = rmqTopicService.add(rmqTopic);
		assert idAdd > 0;
		rmqTopic.setId(idAdd);

		rmqTopic = rmqTopicService.load(rmqTopic.getId());
		System.out.println(rmqTopic);

		rmqTopic = mockUpdateData(rmqTopic);
		Integer idUpdate = rmqTopicService.update(rmqTopic);
		assert idUpdate > 0;

		int queryCount = rmqTopicService.queryCount();
		assert queryCount > 0;

		List<RmqTopic> queryList  = rmqTopicService.query(1, 20);
		assert queryList.size() > 0;
		System.out.println(queryList);

	}

	private RmqTopic mockAddData() {

		RmqTopic rmqTopic = new RmqTopic();
		rmqTopic.setId(1137680873);
		rmqTopic.setName("rqlaycffjzbifkcflesunptgfiiinriorfio");
        rmqTopic.setMemo("备注");

		return rmqTopic;
	}

	private RmqTopic mockUpdateData(RmqTopic rmqTopic) {

		RmqTopic rmqTopicUpdate = new RmqTopic();
		rmqTopicUpdate.setId(rmqTopic.getId());
		rmqTopicUpdate.setName("expefcppdqvlrpciannilbhfopcrdjxzqtouzurrmesihvvuqsflla");
        rmqTopicUpdate.setMemo("备注");

		return rmqTopicUpdate;
	}

}
