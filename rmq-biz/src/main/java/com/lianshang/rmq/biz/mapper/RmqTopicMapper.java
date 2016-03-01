package com.lianshang.rmq.biz.mapper;

import com.lianshang.rmq.biz.entity.RmqTopicEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface RmqTopicMapper {

	int insert(
		@Param("entity") RmqTopicEntity rmqTopicEntity
	);

	RmqTopicEntity load(
		@Param("id") Integer id	
	);

    RmqTopicEntity loadByName(
            @Param("name") String name
    );

	int update(
		@Param("entity") RmqTopicEntity rmqTopicEntity
	);

	List<RmqTopicEntity> query(
		@Param("offset") int offset,
		@Param("pageSize") int pageSize
	);

	int queryCount(
	);

}
