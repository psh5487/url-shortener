package com.sgs.urlshortener.controller;

import com.sgs.urlshortener.model.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {

    private Long totalCount;

    private List<UrlInfo> top5UrlInfoList;

    private List<UrlInfo> allUrlInfoList;
}
