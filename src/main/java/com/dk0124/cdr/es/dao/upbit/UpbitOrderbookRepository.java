package com.dk0124.cdr.es.dao.upbit;


import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitOrderbookDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UpbitOrderbookRepository extends ElasticsearchRepository<UpbitOrderbookDoc> {

    public UpbitOrderbookRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public Class getDocType() {
        return UpbitOrderbookDoc.class;
    }
}
