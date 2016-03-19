package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.lianshang.rmq.admin.proxy.TopicProxy;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.api.util.IdUtil;
import com.lianshang.rmq.provider.MessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
@Path("/topic-mgmt")
@Produces(MediaType.APPLICATION_JSON)
public class TopicResource extends BaseResource {

    private final static Logger LOG = LoggerFactory.getLogger(TopicResource.class);


    @Autowired
    TopicService topicService;

    @Autowired
    TopicProxy topicProxy;

    @POST
    @Path("query")
    public Map<String, Object> query(
            @FormParam("draw") int draw,
            @FormParam("start") int start,
            @FormParam("limit") int limit
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

    @POST
    @Path("create")
    public Map<String, Object> create(
            @FormParam("name") String name,
            @FormParam("memo") String memo
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

    @POST
    @Path("/produce")
    public Map<String, Object> produce(
            @FormParam("topic") String topic,
            @FormParam("content") String content
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
