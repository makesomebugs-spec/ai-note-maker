package com.research.assistant;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;



@Service
public class ResearchService {
	//inject values from app.props
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	//webclient
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	public ResearchService(WebClient.Builder webClientBuilder,ObjectMapper objectMapper) {
		this.webClient = webClientBuilder.build();
		this.objectMapper=objectMapper;
	}
	//here whichever 3rd party apis respond
	public Mono<String> processContent(ResearchRequest request) {
		//build prompt
		
		String prompt=buildPrompt(request);
		//query ai model api
		//to look like body of request in postman api json
		Map<String, Object> requestBody=Map.of(//req body
				"contents",new Object[] {
						Map.of("parts",new Object[] {
								Map.of("text",prompt)
						})
				}
		);// for gemini api, config the project for the model
		return webClient.post()
				.uri(geminiApiUrl+geminiApiKey)
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.map(this::extractTextFromResponse);
		
		//parse response
		//return response
		//sep m() for each
		
	}
	private String extractTextFromResponse(String response) {
		try {
			//pass response using a class
			//maps json to object geminiReponse
			GeminiResponse geminiResponse=objectMapper.readValue(response,GeminiResponse.class);
			if(geminiResponse.getCandidates()!=null&&!geminiResponse.getCandidates().isEmpty()) {
				GeminiResponse.Candidate firstCandidate=geminiResponse.getCandidates().get(0);
				if(firstCandidate.getContent()!=null&&
						firstCandidate.getContent().getParts()!=null&&
						!firstCandidate.getContent().getParts().isEmpty()) {
					return firstCandidate.getContent().getParts().get(0).getText();
				}
			}
			return "No content found in response";
		}
		catch (Exception e) {
			return "Error Parsing"+e.getMessage();
		}
	}
	private String buildPrompt(ResearchRequest request) {
		StringBuilder prompt=new StringBuilder();
		switch(request.getOperation()) {//from getter
		case "summarize":
			prompt.append("Provide a clear and concise summary of the following text in a few sentences\n\n");
			break;
		case "suggest":
			prompt.append("Based on the following content, suggest related topics and further reading. Format the response with clear headings and bullet points:\n\n");
			break;
		default:
			throw new IllegalArgumentException("Unknown Operation: "+request.getOperation());
		}
		prompt.append(request.getContent());
		return prompt.toString();
	}
}
