package com.sgs.urlshortener.controller;

import com.sgs.urlshortener.model.UrlInfo;
import com.sgs.urlshortener.service.AnalysisServiceImpl;
import com.sgs.urlshortener.service.UrlServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class UrlRestController {

    private final UrlServiceImpl shorteningServiceImpl;
    private final AnalysisServiceImpl analysisServiceImpl;

    @PostMapping("/shortening")
    public ResponseEntity shorteningUrl(@RequestBody Map<String, String> request) {
        String longUrl = request.get("long_url");
        UrlInfo urlInfo = shorteningServiceImpl.shorteningUrl(longUrl);

        return new ResponseEntity(urlInfo, HttpStatus.OK);
    }

    @PostMapping("/original")
    public ResponseEntity originalUrl(@RequestBody Map<String, String> request) {
        String shortUrl = request.get("short_url");
        String originalUrl = shorteningServiceImpl.getOriginalUrl(shortUrl);

        if(originalUrl.equals("")) {
            return new ResponseEntity("Not Found Url", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(originalUrl, HttpStatus.OK);
    }

    @GetMapping("/analysis")
    public ResponseEntity analysis() {
        AnalysisResult analysisResult = AnalysisResult.builder()
                .totalCount(analysisServiceImpl.count())
                .top5UrlInfoList(analysisServiceImpl.top5UrlInfo())
                .allUrlInfoList(analysisServiceImpl.allUrlInfo())
                .build();
        return new ResponseEntity(analysisResult, HttpStatus.OK);
    }
}
