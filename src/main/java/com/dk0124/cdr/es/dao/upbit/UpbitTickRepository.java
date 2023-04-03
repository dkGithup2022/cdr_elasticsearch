package com.dk0124.cdr.es.dao.upbit;


import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitTickDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UpbitTickRepository extends ElasticsearchRepository<UpbitTickDoc> {

    public UpbitTickRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public Class getDocType() {
        return UpbitTickDoc.class;
    }
}
