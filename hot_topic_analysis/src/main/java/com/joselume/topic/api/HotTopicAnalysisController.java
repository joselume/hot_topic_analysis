package com.joselume.topic.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joselume.topic.entity.Occurrence;
import com.joselume.topic.service.HotTopicAnalysisService;

@RestController
public class HotTopicAnalysisController {
		
	private final HotTopicAnalysisService service;

	public HotTopicAnalysisController(HotTopicAnalysisService service) {
		this.service = service;
	}
	
	@PostMapping("/analyse/new")
	public long analyseRSS (@RequestBody List<String> urls) {
		return this.service.processRequest(urls);
	}
	
	@GetMapping("/frequency/{id}")
	List<Occurrence> getTopThreeMatches(@PathVariable Long id ){
		return service.getTopOccurrences(id);
	}
}
