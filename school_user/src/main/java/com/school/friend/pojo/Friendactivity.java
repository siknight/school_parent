package com.school.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_friendactivity")
public class Friendactivity implements Serializable{

	@Id
	private String id;//

	private String activityname;//
	private String userid;//

	private java.util.Date createtime;//发表日期
	private java.util.Date updatetime;//修改日期,我这个应该用不到


	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}


	@Override
	public String toString() {
		return "Friendactivity{" +
				"id='" + id + '\'' +
				", activityname='" + activityname + '\'' +
				", userid='" + userid + '\'' +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				'}';
	}
}
