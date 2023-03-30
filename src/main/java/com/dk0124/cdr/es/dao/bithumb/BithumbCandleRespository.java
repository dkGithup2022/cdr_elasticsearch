package com.dk0124.cdr.es.dao.bithumb;

import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BithumbCandleRespository {

    private final ElasticsearchOperations elasticsearchOperations;

    public BithumbCandleDoc index(String indexName, BithumbCandleDoc document) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return elasticsearchOperations.save(document, indexCoordinates);
    }

    public BithumbCandleDoc index(String indexName, String id, BithumbCandleDoc document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexQuery q=  elasticsearchOperations.save(indexQuery, IndexCoordinates.of(indexName));

        if (q == null )
            throw new RuntimeException("Operation response is null ");

        return (BithumbCandleDoc) q.getObject();
    }
}
