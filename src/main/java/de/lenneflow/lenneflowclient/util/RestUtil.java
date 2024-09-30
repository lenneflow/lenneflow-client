package de.lenneflow.lenneflowclient.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestUtil {

    private final RestTemplate restTemplate;

    public RestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> List<T> getForObjectList(String url, Class<List> objectClass){
        try {
            return restTemplate.getForObject(url, objectClass);
        } catch (RestClientException e) {
            //TODO logger
            return new ArrayList();
        }
    }

    public <T> List<T> postForObjectList(String url, Object body, Class<List> objectClass){
        try {
            return restTemplate.postForObject(url, body, objectClass);
        } catch (RestClientException e) {
            //TODO logger
            return new ArrayList();
        }
    }

    public <T> T getForObject(String url, Class<T> objectClass){
        try {
            return restTemplate.getForObject(url, objectClass);
        } catch (RestClientException e) {
            //TODO logger
            return null;
        }
    }

    public void deleteObject(String url){
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            //TODO logger
        }
    }

    public <T> T postForObject(String url, Object body, Class<T> objectClass){
        try {
            return restTemplate.postForObject(url, body, objectClass);
        } catch (RestClientException e) {
            //TODO logger
            return null;
        }
    }

}
