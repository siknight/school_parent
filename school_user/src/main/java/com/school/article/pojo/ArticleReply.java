package com.school.article.pojo;

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
@Table(name="tb_article_reply")
public class ArticleReply implements Serializable{

	@Id
	private String id;//编号
	private String articleid;//问题ID
	private String content;//回答内容
	private java.util.Date createtime;//创建日期
	private java.util.Date updatetime;//更新日期
	private String userid;//回答人ID
	private String nickname;//回答人昵称

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNickname() {		
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	@Override
	public String toString() {
		return "ArticleReply{" +
				"id='" + id + '\'' +
				", articleid='" + articleid + '\'' +
				", content='" + content + '\'' +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				", userid='" + userid + '\'' +
				", nickname='" + nickname + '\'' +
				'}';
	}
}
