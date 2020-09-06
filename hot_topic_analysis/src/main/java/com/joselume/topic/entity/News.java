package com.joselume.topic.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class News {
	@JsonIgnore
	private @Id @GeneratedValue long id;
	
	@Column(length=2000)
	private String title;

	@ManyToOne
	@JsonIgnore
	private RSSFeed feed;
	
	@Column(length=2000)
	private  String link;
	
	@ManyToMany(mappedBy="news")
	@JsonIgnore
	private List<Occurrence> occurrences = new ArrayList<>(); 
	
	public News() {
	}

	public News(String title, RSSFeed feed, String link, List<Occurrence> occurrences) {
		this.title = title;
		this.feed = feed;
		this.link = link;
		this.occurrences = occurrences;
	}
	
	public void addOccurrence(Occurrence occurrence) {
		occurrences.add(occurrence);
	}

	public RSSFeed getFeed() {
		return feed;
	}
	
	public long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	public String getTitle() {
		return title;
	}

	public void setFeed(RSSFeed feed) {
		this.feed = feed;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public void setOccurrences(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
