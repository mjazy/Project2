package com.NewsBrowser.Server.Service;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Named
public class NewsAPINewsFetcher implements NewsFetcherInterface {
	
	@Inject
	RestTemplate restTemplate;	
	
	@Inject
	NewsAPILoggingValidator newsAPILoggingValidator;
	
	@Inject
	Logger logger;
	
	
	private String url;
	private final String APIKEY = "dc98f2c2ddfe44d0b8102025c394cd08";
	

	/**
	 * Related to creation of ResponseEntity.
	 */
	
	private HttpEntity<?> httpEntity(){
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(httpHeaders);
	}
		
	
	@Override
	public String fetchNews(String country, String category) {
		setFetchNewsUrl(country, category);
		ResponseEntity<String> responseEntity = restTemplate.exchange(this.url, HttpMethod.GET, httpEntity(), String.class);
		
		if(newsAPILoggingValidator.shouldDebugLogBeAdded(responseEntity) == true) {
			logger.debug("{} {}", responseEntity.getStatusCode(), responseEntity.getBody());		
		}
		else if (newsAPILoggingValidator.shouldErrorLogBeAdded(responseEntity) == true) {
			logger.error("{} {}", responseEntity.getStatusCode(), responseEntity.getBody());		
		}
		
		String responseBody = responseEntity.getBody();
		return responseBody;
	}

	

	/**
	 * Sets URL which can be used to send GET request related to fetching news.
	 * @param country should be supplied in ISO 3166-1 code e.g. "pl".
	 * @param category should be one of "business, entertainment, general, health, science, sports, technology".
	 */ 
	private void setFetchNewsUrl(String country, String category) {
		this.url = String.format("https://newsapi.org/v2/top-headlines?country=%s&category=%s&apiKey=%s", country, category, this.APIKEY);
	}
	
	
	

}
