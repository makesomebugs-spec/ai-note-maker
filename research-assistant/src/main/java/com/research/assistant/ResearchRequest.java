package com.research.assistant;

import lombok.Data;

@Data //getters and setters
public class ResearchRequest {
	private String content;
	private String operation;//can be summarise, suggest related, such opns can be added here
}
