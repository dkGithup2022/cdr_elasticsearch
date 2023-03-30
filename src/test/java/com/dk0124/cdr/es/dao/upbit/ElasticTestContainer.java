package com.dk0124.cdr.es.dao.upbit;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class ElasticTestContainer extends ElasticsearchContainer {
    private static final String DOCKER_ELASTIC = "docker.elastic.co/elasticsearch/elasticsearch:7.17.6";

    private static final String CLUSTER_NAME = "sample-cluster";

    private static final String ELASTIC_SEARCH = "elasticsearch";

    public ElasticTestContainer() {
        super(DOCKER_ELASTIC);
        this.addFixedExposedPort(9292, 9292);
        this.addFixedExposedPort(9393, 9393);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
