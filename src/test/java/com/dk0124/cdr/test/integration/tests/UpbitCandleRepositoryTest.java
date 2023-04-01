package com.dk0124.cdr.test.integration.tests;


import com.dk0124.cdr.es.dao.bithumb.BithumbCandleRespository;
import com.dk0124.cdr.test.integration.ElasticTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpbitCandleRepositoryTest {

    @Container
    private static ElasticTestContainer esContainer = new ElasticTestContainer();

    @Autowired
    private BithumbCandleRespository bithumbCandleRespository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    static void setUp() {
        esContainer.start();
    }

    @Test
    public void empty() {
        assertNotNull(elasticsearchOperations);
        assertNotNull(bithumbCandleRespository);

    }
}
