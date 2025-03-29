package de.lenneflow.lenneflowclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RestUtil {

    private static final String ERROR_MESSAGE = "Error while getting object list from url: {} {}";
    private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);

    @Value("${lenneflow.master.token}")
    private String masterToken;

    private final RestTemplate restTemplate;

    public RestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> List<T> getForObjectList(String url, ParameterizedTypeReference<?> responseType){
        try {
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            ResponseEntity<List<T>> resp = (ResponseEntity<List<T>>) restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            return resp.getBody();
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
            return new ArrayList();
        }
    }

    public List postForObjectList(String url, Object body, Class<List> objectClass){
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, getHeaders());
            return restTemplate.exchange(url, HttpMethod.POST, entity, objectClass).getBody();
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
            return new ArrayList();
        }
    }

    public <T> T getForObject(String url, Class<T> objectClass){
        try {
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(url, HttpMethod.GET, entity, objectClass).getBody();
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
            return null;
        }
    }

    public void deleteObject(String url){
        try {
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
        }
    }

    public <T> T postForObject(String url, Object body, Class<T> objectClass){
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, getHeaders());
            return restTemplate.exchange(url, HttpMethod.POST, entity, objectClass).getBody();
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
            return null;
        }
    }

    public HttpStatusCode postForStatusCode(String url, Object body, Class objectClass){
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, getHeaders());
            return restTemplate.exchange(url, HttpMethod.POST, entity, objectClass).getStatusCode();
        } catch (RestClientException e) {
            logger.error(ERROR_MESSAGE, url, e.getMessage());
            return null;
        }
    }


    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.setBearerAuth(masterToken.trim());

        return headers;
    }

}
