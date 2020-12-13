package com.sgs.urlshortener.service;

import com.sgs.urlshortener.model.UrlInfo;

public interface UrlService {
    UrlInfo shorteningUrl(String longUrl);
    String getOriginalUrl(String shortUrl);
}
