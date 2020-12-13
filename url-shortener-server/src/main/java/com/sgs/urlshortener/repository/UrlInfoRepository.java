package com.sgs.urlshortener.repository;

import com.sgs.urlshortener.model.UrlInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlInfoRepository extends JpaRepository<UrlInfo, Long> {
    // 저장된 총 UrlInfo 수
    long count();

    // 저장된 모든 UrlInfo 가져오기
    List<UrlInfo> findAll();

    // 총 클릭 5위 안의 UrlInfo
    List<UrlInfo> findFirst5ByOrderByClickCountDesc();

    // longUrl 에 해당하는 UrlInfo 찾기
    Optional<UrlInfo> findByLongUrl(String longUrl);

    // shortUrl 에 해당하는 UrlInfo 찾기
    Optional<UrlInfo> findByShortUrl(String shortUrl);

    // id 에 해당하는 UrlInfo 찾기
    UrlInfo findUrlInfoById(Long id);
}
