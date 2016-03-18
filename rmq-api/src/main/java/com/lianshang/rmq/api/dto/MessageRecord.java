package com.lianshang.rmq.api.dto;

import java.io.Serializable;
import java.util.Date;
import com.google.common.base.MoreObjects;

public class MessageRecord implements Serializable {

	private static final long serialVersionUID = 14568277790401879L;

	/**自增ID**/
	private Long id;

    /**消息ID**/
    private Long messageId;

	/**主题名**/
	private String topic;

	/**发布者IP**/
	private String producerIp;

	/**消息产生时间**/
	private Date birthTime;

	/**消息内容字符串**/
	private String content;

	/**有效性 1-有效 0-无效**/
	private Byte validity;

	/**添加时间**/
	private Date createTime;

	/**更新时间**/
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getProducerIp() {
		return producerIp;
	}

	public void setProducerIp(String producerIp) {
		this.producerIp = producerIp;
	}

	public Date getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(Date birthTime) {
		this.birthTime = birthTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Byte getValidity() {
		return validity;
	}

	public void setValidity(Byte validity) {
		this.validity = validity;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
			.add("id",id)
			.add("topic",topic)
			.add("producerIp", producerIp)
			.add("birthTime",birthTime)
			.add("content",content)
			.add("validity",validity)
			.add("createTime",createTime)
			.add("updateTime",updateTime)
			.toString();
	}

}
