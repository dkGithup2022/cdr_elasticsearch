package com.dk0124.cdr.es.dao;


import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

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
