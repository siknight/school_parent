package com.school.article.pojo;

import com.school.user.pojo.User;

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
@Table(name="tb_article")
public class Article implements Serializable{

	@Id
	private String id;//ID

	
	private String columnid;//专栏ID，对应另一张表
	private String userid;//用户ID
	private String title;//标题
	private String content;//文章正文，用于存储具体内容
	private String image;//文章封面
	private java.util.Date createtime;//发表日期
	private java.util.Date updatetime;//修改日期,我这个应该用不到
	private String ispublic;//是否公开，我这个暂时不用
	private String istop;//是否置顶，我这个暂时不用
	private Integer visits;//浏览量
	private Integer thumbup;//点赞数
	private Integer comment;//评论数
	private String state;//审核状态，这个属性，我这项目不用了
	private String channelid;//所属频道,对应另一张表，我这个暂时不用
	private String url;//URL
	private String type;//类型

	private  String time;


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getColumnid() {		
		return columnid;
	}
	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTitle() {		
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {		
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {		
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	public java.util.Date getUpdatetime() {		
		return updatetime;
	}
	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getIspublic() {		
		return ispublic;
	}
	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	public String getIstop() {		
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}

	public Integer getVisits() {		
		return visits;
	}
	public void setVisits(Integer visits) {
		this.visits = visits;
	}

	public Integer getThumbup() {		
		return thumbup;
	}
	public void setThumbup(Integer thumbup) {
		this.thumbup = thumbup;
	}

	public Integer getComment() {		
		return comment;
	}
	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getChannelid() {		
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getUrl() {		
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Article{" +
				"id='" + id + '\'' +
				", columnid='" + columnid + '\'' +
				", userid='" + userid + '\'' +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", image='" + image + '\'' +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				", ispublic='" + ispublic + '\'' +
				", istop='" + istop + '\'' +
				", visits=" + visits +
				", thumbup=" + thumbup +
				", comment=" + comment +
				", state='" + state + '\'' +
				", channelid='" + channelid + '\'' +
				", url='" + url + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
