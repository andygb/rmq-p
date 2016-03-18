package com.lianshang.rmq.api.service;

import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.query.MessageRecordQuery;

import java.util.List;

public interface MessageRecordService {

	Long add(MessageRecord messageRecord);

	MessageRecord load(Long id);

	Long update(MessageRecord messageRecord);

	List<MessageRecord> query(MessageRecordQuery query, int pageNo, int pageSize);

    List<MessageRecord> queryByLimit(
            MessageRecordQuery query,
            int start,
            int limit
    );

	int queryCount(MessageRecordQuery query);

}
