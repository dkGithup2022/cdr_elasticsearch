package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

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
