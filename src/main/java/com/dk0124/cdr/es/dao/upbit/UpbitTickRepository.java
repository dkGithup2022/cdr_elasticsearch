package com.dk0124.cdr.es.dao.upbit;

import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.dao.ElasticsearchRepository;
import com.dk0124.cdr.es.document.upbit.UpbitTickDoc;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class UpbitTickRepository extends ElasticsearchRepository<UpbitTickDoc> {

	public UpbitTickRepository(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	@Override
	protected IndexCoordinates genIndexCoordinate(String code) {
		return IndexCoordinates.of(EsUtils.upbitTickIndexFromCode(code));

	}

	@Override
	protected String getDocStrCode(UpbitTickDoc doc) {
		return doc.getCode();
	}

	@Override
	public IndexQuery buildIndexQuery(UpbitTickDoc doc) throws JsonProcessingException {
		IndexQuery indexQuery = new IndexQueryBuilder()
			.withId(EsUtils.generateId(doc)) // 문서 아이디 지정
			.withObject(doc)
			.build();
		return indexQuery;
	}

	@Override
	public Class getDocType() {
		return UpbitTickDoc.class;
	}
}
