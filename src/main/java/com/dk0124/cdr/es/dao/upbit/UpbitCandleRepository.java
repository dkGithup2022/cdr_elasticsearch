package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class UpbitCandleRepository extends ElasticsearchRepository<UpbitCandleDoc> {

	public UpbitCandleRepository(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	@Override
	protected IndexCoordinates genIndexCoordinate(String code) {
		return IndexCoordinates.of(EsUtils.upbitCandleIndexFromCode(code));
	}

	@Override
	protected String getDocStrCode(UpbitCandleDoc doc) {
		return doc.getMarket();
	}

	@Override
	public IndexQuery buildIndexQuery(UpbitCandleDoc doc) throws JsonProcessingException {
		IndexQuery indexQuery = new IndexQueryBuilder()
			.withId(EsUtils.generateId(doc)) // 문서 아이디 지정
			.withObject(doc)
			.build();
		return indexQuery;
	}

	public Class<UpbitCandleDoc> getDocType() {
		return UpbitCandleDoc.class;
	}

}
