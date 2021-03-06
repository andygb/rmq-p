# RMQ

 
### 什么是RMQ?
RMQ是一个基于RabbitMQ实现的消息队列系统，实现了发布/订阅模式

---

### 当前版本

[maven仓库中查看](http://192.168.1.251:8081/artifactory/webapp/search/artifact/?q=rmq-*-1.0.1.*)

#### 发布者

```
		<dependency>
            <groupId>com.lianshang.rmq</groupId>
            <artifactId>rmq-provider</artifactId>
            <version>1.0.1</version>
        </dependency>
```

#### 订阅者
```
        <dependency>
            <groupId>com.lianshang.rmq</groupId>
            <artifactId>rmq-consumer</artifactId>
            <version>1.0.1</version>
        </dependency>
```

### 相关名词
* *topic* -- topic是一个预先在系统中申请到的字符串，用于标识一类消息的主题，发布方需将消息发布到特定的主题，接收方需声明自己接收特定主题的
* *consumer id* -- 消费者ID的作用是标志同一主题下的不同的订阅方。在分布式系统中，若同一主题的消息有多个订阅方，则不同消费者ID的订阅该都会收到该消息，而相同消费者ID的订阅方中只有一个会收到该消息。举个例子，现有订阅方A-1, A-2, B-1, B-2都订阅了同一主题的消息，其中A-1, A-2的消费者ID是"A", B-1, B-2的消费者ID是"B", 则当一条消息产生时， A-1, A-2中有一方会收到消息，同时B-1, B-2中有一方会收到消息

---


### 怎样使用RMQ?
#### 发布者
发布者需要构造MessageProvider以声明发布消息的主题

实例化发布者及发送消息的方法如下

```
public class MessageProvider {

    /**
     * 构造消息发布者
     * @param topic 消息主题
     */
    public MessageProvider(String topic) {
        ...
    }

    /**
     * 发送二进制消息
     * @param content   消息内容
     * @throws ConnectionException
     * @throws SerializationException   
     */
    public void sendBytes(byte[] content) throws ConnectionException, SerializationException {
        ...
    }

    /**
     * 发送字符串消息
     * @param content   消息内容
     * @throws ConnectionException
     * @throws SerializationException
     */
    public void sendString(String content) throws ConnectionException, SerializationException {
        ...
    }

    /**
     * 发送对象消息
     * @param content   消息内容
     * @throws SerializationException
     * @throws ConnectionException
     */
    public void sendObject(Object content) throws SerializationException, ConnectionException {
        ...
    }
}

```

具体使用举例如下：

```
		MessageProvider messageProvider = new MessageProvider("test-topic");
        messageProvider.sendString("An example message");        
```


#### 订阅者

订阅者需构造`MessageListener`以监听指定主题的消息及自身的消费者ID

实例化监听者的方法如下

```
public class MessageListener {
	...

    /**
     * 构造监听器
     * @param topic 消息主题
     * @param consumerId    消费者ID
     * @param consumer      消费者，即消息处理器
     * @throws ConnectionException
     */
    public MessageListener(String topic, String consumerId, MessageConsumer consumer) throws ConnectionException {
        ...
    }
}
```

构造监听者需要参数如下:

* *topic* -- 声明订阅消息主题	
* *consumerId* -- 声明自身的消费者ID
* *consumer* -- 消息消费者(处理器), 对消息收到的消息进行处理，需实现MessageConsumer接口

举例如下：

```
		MessageListener listener = new MessageListener("test-topic", "test-consumer-1", new MessageConsumer() {
            @Override
            public ConsumeResult onMessage(Message message) {
                try {
                    System.out.println(message.getContentString());
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return new ConsumeResult(ConsumeAction.ACCEPT, "OK");
            }
        });
        
        ...
        lisenter.close();
```
上述例子中，使用匿名类方式实现了`MessageConsumer`接口，注意到实现`MessageConsumer`接口的`onMessage`方法需返回`ConsumeResult`对象，其中`ConsumeAction`表示消费行为，枚举如下：

* *ACCEPT* -- 表示消费成功
* *REJECT* -- 表示消费者拒绝该消息，并不再重试
* *RETRY* -- 表示消费者处理该消息失败，需要重试

若返回消费行为*RETRY*，则该条消息会再次进入消息队列，再次发送给该主题下同消费者ID的订阅方(不一定是同一个`MessageListener`)，一条消息最多重试1次

监听器使用完毕，需要调用`close()`方法以关闭连接，否则应用无法正常关闭

推荐使用spring注入方式实例化监听器，如下

```
	<bean class="com.lianshang.rmq.consumer.MessageListener" id="messageListener" destroy-method="close">
        <constructor-arg name="topic" value="test-topic"/>
        <constructor-arg name="consumerId" value="test-consumer-1"/>
        <constructor-arg name="consumer" ref="testConsumer"/>
    </bean>
```
其中`testConsumer`为一个实现MessageConsumer的类的实例bean

---

### 管理后台地址

* beta		http://192.168.1.225:8140/rmq-admin
* ppe 		http://lsrmq.lian-shang.cn/rmq-admin
* product	http://lsrmq.lianshang.com/rmq-admin

### 目前已有topic

|topic | 说明|
|------|-------|
|demand.publish.success|发布采购需求成功|
|demand.audit.success|审核采购需求成功|
|trade.create.success|创建订单成功|
|trade.finish.success|订单交易成功|

---> 

### FAQ

#### Q: RMQ消息会重复发么?

A: 会。由于RMQ保证消息不会丢失，所以当消息没有正确确认时或发生异常时，消息将会重发，因此可能重发时消息已经处理；消息体中有`Message.getId()`字段可获取消息的唯一ID, 因上述原因重发的消息其ID不变，业务方可根据该值做去重处理

#### Q: RMQ发出的消息会保存多久，如果消息发出时接收方没有启动，那么启动时能否接收到消息？

A: 消息保存在服务器中的前提是服务器中已有该Consumer id的队列，当该Consumer id的接收方第一次启动时会在集群中建立相应队列，此后会一直存在(除非人工删除); 发送到队列中的消息只有被消费后才会删除，因此上述问题分为两种情况：1. 接收方consumer id从未运行过，则接收方启动时也接收不到启动前发出的消息；2. 接收方consumer id曾运行过，则接收方启动时将收到启动前没有被消费的消息

#### Q: RMQ的消息保证顺序性么？

A: 否