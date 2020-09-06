package com.joselume.topic.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Occurrence {
	@JsonIgnore
	private @Id @GeneratedValue long id;

	@ManyToOne
	@JsonIgnore
	private Request request;
	
	private String word;

	private int counter;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="OCCURRENCE_NEWS", joinColumns = @JoinColumn(name="OCCURRENCE_ID"), inverseJoinColumns = @JoinColumn(name="NEWS_ID"))
	private List<News> news = new ArrayList<>(); 

	public Occurrence() {
	}

	public Occurrence(Request request, String word, int counter, List<News> news) {
		this.request = request;
		this.word = word;
		this.counter = counter;
		this.news = news;
	}

	public void addNews(News news) {
		if (!this.news.contains(news)) {
			this.news.add(news);	
		}
	}

	public int getCounter() {
		return counter;
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

	public String getWord() {
		return word;
	}

	public void setCounter(int ocurrences) {
		this.counter = ocurrences;
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

	public void setWord(String word) {
		this.word = word;
	}
}
