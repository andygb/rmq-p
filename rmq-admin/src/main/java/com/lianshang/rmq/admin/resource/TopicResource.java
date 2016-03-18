package com.lianshang.rmq.admin.resource;

import com.google.common.collect.ImmutableMap;
import com.lianshang.rmq.admin.proxy.TopicProxy;
import com.lianshang.rmq.admin.utils.ResponseUtil;
import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.exception.ErrCode;
import com.lianshang.rmq.api.service.TopicService;
import com.lianshang.rmq.api.util.IdUtil;
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

        topic.setName(name);
        topic.setMemo(memo);
        Integer topicId = topicProxy.add(topic);

        if (!IdUtil.validId(topicId)) {
            return ResponseUtil.wrap(ErrCode.OPERATION_FAILED, "添加topic失败");
        }

        return ImmutableMap.<String, Object>builder()
                .put("code", ResponseUtil.SUCCESS_CODE)
                .put("message", "操作成功")
                .build();
    }
}
