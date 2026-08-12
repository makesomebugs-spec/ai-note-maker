package com.research.assistant;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RequestMapping("/api/research") //mapping endpoints in these wires
@RestController
@CrossOrigin(origins="*")
//make constructor with all instance variables
@AllArgsConstructor 
public class ResearchController {
	private final ResearchService researchService;
	@PostMapping("/process")//post req
	public Mono<String> processContent(@RequestBody ResearchRequest request){
		return researchService.processContent(request);
	}
//	public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
//		String result=researchService.processContent(request);
//		return ResponseEntity.ok(result);
//	}
}