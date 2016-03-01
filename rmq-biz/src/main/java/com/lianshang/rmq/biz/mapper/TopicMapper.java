package com.lianshang.rmq.biz.mapper;

import com.lianshang.rmq.biz.entity.TopicEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TopicMapper {

	int insert(
		@Param("entity") TopicEntity topicEntity
	);

	TopicEntity load(
		@Param("id") Integer id	
	);

    TopicEntity loadByName(
            @Param("name") String name
    );

	int update(
		@Param("entity") TopicEntity topicEntity
	);

    int updateByName(
            @Param("entity") TopicEntity topicEntity
    );

	List<TopicEntity> query(
		@Param("offset") int offset,
		@Param("pageSize") int pageSize
	);

	int queryCount(
	);

    List<TopicEntity> getByStep(
            @Param("lastId") int lastId,
            @Param("limit") int limit
    );

}
