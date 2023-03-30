package com.dk0124.cdr.es.dao.upbit.integration;


import com.dk0124.cdr.es.dao.upbit.ElasticTestContainer;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.net.URISyntaxException;

public class UpbitCandleRepositoryTest {

    @Container
    protected static ElasticsearchContainer elasticsearchContainer = new ElasticTestContainer();

    @BeforeAll
    public static void setUp() {
        elasticsearchContainer.start();
    }

    @Test
    public void empty() throws URISyntaxException, IOException {
    }
}
