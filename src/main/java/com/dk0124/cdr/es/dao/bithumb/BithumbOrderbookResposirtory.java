package com.dk0124.cdr.es.dao.bithumb;

import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbOrderbookDoc;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class BithumbOrderbookResposirtory extends ElasticsearchRepository <BithumbOrderbookDoc>{

    public BithumbOrderbookResposirtory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    protected IndexCoordinates genIndexCoordinate(String code) {
        return IndexCoordinates.of(EsUtils.bithumbOrderbookIndexFromCode(code));
    }

    @Override
    protected String getDocStrCode(BithumbOrderbookDoc doc) {
        return doc.getCode();
    }

    @Override
    public IndexQuery buildIndexQuery(BithumbOrderbookDoc doc) throws JsonProcessingException {
        IndexQuery indexQuery = new IndexQueryBuilder()
            .withId(EsUtils.generateId(doc)) // 문서 아이디 지정
            .withObject(doc)
            .build();
        return indexQuery;
    }

    @Override
    public Class getDocType() {
        return BithumbOrderbookDoc.class;
    }
}
