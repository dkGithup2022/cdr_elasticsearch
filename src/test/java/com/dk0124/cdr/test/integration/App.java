package com.dk0124.cdr.test.integration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dk0124.cdr.test.integration", "com.dk0124.cdr.es.dao"})
public class App {

    @Configuration
    public static class ClientConfig {
        @Value("${spring.elasticsearch.host}")
        private String host;

        @Value("${spring.elasticsearch.port}")
        private int port;

        @Bean
        public RestHighLevelClient elasticsearchClient() {
            final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(host + ":" + port)
                    .build();

            return RestClients.create(clientConfiguration).rest();
        }

        @Bean
        public ElasticsearchOperations elasticsearchOperations() {
            return new ElasticsearchRestTemplate(elasticsearchClient());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
