package com.dk0124.cdr.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EsIndexOps {

    private ElasticsearchRestTemplate elasticsearchOperations;

    private final int testPort = 29200;

    public EsIndexOps() {
        RestHighLevelClient client = RestClients.create(ClientConfiguration.create("localhost:" + testPort)).rest();
        elasticsearchOperations = new ElasticsearchRestTemplate(client);
        elasticsearchOperations.setRefreshPolicy(RefreshPolicy.IMMEDIATE); // 테스트에선 refresh interval 무시됨.
    }

    public boolean deleteIndex(String index) {
        return elasticsearchOperations.indexOps(IndexCoordinates.of(index)).delete();
    }

    public void forceMerge(String index) {
        String forcemergeUrl = "http://localhost:" + testPort + "/" + index + "/_forcemerge?max_num_segments=1";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(forcemergeUrl, HttpMethod.POST, entity, String.class);

    }

    public void forceMergeAll() {
        String forcemergeUrl = "http://localhost:" + testPort + "/_forcemerge";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(forcemergeUrl, HttpMethod.POST, entity, String.class);

    }


    public void createIndexWithMappingAndSetting(String index, String mappingPath, String settingPath) throws IOException {
        Map<String, Object> mapping = readResourceAsMap(mappingPath);
        Map<String, Object> setting = readResourceAsMap(settingPath);

        Document mapDoc = Document.from(mapping);
        IndexCoordinates indexCoordinates = IndexCoordinates.of(index);
        elasticsearchOperations.indexOps(indexCoordinates).create(setting, mapDoc);
    }

    private Map<String, Object> readResourceAsMap(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        String json = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(json, HashMap.class);
    }
}
