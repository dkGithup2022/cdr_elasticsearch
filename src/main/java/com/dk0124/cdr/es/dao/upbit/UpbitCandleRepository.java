package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UpbitCandleRepository extends ElasticsearchRepository<UpbitCandleDoc> {

    public UpbitCandleRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    public Class<UpbitCandleDoc> getDocType() { return UpbitCandleDoc.class; }

}
