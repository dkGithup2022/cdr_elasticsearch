package com.dk0124.cdr.es.dao.bithumb;


import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbTickDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class BithumbTickRepository extends ElasticsearchRepository<BithumbTickDoc> {

    public BithumbTickRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public Class getDocType() {
        return BithumbTickDoc.class;
    }
}
