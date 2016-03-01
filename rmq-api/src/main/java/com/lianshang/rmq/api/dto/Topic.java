package com.lianshang.rmq.api.dto;

import java.io.Serializable;
import java.util.Date;
import com.google.common.base.MoreObjects;

public class Topic implements Serializable {

	private static final long serialVersionUID = 14567539449343618L;

	/**自增ID**/
	private Integer id;

	/**主题名**/
	private String name;

    /**
     * 备注
     */
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
