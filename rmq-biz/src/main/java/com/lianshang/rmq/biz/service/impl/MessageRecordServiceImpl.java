package com.lianshang.rmq.biz.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.query.MessageRecordQuery;
import com.lianshang.rmq.api.service.MessageRecordService;
import com.lianshang.rmq.biz.entity.MessageRecordEntity;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.lianshang.rmq.biz.mapper.MessageRecordMapper;
import com.lianshang.rmq.biz.query.MessageRecordQueryEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageRecordServiceImpl implements MessageRecordService {

	@Autowired
	private MessageRecordMapper messageRecordMapper;

    private final Function<MessageRecordEntity, MessageRecord> entity2dtoFunction = new Function<MessageRecordEntity, MessageRecord>() {
        @Override
        public MessageRecord apply(MessageRecordEntity input) {
            return entity2dto(input);
        }
    };

	@Override
	public Long add(MessageRecord messageRecord) {

		if (!validAdd(messageRecord)) {
			return null;
		}

		MessageRecordEntity messageRecordEntity = dto2entity(messageRecord);
		messageRecordMapper.insert(messageRecordEntity);
		return messageRecordEntity.getId();
	}


	@Override
	public MessageRecord load(Long id) {

		MessageRecordEntity messageRecordEntity = messageRecordMapper.load(id);
		return entity2dto(messageRecordEntity);
	}


	@Override
	public Long update(MessageRecord messageRecord) {

		if (!validUpdate(messageRecord)) {
			return null;
		}

		MessageRecordEntity messageRecordEntity = dto2entity(messageRecord);
		messageRecordMapper.update(messageRecordEntity);
		return messageRecordEntity.getId();
	}


	@Override
	public List<MessageRecord> query(MessageRecordQuery query, int pageNo, int pageSize) {

		List<MessageRecordEntity> entityList = messageRecordMapper.query(dto2entity(query), (pageNo-1)*pageSize, pageSize);
		if (entityList == null) {
			return null;
		}

		List<MessageRecord> list = new ArrayList<>();
		for (MessageRecordEntity messageRecordEntity : entityList) {
			list.add(entity2dto(messageRecordEntity));
		}
		return list;
	}

    @Override
    public List<MessageRecord> queryByLimit(MessageRecordQuery query, int start, int limit) {
        List<MessageRecordEntity> entityList = messageRecordMapper.query(dto2entity(query), start, limit);

        return FluentIterable.from(entityList).transform(entity2dtoFunction).toList();
    }

    @Override
    public int queryCount(MessageRecordQuery query) {
        return messageRecordMapper.queryCount(dto2entity(query));
    }


	private boolean validAdd(MessageRecord messageRecord) {

		if (messageRecord == null) {
			return false;
		}

        if (messageRecord.getMessageId() == null) {
            return false;
        }

		if (messageRecord.getTopic() == null) {
			return false;
		}

		if (messageRecord.getProducerIp() == null) {
			return false;
		}

		if (messageRecord.getBirthTime() == null) {
			return false;
		}

		if (messageRecord.getContent() == null) {
			return false;
		}

		return true;
	}

	private boolean validUpdate(MessageRecord messageRecord) {

		if (messageRecord == null) {
			return false;
		}

		if (messageRecord.getId() == null) {
			return false;
		}

		return true;
	}

	private MessageRecordEntity dto2entity(MessageRecord messageRecord) {

		if (messageRecord == null) {
			return null;
		}

		MessageRecordEntity messageRecordEntity = new MessageRecordEntity();
		messageRecordEntity.setId(messageRecord.getId());
        messageRecordEntity.setMessageId(messageRecord.getMessageId());
		messageRecordEntity.setTopic(messageRecord.getTopic());
		messageRecordEntity.setProducerIp(messageRecord.getProducerIp());
		messageRecordEntity.setBirthTime(messageRecord.getBirthTime().getTime());
		messageRecordEntity.setContent(messageRecord.getContent());
		messageRecordEntity.setValidity(messageRecord.getValidity());
		messageRecordEntity.setCreateTime(messageRecord.getCreateTime());
		messageRecordEntity.setUpdateTime(messageRecord.getUpdateTime());
		return messageRecordEntity;
	}

	private MessageRecord entity2dto(MessageRecordEntity messageRecordEntity) {

		if (messageRecordEntity == null) {
			return null;
		}

		MessageRecord messageRecord = new MessageRecord();
		messageRecord.setId(messageRecordEntity.getId());
        messageRecord.setMessageId(messageRecordEntity.getMessageId());
		messageRecord.setTopic(messageRecordEntity.getTopic());
		messageRecord.setProducerIp(messageRecordEntity.getProducerIp());
		messageRecord.setBirthTime(new Date(messageRecordEntity.getBirthTime()));
		messageRecord.setContent(messageRecordEntity.getContent());
		messageRecord.setValidity(messageRecordEntity.getValidity());
		messageRecord.setCreateTime(messageRecordEntity.getCreateTime());
		messageRecord.setUpdateTime(messageRecordEntity.getUpdateTime());
		return messageRecord;
	}

    private MessageRecordQueryEntity dto2entity(MessageRecordQuery query) {
        return new MessageRecordQueryEntity()
                .setTopic(query.getTopic())
                .setProducerIp(query.getProducerIp())
                .setBirthTimeBegin(getDateLong(query.getBirthTimeBegin()))
                .setBirthTimeEnd(getDateLong(query.getBirthTimeEnd()));
    }

    private Long getDateLong(Date date) {
        if (date == null) {
            return null;
        }

        return date.getTime();
    }

}
