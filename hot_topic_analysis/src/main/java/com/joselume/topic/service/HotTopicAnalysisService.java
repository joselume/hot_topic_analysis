package com.joselume.topic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.joselume.topic.constant.ExcludedWords;
import com.joselume.topic.dto.Item;
import com.joselume.topic.dto.Rss;
import com.joselume.topic.entity.News;
import com.joselume.topic.entity.Occurrence;
import com.joselume.topic.entity.RSSFeed;
import com.joselume.topic.entity.Request;
import com.joselume.topic.exception.NoOccurrencesToGenerate;
import com.joselume.topic.repository.OccurrenceRepository;
import com.joselume.topic.repository.RequestRepository;

@Service
public class HotTopicAnalysisService {
	
	private final String EXCLUSION_REGEX = "[^a-zA-Z]+";
	
	private static final Logger log = LoggerFactory.getLogger(HotTopicAnalysisService.class);
	
	private final RequestRepository requestRepository; 
	
	private final OccurrenceRepository occurrenceRepository;
		
	@Autowired
	private final RestTemplate restTemplate;

	public HotTopicAnalysisService(RequestRepository requestRepository, OccurrenceRepository occurrenceRepository, RestTemplate restTemplate) {
		this.requestRepository = requestRepository;
		this.occurrenceRepository = occurrenceRepository;
		this.restTemplate = restTemplate;
	}	
	
	/**
	 * Analysis several RSS feeds to find hot topics
	 * @param RSS feeds URLs
	 * @return id for the request
	 */
	public long processRequest (List<String> urls) {
		
		// Get the information of every RSS Feed
 		Request request = new Request();
		List<RSSFeed> rssFeeds =  this.fillRSSFeeds(urls, request);
		request.setFeeds(rssFeeds);
		
		// Find the number of word occurrences 
		Map <String, Occurrence> occurrences = this.findWordOccurrences(request, rssFeeds); 
		request.setOccurrence(new ArrayList<Occurrence>(occurrences.values()));
		
		Request savedRequest = new Request();
		
		if (request.getOccurrences().size() > 0) {
			savedRequest = requestRepository.save(request); 
		} else {
			throw new NoOccurrencesToGenerate ("There are not hot topics for the given URLs");
		}
				
		return savedRequest.getId();
	}
	
	/**
	 * Get the top three occurrence matches
	 * @param id of the request
	 */
	public List<Occurrence> getTopOccurrences(Long id) {
		List<Occurrence> occurrences = new ArrayList<>();
		
		Optional<Request> request = requestRepository.findById(id);
		
		if (request.isPresent()) {
			occurrences = occurrenceRepository.findFirst3ByRequestOrderByCounterDesc(request.get());
		} else {
			log.debug("The request Id does not exists");
		}
		return occurrences;
	}
	
	/**
	 * Find word occurrences iterating through the list of rssFeeds using threads
	 * @param List of RSSFeed objects
	 * @return The occurrences for every word
	 */
	private Map <String, Occurrence> findWordOccurrences (Request request, List<RSSFeed> rssFeeds) {
		
		Map <String, Occurrence> occurrences = new ConcurrentHashMap<String, Occurrence>();
		
		for (RSSFeed rssFeed : rssFeeds) {
			
			for (News news : rssFeed.getNews()) {
				String title = news.getTitle().toLowerCase();
				String [] words = title.split(" ");
				
				for (String word : words) {
					if (! this.excludeWord(word)) {
						if (occurrences.containsKey(word)) {
							Occurrence occurrence = occurrences.get(word);
							occurrence.setCounter(occurrence.getCounter() + 1);
							occurrence.addNews(news);
						} else {
							Occurrence occurrence = new Occurrence(request, word, 1, new ArrayList<>());
							occurrence.addNews(news);
							occurrences.put(word, occurrence);
							news.addOccurrence(occurrence);
						}
					}
				}
			}
		}				
		return occurrences;
	}
	
	/**
	 * Determine if a word should be excluded of the process
	 * @param word
	 * @return
	 */
	private boolean excludeWord (String word) {
		boolean exclude = false;
		if (ExcludedWords.contains(word) || word.matches(this.EXCLUSION_REGEX)) {
			exclude = true;
		}
		return exclude;
	}
	
	/**
	 * Get the RSS Feed data for every URL
	 * @param urls
	 * @return
	 */
	private List<RSSFeed> fillRSSFeeds (List<String> urls, Request request) {
		
		List<RSSFeed> rssFeeds = new ArrayList<RSSFeed>();
		
		// HTTP Request using threads
		ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(20);
        Collection<Callable<RSSFeed>> callables = new ArrayList<>();
		for (String url : urls) {
			callables.add(() -> this.RSSFeedHttpRequest(request, url));
		}
		
		// Retrieve responses 
		try {
			List<Future<RSSFeed>> RSSFeedFutures = executor.invokeAll(callables);
			for (Future<RSSFeed> rssFeedFuture : RSSFeedFutures) {		
					rssFeeds.add(rssFeedFuture.get());
		    }
		} catch (InterruptedException | ExecutionException e) {
			log.debug(e.getMessage());
		}
		
		return rssFeeds;
	}
	
	/**
	 * Get the RSS Feed information by the sending an HTTP Request
	 * @param url of the RSS Feed
	 * @return RSS object
	 */
	private RSSFeed RSSFeedHttpRequest(Request request, String url) {		
		Rss rss = restTemplate.getForObject(url, Rss.class);
		
		// Fill the domain objects
		RSSFeed rssFeed = new RSSFeed();
		rssFeed.setRequest(request);
		rssFeed.setUrl(url);
		
		List<News> newsList = new ArrayList<>() ;
		for (Item item : rss.getChannel().getItem()) {
			News news = new News(item.getTitle(), rssFeed, item.getLink(), new ArrayList<>());
			newsList.add(news);
		}
		rssFeed.setNews(newsList);
		
		return rssFeed;
	}
}
