package com.sgs.urlshortener.service;

import com.sgs.urlshortener.model.UrlInfo;
import com.sgs.urlshortener.model.UrlInfoRedis;
import com.sgs.urlshortener.repository.UrlInfoRedisRepository;
import com.sgs.urlshortener.repository.UrlInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlServiceImpl implements UrlService {

    private final UrlInfoRepository urlInfoRepository;
    private final UrlInfoRedisRepository urlInfoRedisRepository;

    static final char[] BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    @Override
    public UrlInfo shorteningUrl(String longUrl) {

        // DB 에 이미 저장된 Url 인지 확인
        Optional<UrlInfo> urlInfo = urlInfoRepository.findByLongUrl(longUrl);

        // ? : 스트림을 사용하여 코드를 짠다면...
        if(urlInfo.isPresent()) {  // 1) 이미 저장돼 있다면
            // Click Count + 1 하고, DB 에 Update 후 반환
            UrlInfo updatedUrlInfo = plusClickCount(urlInfo.get());

            // Click Count 값이 10 이 되면, 캐시에 올리기
            if(updatedUrlInfo.getClickCount() == 10) {
                saveInRedisCache(updatedUrlInfo);
            }
            // 최신 UrlInfo 객체 리턴
            return updatedUrlInfo;
        } else { // 2) 저장돼 있지 않다면
            // DB에 저장
            return saveNewUrlInfo(longUrl);
        }
    }

    private UrlInfo saveNewUrlInfo(String longUrl) {

        // longUrl DB 에 저장 후, id 받기
        UrlInfo urlInfo = UrlInfo.builder()
                .longUrl(longUrl)
                .clickCount(1L)
                .build();

        UrlInfo savedUrlInfo = urlInfoRepository.save(urlInfo);

        // id를 base62로 인코딩하여, shortUrl 만들기
        // 만든 shortUrl DB 에 저장
        UrlInfo newUrlInfo = urlInfoRepository.findUrlInfoById(savedUrlInfo.getId());
        newUrlInfo.setShortUrl(encode(newUrlInfo.getId()));

        // 저장한 UrlInfo 객체 리턴
        return urlInfoRepository.save(newUrlInfo);
    }

    @Override
    public String getOriginalUrl(String shortUrl) {

        // Redis 에 저장되어있는지 먼저 확인
        UrlInfoRedis urlInfoRedis = urlInfoRedisRepository.findByShortUrl(shortUrl);

        if(urlInfoRedis != null) { // 1) Redis 에 이미 저장돼 있다면
            log.info("Url Exist in Redis");

            // DB의 Click Count + 1 하고, DB 에 Update (비동기 처리)
            CompletableFuture.runAsync(() -> {
                Optional<UrlInfo> urlInfo = urlInfoRepository.findByShortUrl(shortUrl);
                plusClickCount(urlInfo.get());
                log.info("Plus Count in MySQL");
            });

            // Redis 에 저장된 UrlInfoRedis 의 longUrl 리턴
            String longUrl = urlInfoRedis.getLongUrl();
            log.info(longUrl + " from Redis");
            return longUrl;

        } else { // 2) Redis 에 없다면
            // DB에 해당 Url 이 저장돼 있는지 확인
            Long id = decode(shortUrl);
            UrlInfo urlInfo = urlInfoRepository.findUrlInfoById(id);

            // 2-1) DB에 저장돼 있을 경우
            if(urlInfo != null) {
                // Click Count + 1 하고, DB 에 Update 후 반환
                UrlInfo updatedUrlInfo = plusClickCount(urlInfo);

                // Click Count 값이 10 이되면, 캐시에 올리기
                if(updatedUrlInfo.getClickCount() == 10) {
                    saveInRedisCache(updatedUrlInfo);
                }
                // 최신 UrlInfo 객체 내 longUrl 리턴
                return updatedUrlInfo.getLongUrl();
            }
        }

        // 2-2) DB에 저장돼 있지 않을 경우, "" 리턴
        return "";
    }

    // Click Count + 1 하고, DB 에 Update
    private UrlInfo plusClickCount(UrlInfo urlInfo) {

        Long plusCount = urlInfo.getClickCount() + 1;
        urlInfo.setClickCount(plusCount);

        return urlInfoRepository.save(urlInfo);
    }

    // Redis Cache 에 UrlInfo 올리기
    private void saveInRedisCache(UrlInfo urlInfo) {

        log.info("Save in Redis");
        UrlInfoRedis urlInfoRedis = UrlInfoRedis.builder()
                .shortUrl(urlInfo.getShortUrl())
                .longUrl(urlInfo.getLongUrl())
                .build();

        urlInfoRedisRepository.save(urlInfoRedis);
    }

    /*
     * Encode to base62 string
     */
    public static String encode(long value) {
        final StringBuilder sb = new StringBuilder();

        do {
            int i = (int)(value % 62);
            sb.append(BASE62[i]);
            value /= 62;
        } while (value > 0);

        return sb.toString();
    }

    /*
     * Decode to Integer
     */
    public static long decode(String value) {
        long result = 0;
        long power = 1;

        for(int i = 0; i < value.length(); i++) {
            int digit = new String(BASE62).indexOf(value.charAt(i));
            result += digit * power;
            power *= 62;
        }
        return result;
    }
}
