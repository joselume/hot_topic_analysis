package com.joselume.topic.service;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.joselume.topic.dto.Channel;
import com.joselume.topic.dto.Item;
import com.joselume.topic.dto.Rss;
import com.joselume.topic.entity.Occurrence;
import com.joselume.topic.entity.Request;
import com.joselume.topic.repository.OccurrenceRepository;
import com.joselume.topic.repository.RequestRepository;

@ExtendWith(MockitoExtension.class)
class HotTopicAnalysisServiceTest {
	
	@Mock
	RequestRepository requestRepository; 
	@Mock
	OccurrenceRepository occurrenceRepository;
	@Mock
	RestTemplate restTemplate;
	@InjectMocks
	HotTopicAnalysisService hotTopicAnalysisService;
	
	
	@Test
	void givenRssFeedsWithNewsAndOccurrencesWhenProcessThenSaveRequestAndReturnId () {
		// Arrange
		Request request1001L = new Request();
		request1001L.setId(1001L);
	
		// URLs
		String url1 = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss";
		String url2 = "https://rss.nytimes.com/services/xml/rss/nyt/es.xml"; 
		List<String> urls = new ArrayList<>(); 
		urls.add(url1);
		urls.add(url2);
				
		// Items
		List<Item> items = new ArrayList<>();
		Item item1 = new Item();
		item1.setTitle("Obama Hillary");
		Item item2 = new Item();
		item2.setTitle("Obama Trump");
		Item item3 = new Item();
		item3.setTitle("Obama Trump");
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		// Channel
		Channel channel = new Channel();
		channel.setItem(items);
		
		// RSS
		Rss rss = new Rss();
		rss.setChannel(channel);
		
		when(restTemplate.getForObject(url1, Rss.class)).thenReturn(rss);
		when(restTemplate.getForObject(url2, Rss.class)).thenReturn(rss);
		when(requestRepository.save(Mockito.any())).thenReturn(request1001L);
				
		// Act
		long requestId = hotTopicAnalysisService.processRequest(urls);
		
		// Assert
		assertEquals(request1001L.getId(), requestId);
	}

	@Test
	void givenRequestWithOccurrencesWhenGetTopOccurrencesThenReturnOccurrences () {
		
		// Arrange
		long requestId = 1L;
		Request request = new Request();
		Optional<Request> requestOptional = Optional.of(request);
		
		List<Occurrence> occurrences = new ArrayList<>();
		occurrences.add(new Occurrence());
		occurrences.add(new Occurrence());
		occurrences.add(new Occurrence());
		
		when(requestRepository.findById(requestId)).thenReturn(requestOptional);
		when(occurrenceRepository.findFirst3ByRequestOrderByCounterDesc(request)).thenReturn(occurrences);
		
		// Act
		List<Occurrence> foundOccurrences = hotTopicAnalysisService.getTopOccurrences(1L);
		
		// Assertion
		assertEquals(foundOccurrences.size(), 3);
	}
	
	@Test
	void givenNonExistingRequestWhenGetTopOccurrencesThenReturnEmptyList () {
		
		// Arrange
		long requestId = 1L;		
		when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

		// Act
		List<Occurrence> foundOccurrences = hotTopicAnalysisService.getTopOccurrences(1L);
		
		// Assertion
		assertEquals(foundOccurrences.size(), 0);
	}

}
