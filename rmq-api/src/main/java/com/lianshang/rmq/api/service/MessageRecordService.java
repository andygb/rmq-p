package com.lianshang.rmq.api.service;

import com.lianshang.rmq.api.dto.MessageRecord;
import java.util.List;

public interface MessageRecordService {

	Long add(MessageRecord messageRecord);

	MessageRecord load(Long id);

	Long update(MessageRecord messageRecord);

	List<MessageRecord> query(int pageNo, int pageSize);

	int queryCount();

}
