package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
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
public class UpbitCandleRepository extends ElasticsearchRepository<UpbitCandleDoc> {

    public UpbitCandleRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    /*
    public List<UpbitCandleDoc> search(String indexName, Pageable pageable) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        Query searchQuery = buildSearchQuery(queryBuilder, pageable);

        return elasticsearchOperations.search(searchQuery, UpbitCandleDoc.class, indexCoordinates)
                .get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private Query buildSearchQuery(QueryBuilder queryBuilder, Pageable pageable) {
        return new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
    }
     */

}
