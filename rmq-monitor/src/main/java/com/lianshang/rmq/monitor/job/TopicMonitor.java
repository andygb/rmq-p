package com.lianshang.rmq.monitor.job;

import com.lianshang.rmq.api.dto.MessageRecord;
import com.lianshang.rmq.api.dto.Topic;
import com.lianshang.rmq.api.service.MessageRecordService;
import com.lianshang.rmq.common.dto.Message;
import com.lianshang.rmq.common.exception.ConnectionException;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.consumer.ConsumeAction;
import com.lianshang.rmq.consumer.ConsumeResult;
import com.lianshang.rmq.consumer.MessageConsumer;
import com.lianshang.rmq.consumer.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuan.zhong on 2016-03-01.
 *
 * @author yuan.zhong
 */
public class TopicMonitor implements TopicScanObserver {

    final private static Logger LOGGER = LoggerFactory.getLogger(TopicMonitor.class);

    final private static String CONSUMER_ID = "rmq.monitor.message.record";

    @Autowired
    private MessageRecordService messageRecordService;

    Map<String, MessageListener> listenerMap = new HashMap<>();

    GetMessageConsumer gmc  = new GetMessageConsumer();

    @Override
    public void seeTopic(Topic topic) {
        if(topic == null){
            LOGGER.info("【***********传入topic对象为null，结束查询topic***********】");
            return;
        }
        String topicName = topic.getName();
        if(!listenerMap.containsKey(topicName)){
            //message 需要的参数不全
            MessageListener ml = null;

            try {
                ml = new MessageListener(topicName,CONSUMER_ID,gmc);
                //将topic 和message放入对象中
                listenerMap.put(topicName,ml);

            }catch (ConnectionException e){
                LOGGER.error("【====查询topic 连接出错====】",e);
            }

        }else{
            LOGGER.info("【***********该topic{}已经存在**********】",topicName);
        }

    }

   public class  GetMessageConsumer implements MessageConsumer{

       @Override
       public ConsumeResult onMessage(Message message, String topic) {
           try {
               LOGGER.info("【*********获取的消息内容********{}*********】", message.getContentString());
               MessageRecord mr = new MessageRecord();
               mr.setBirthTime(message.getBirthTime());
               mr.setTopic(topic);
               mr.setContent(message.getContentString());
               mr.setMessageId(message.getId());
//             mr.setId(message.getId()); id 是自增这里不用设置
               mr.setProducerId(message.getProducerIp());

               Long lon = messageRecordService.add(mr);
               LOGGER.info("【********消息记录增加到了{}行***********】",lon);

           } catch (SerializationException e) {
               LOGGER.error("【**********消息对象出错**********】",e);
//               return new ConsumeResult(ConsumeAction.RETRY,"重试" );
           }
           LOGGER.info("【********成功返回消息对象*********】");
           return new ConsumeResult(ConsumeAction.ACCEPT,"ok" );
       }
   }

    public void close() {
        for (MessageListener listener : listenerMap.values()) {
            try {
                listener.close();
            } catch (ConnectionException e) {
                LOGGER.error("listener close exception", e);
            }
        }
    }
}
