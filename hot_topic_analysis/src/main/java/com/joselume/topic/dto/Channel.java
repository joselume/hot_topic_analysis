package com.joselume.topic.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "channel")
public class Channel {
	private List<Item> item;


	public List<Item> getItem() {
		return item;
	}
	
	@XmlElement
	public void setItem(List<Item> item) {
		this.item = item;
	}
	
	
}
