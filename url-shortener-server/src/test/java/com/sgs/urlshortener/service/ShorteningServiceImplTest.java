package com.sgs.urlshortener.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShorteningServiceImplTest {
    @Autowired
    private UrlServiceImpl shorteningServiceImpl;

    @Test
    void shorteningUrl() {
    }

    @Test
    void encode() {
        String encoded = UrlServiceImpl.encode(123l);
        System.out.println(encoded);
    }

    @Test
    void decode() {
    }
}