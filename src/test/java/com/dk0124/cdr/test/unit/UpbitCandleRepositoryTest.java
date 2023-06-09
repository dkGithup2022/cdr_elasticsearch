package com.dk0124.cdr.test.unit;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.es.dao.upbit.UpbitCandleRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UpbitCandleRepositoryTest {

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private UpbitCandleRepository candleRepository;

    @Test
    void empty() {
        assertNotNull(candleRepository);
    }

    @ParameterizedTest()
    @MethodSource("get_index_names")
    void testIndex(String indexName) {
        // given
        UpbitCandleDoc document = UpbitCandleDoc.builder().highPrice(0.0).highPrice(0.0).market("CODE").build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);

        // when
        when(elasticsearchOperations.save(document, indexCoordinates)).thenReturn(document);
        UpbitCandleDoc result = candleRepository.index(indexName, document);

        // then
        assertEquals(document, result);
    }


    @ParameterizedTest()
    @MethodSource("get_index_names")
    void testIndexWithId(String indexName) {
        // given
        UpbitCandleDoc document = UpbitCandleDoc.builder().highPrice(0.0).highPrice(0.0).market("CODE").build();
        String id = indexName + "_id";

        // when
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        when(elasticsearchOperations.index(any(),any())).thenReturn(id);

        UpbitCandleDoc result = candleRepository.index(indexName, id, document);

        // then
        assertEquals(document, result);

    }

    static Stream<Arguments> get_index_names() {
        String UPBIT_CANDLE_PREFIX = "upbit_candle";
        return Arrays.stream(UpbitCoinCode.values())
                .map(code -> String.join("_", UPBIT_CANDLE_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_")))
                .map(Arguments::of);
    }
}