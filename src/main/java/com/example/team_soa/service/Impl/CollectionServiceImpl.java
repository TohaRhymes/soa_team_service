package com.example.team_soa.service.Impl;

import com.example.team_soa.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Slf4j
@Service
public class CollectionServiceImpl implements CollectionService {

    private final RestTemplate restTemplate;
    private final String baseURL = "http://localhost:8080";


    @Autowired
    public CollectionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> makeDepressiveByHumanId(Long id) {
        String url = new StringBuilder().append(baseURL).append(String.format("/v1/collection_api/humans/%d/mood/%s", id, "sorrow")).toString();
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        return response;
    }
}
