package com.joselume.topic.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Request {
	
	private @Id @GeneratedValue long id;
	
	@JsonBackReference 
	@OneToMany(mappedBy="request", cascade=CascadeType.ALL)
	private List<Occurrence> occurrences;
	
	@OneToMany(mappedBy="request", cascade=CascadeType.ALL)
	private List<RSSFeed> feeds;
	
	public Request() {
	}

	public Request(List<Occurrence> occurrences, List<RSSFeed> feeds) {
		this.occurrences = occurrences;
		this.feeds = feeds;
	}

	public List<RSSFeed> getFeeds() {
		return feeds;
	}
	
	public long getId() {
		return id;
	}

	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	public void setFeeds(List<RSSFeed> feeds) {
		this.feeds = feeds;
	}


	public void setId(long id) {
		this.id = id;
	}

	public void setOccurrence(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}	
}
