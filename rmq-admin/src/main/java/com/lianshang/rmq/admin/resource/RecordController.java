package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lianshang.common.utils.general.StringUtil;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.query.MessageRecordQuery;
import com.lianshang.rmq.api.service.MessageRecordService;
import com.lianshang.sso.api.security.SecurityControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
@RequestMapping(value = "/record")
public class RecordController extends BaseController {

    private final static List<DateFormat> dateFormatList = Lists.<DateFormat>newArrayList(
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    ) ;

    @Autowired
    MessageRecordService messageRecordService;

    @SecurityControl(sysTableId = "60002",isButton = false)
    @RequestMapping(value = "/query", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> query(
            @RequestParam(value = "draw")int draw,
            @RequestParam(value = "topic")String topic,
            @RequestParam(value = "producerIp") String producerIp,
            @RequestParam(value = "birthTimeBegin") String birthTimeBegin,
            @RequestParam(value = "birthTimeEnd") String birthTimeEnd,
            @RequestParam(value = "start")int start,
            @RequestParam(value = "limit")int limit
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

    @SecurityControl(sysTableId = "60002",isButton = true, btnScript = "OnView")
    @RequestMapping(value = "/view", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Map<String, Object> view(
            @RequestParam("id") long id
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
