package com.lianshang.rmq.biz.entity;

import java.util.Date;
import com.google.common.base.MoreObjects;

public class RmqTopicEntity {

	/**自增ID**/
	private Integer id;

	/**主题名**/
	private String name;

    private String memo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
			.add("id",id)
			.add("name",name)
            .add("memo", memo)
			.toString();
	}

}
