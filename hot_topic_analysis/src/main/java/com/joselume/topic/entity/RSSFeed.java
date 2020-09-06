package com.joselume.topic.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RSSFeed {
	private @Id @GeneratedValue long id;
	private String url;
	
	@ManyToOne
	private Request request;
	
	@OneToMany(mappedBy="feed", cascade=CascadeType.ALL)
	private List<News> news;
	
	public RSSFeed() {
	}

	public RSSFeed(String url, Request request, List<News> news) {
		this.url = url;
		this.request = request;
		this.news = news;
	}

	public long getId() {
		return id;
	}

	public List<News> getNews() {
		return news;
	}

	public Request getRequest() {
		return request;
	}

	public String getUrl() {
		return url;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setNews(List<News> news) {
		this.news = news;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
