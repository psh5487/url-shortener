package com.sgs.urlshortener.service;

import com.sgs.urlshortener.model.UrlInfo;
import com.sgs.urlshortener.repository.UrlInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl {

    private final UrlInfoRepository urlInfoRepository;

    public Long count() {
        return urlInfoRepository.count();
    }

    public List<UrlInfo> top5UrlInfo() {
        return urlInfoRepository.findFirst5ByOrderByClickCountDesc();
    }

    public List<UrlInfo> allUrlInfo() {
        return urlInfoRepository.findAll();
    }
}
