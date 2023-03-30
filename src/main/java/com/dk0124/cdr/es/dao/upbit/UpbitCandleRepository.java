package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.dto.upbit.UpbitCandleDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UpbitCandleRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public UpbitCandleDto index(String indexName, UpbitCandleDto document) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return elasticsearchOperations.save(document, indexCoordinates);
    }

    public UpbitCandleDto index(String indexName, String id, UpbitCandleDto document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexQuery querysRes =  elasticsearchOperations.save(indexQuery, IndexCoordinates.of(indexName));

        if (querysRes == null )
            throw new RuntimeException("Operation response is null ");

        return (UpbitCandleDto) querysRes.getObject();
    }

    public List<UpbitCandleDto> search(String indexName, Pageable pageable) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        Query searchQuery = buildSearchQuery(queryBuilder, pageable);

        return elasticsearchOperations.search(searchQuery, UpbitCandleDto.class, indexCoordinates)
                .get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private Query buildSearchQuery(QueryBuilder queryBuilder, Pageable pageable) {
        return new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
    }

}
