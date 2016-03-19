package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.lianshang.rmq.admin.proxy.TopicProxy;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.api.util.IdUtil;
import com.lianshang.rmq.provider.MessageProvider;
import com.lianshang.sso.api.security.SecurityControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-03-18.
 *
 * @author yuan.zhong
 */
@Controller
@RequestMapping(value = "/topic-mgmt")
public class TopicController extends BaseController {

    private final static Logger LOG = LoggerFactory.getLogger(TopicController.class);


    @Autowired
    TopicService topicService;

    @Autowired
    TopicProxy topicProxy;

    @SecurityControl(sysTableId = "60003",isButton = false)
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Map<String, Object> query(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("limit") int limit
    )  {
        List<Topic> topicList = topicService.queryByLimit(start, limit) ;

        int totalCount = topicService.queryCount();

        return ImmutableMap.<String, Object>builder()
                .put("code", ResponseUtil.SUCCESS_CODE)
                .put("message", "OK")
                .put("draw", ++draw)
                .put("recordsTotal", topicList.size())
                .put("recordsFiltered", totalCount)
                .put("data", topicList)
                .build();
    }

    @SecurityControl(sysTableId = "60003",isButton = true, btnScript = "OnCreate")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> create(
            @RequestParam("name") String name,
            @RequestParam("memo") String memo
    ) {
        Topic topic = new Topic();

        topic.setName(name.trim());
        topic.setMemo(memo.trim());
        Integer topicId = topicProxy.add(topic);

        if (!IdUtil.validId(topicId)) {
            return ResponseUtil.wrap(ErrCode.OPERATION_FAILED, "添加topic失败");
        }

        return ImmutableMap.<String, Object>builder()
                .put("code", ResponseUtil.SUCCESS_CODE)
                .put("message", "操作成功")
                .build();
    }

    @SecurityControl(sysTableId = "60003",isButton = true, btnScript = "OnProduce")
    @RequestMapping(value = "/produce", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Map<String, Object> produce(
            @RequestParam("topic") String topic,
            @RequestParam("content") String content
    ) {

        MessageProvider messageProvider = new MessageProvider(topic.trim());

        try {
            messageProvider.sendString(content);
        } catch (Exception e) {
            LOG.error("Produce message error, topic [{}]", topic, e);
            return ResponseUtil.wrap(e, "发送失败");
        }

        return ResponseUtil.wrap(ResponseUtil.SUCCESS_CODE, "发送成功");
    }
}
