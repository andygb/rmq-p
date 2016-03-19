package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lianshang.common.utils.general.StringUtil;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.query.MessageRecordQuery;
import com.lianshang.rmq.api.service.MessageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final static List<DateFormat> dateFormatList = Lists.<DateFormat>newArrayList(
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    ) ;

    @Autowired
    MessageRecordService messageRecordService;

    @POST
    @Path("/query")
    public Map<String, Object> query(
            @FormParam(value = "draw")int draw,
            @FormParam(value = "topic")String topic,
            @FormParam(value = "producerIp") String producerIp,
            @FormParam(value = "birthTimeBegin") String birthTimeBegin,
            @FormParam(value = "birthTimeEnd") String birthTimeEnd,
            @FormParam(value = "start")int start,
            @FormParam(value = "limit")int limit
    ) {
        MessageRecordQuery query = new MessageRecordQuery()
                .setTopic(topic.trim())
                .setProducerIp(producerIp.trim())
                .setBirthTimeBegin(parseDate(birthTimeBegin))
                .setBirthTimeEnd(parseDate(birthTimeEnd));

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

    private Date parseDate(String dateStr) {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }

        for (DateFormat dateFormat : dateFormatList) {
            Date date = StringUtil.parseDate(dateStr, dateFormat);
            if (date != null) {
                return date;
            }
        }

        return null;
    }

    private Date parseDate(Long dateLong) {
        if (dateLong == null) {
            return null;
        }

        return new Date(dateLong);
    }
}
