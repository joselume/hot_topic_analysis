package com.joselume.topic.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.joselume.topic.entity.Occurrence;
import com.joselume.topic.entity.RSSFeed;

@XmlRootElement(name = "item")
public class Item {
	
	@Column(length=2000)
	private String title;
	
	@ManyToOne
	private RSSFeed feed;
	
	@Column(length=2000)
	private  String link;
	
	@ManyToMany(mappedBy="items")
	private List<Occurrence> occurrences = new ArrayList<>(); 
	
	public Item() {
	}

	public Item(String title, RSSFeed feed, String link) {
		this.title = title;
		this.feed = feed;
		this.link = link;
	}

	public RSSFeed getFeed() {
		return feed;
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
	
	@XmlElement
	public void setLink(String link) {
		this.link = link;
	}
	
	public void setOccurrences(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}
	
	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}
}
