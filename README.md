# RMQ
[version]: 1.0.0-SNAPSHOT
 
### 什么是RMQ?
RMQ是一个基于RabbitMQ实现的消息队列系统，实现了发布/订阅模式

---

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