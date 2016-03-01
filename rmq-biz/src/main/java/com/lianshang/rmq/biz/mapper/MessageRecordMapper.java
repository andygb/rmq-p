package com.lianshang.rmq.biz.mapper;

import com.lianshang.rmq.biz.entity.MessageRecordEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MessageRecordMapper {

	long insert(
		@Param("entity") MessageRecordEntity messageRecordEntity
	);

	MessageRecordEntity load(
		@Param("id") Long id
	);

	long update(
		@Param("entity") MessageRecordEntity messageRecordEntity
	);

	List<MessageRecordEntity> query(
		@Param("offset") int offset,
		@Param("pageSize") int pageSize
	);

	int queryCount(
	);

}
