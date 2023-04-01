package com.dk0124.cdr.es.dao.bithumb;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class BithumbCandleRespository extends ElasticsearchRepository<BithumbCandleDoc> {
    public BithumbCandleRespository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    public Class<BithumbCandleDoc> getDocType() {
        return BithumbCandleDoc.class;
    }
}
