package com.dk0124.cdr.es.dao.bithumb;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class BithumbCandleRespository  extends ElasticsearchRepository<BithumbCandleDoc> {
    public BithumbCandleRespository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }
}
