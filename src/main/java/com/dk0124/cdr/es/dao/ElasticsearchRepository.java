package com.dk0124.cdr.es.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public abstract class ElasticsearchRepository<T> {
    private final ElasticsearchOperations elasticsearchOperations;

    public T index(String indexName, T document) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return elasticsearchOperations.save(document, indexCoordinates);
    }

    public T index(String indexName, String id, T document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        String response = elasticsearchOperations.index(indexQuery, indexCoordinates);

        if (response == null) {
            throw new RuntimeException("Operation response is null");
        }

        return (T) document;
    }


    public Page<T> findAll(String indexName, Pageable pageable) {

        Query query = Query.findAll();
        query.setPageable(pageable);

        SearchHits<T> searchHits = elasticsearchOperations.search(query, getDocType(), IndexCoordinates.of(indexName));
        SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        return (Page<T>) SearchHitSupport.unwrapSearchHits(page);
    }

    public abstract Class getDocType();
}
