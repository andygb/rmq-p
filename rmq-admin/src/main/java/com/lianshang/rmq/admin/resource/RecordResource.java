package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.query.MessageRecordQuery;
import com.lianshang.rmq.api.service.MessageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Path("/record")
@Produces(MediaType.APPLICATION_JSON)
public class RecordResource extends BaseResource {

    @Autowired
    MessageRecordService messageRecordService;

    @POST
    @Path("/query")
    public Map<String, Object> query(
            @FormParam("draw")int draw,
            @FormParam("topic")String topic,
            @FormParam("producerIp") String producerIp,
            @FormParam("start")int start,
            @FormParam("limit")int limit
    ) {
        MessageRecordQuery query = new MessageRecordQuery()
                .setTopic(topic.trim())
                .setProducerIp(producerIp.trim());

        List<MessageRecord> recordList = messageRecordService.queryByLimit(query, start, limit);

        int totalCount = messageRecordService.queryCount(query);

        return ImmutableMap.<String, Object>builder()
                .put("code", ResponseUtil.SUCCESS_CODE)
                .put("message", "OK")
                .put("draw", ++draw)
                .put("recordsTotal", totalCount)
                .put("recordsFiltered", totalCount)
                .put("data", recordList)
                .build();
    }

    @POST
    @Path("/view")
    public Map<String, Object> view(
            @FormParam("id") long id
    ) {
        MessageRecord record = messageRecordService.load(id);

        if (record == null) {
            return ResponseUtil.wrap(ErrCode.INVALID_INFO, "无效的消息ID");
        }

        return ImmutableMap.<String, Object>builder()
                .put("code", ResponseUtil.SUCCESS_CODE)
                .put("message", "OK")
                .put("data", record)
                .build();
    }
}
