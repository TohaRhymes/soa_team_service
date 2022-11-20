package com.example.team_soa.service;

import org.springframework.http.ResponseEntity;

public interface CollectionService {
    public ResponseEntity<String> makeDepressiveByHumanId(Long id);
}
