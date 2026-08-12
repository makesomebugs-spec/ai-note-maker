package com.research.assistant;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true) //ignore other properties that might be in the response
public class GeminiResponse {
	private List<Candidate> candidates;
	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Candidate{//should be public to be accessed by service cls
		private Content content;
	}
	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Content{
		private List<Part> parts;
	}
	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Part{
		private String text;;
	}
	
}
