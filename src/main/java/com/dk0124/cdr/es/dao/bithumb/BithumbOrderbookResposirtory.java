package com.dk0124.cdr.es.dao.bithumb;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbOrderbookDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class BithumbOrderbookResposirtory extends ElasticsearchRepository <BithumbOrderbookDoc>{

    public BithumbOrderbookResposirtory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public Class getDocType() {
        return BithumbOrderbookDoc.class;
    }
}
