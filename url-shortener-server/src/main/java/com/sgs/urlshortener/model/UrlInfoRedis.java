package com.sgs.urlshortener.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("urlInfos")
@Data
@Builder
public class UrlInfoRedis implements Serializable {
    @Id
    private Long id;

    private String longUrl;

    @Indexed
    private String shortUrl;
}
