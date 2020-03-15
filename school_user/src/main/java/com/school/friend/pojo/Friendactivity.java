package com.school.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
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


	
	private String activityName;//
	private String userid;//

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getActivityName() {		
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}


	
}
