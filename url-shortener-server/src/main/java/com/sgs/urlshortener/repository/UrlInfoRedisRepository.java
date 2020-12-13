package com.sgs.urlshortener.repository;

import com.sgs.urlshortener.model.UrlInfoRedis;
import org.springframework.data.repository.CrudRepository;

public interface UrlInfoRedisRepository extends CrudRepository<UrlInfoRedis, Long> {
    // shortUrl 에 해당하는 UrlInfoRedis 찾기
    UrlInfoRedis findByShortUrl(String shortUrl);
}
