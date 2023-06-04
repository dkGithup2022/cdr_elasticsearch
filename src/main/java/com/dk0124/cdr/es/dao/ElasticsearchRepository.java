package com.dk0124.cdr.es.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

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

		// 쿼리 직접 작성 시, entity string 이 매핑이 안됨 .
		if (response == null) {
			throw new RuntimeException("Operation response is null");
		}

		return (T)document;
	}

	public Page<T> findAll(String indexName, Pageable pageable) {

		Query query = Query.findAll();
		query.setPageable(pageable);

		SearchHits<T> searchHits = elasticsearchOperations.search(query, getDocType(), IndexCoordinates.of(indexName));
		SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

		return (Page<T>)SearchHitSupport.unwrapSearchHits(page);
	}

	/**
	 *  bulk index 요청
	 * @param documents
	 * @return
	 * @throws JsonProcessingException
	 *
	 *
	 * [ 설명 ]
	 *
	 *  spring data elasticsearch 에서는 하나의 bulk req 에서 여러 인덱스에 대한 연산을 보내지 못하게 해놨음 .
	 *  현재 구조가 각 document 의 인덱스를 Util 클래스의 함수로 만들어서 넣기 때문에 bulk 요청을 위한 구분이 필요함.
	 *
	 *
	 * [ 개선점 & 마음에 안드는 점 ]
	 * 전반적으로 쓸데없이 복잡해 보이는 요소가 있음 .
	 * bulk 요청만을 위한 abstract 함수가 2개나 생겼다는 점.
	 * 그리고, bulk 요청을 인덱스에 따라 구분자로 나눠서 보내는 과정에서 코드가 장황해 진다는 점이 있음.
	 *
	 * 이거 좀더 찾아보고 진짜 우회하는 기능 조차도 없다면 spring data elasticsearch issue 란에 건의할 수는 있을듯 ?( 일단은 생각만 )
	 */
	public List<IndexedObjectInformation> bulkIndex(List<T> documents) throws JsonProcessingException {
		// check if empty
		if (documents.size() == 0)
			return null;

		HashMap<String, ArrayList<IndexQuery>> queryMap = new HashMap<>();

		// seperate bulk req by T.getCode() or T.getMarket();
		documents.stream().forEach(
			(T doc) -> {
				// check if instance of code ;
				checkType(doc);
				try {
					queryMap.computeIfAbsent(getDocStrCode(doc), e -> new ArrayList<IndexQuery>())
						.add(buildIndexQuery(doc));
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			});

		List<IndexedObjectInformation> result = new ArrayList<>();

		// send each bulk;
		for (String code : queryMap.keySet())
			result.addAll(elasticsearchOperations.bulkIndex(queryMap.get(code), genIndexCoordinate(code)));


		return result;
	}

	protected abstract IndexCoordinates genIndexCoordinate(String code);

	private void checkType(T doc) {
		if (getDocType().isInstance(doc))
			return;
		throw new RuntimeException(
			"There is type misMatch : generic T does not contain document |  T : " + getDocType() + " | doc: "
				+ doc.getClass());
	}

	protected abstract String getDocStrCode(T doc);

	public abstract IndexQuery buildIndexQuery(T doc) throws JsonProcessingException;

	public abstract Class getDocType();

}
