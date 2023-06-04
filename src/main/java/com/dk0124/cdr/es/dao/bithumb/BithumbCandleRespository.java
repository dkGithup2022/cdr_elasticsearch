package com.dk0124.cdr.es.dao.bithumb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BithumbCandleRespository extends ElasticsearchRepository<BithumbCandleDoc> {

	@Autowired
	private ObjectMapper objectMapper;

	public BithumbCandleRespository(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	@Override
	protected IndexCoordinates genIndexCoordinate(String code) {
		return IndexCoordinates.of(EsUtils.bithumbCandleIndexFromCode(code));
	}

	@Override
	protected String getDocStrCode(BithumbCandleDoc doc) {
		return doc.getCode();
	}

	@Override
	public IndexQuery buildIndexQuery(BithumbCandleDoc doc) throws JsonProcessingException {

		IndexQuery indexQuery = new IndexQueryBuilder()
			.withId(EsUtils.generateId(doc)) // 문서 아이디 지정
			.withObject(doc)
			.build();
		return indexQuery;
	}

	@Autowired
	public Class<BithumbCandleDoc> getDocType() {
		return BithumbCandleDoc.class;
	}

}
