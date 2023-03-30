package com.dk0124.cdr.es.dao.bithumb;


import com.dk0124.cdr.es.document.bithumb.BithumbTickDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BithumbTickRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    public BithumbTickDoc index(String indexName, BithumbTickDoc document) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return elasticsearchOperations.save(document, indexCoordinates);
    }

    public BithumbTickDoc index(String indexName, String id, BithumbTickDoc document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexQuery res = elasticsearchOperations.save(indexQuery, IndexCoordinates.of(indexName));

        if (res == null)
            throw new RuntimeException("Operation response is null ");

        return (BithumbTickDoc
                ) res.getObject();
    }
}
