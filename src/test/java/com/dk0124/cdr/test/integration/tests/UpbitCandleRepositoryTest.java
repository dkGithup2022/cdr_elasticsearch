package com.dk0124.cdr.test.integration.tests;


import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.es.dao.upbit.UpbitCandleRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.dk0124.cdr.test.integration.EsIndexOps;
import com.dk0124.cdr.test.integration.ElasticTestContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpbitCandleRepositoryTest {

    @Container
    private static ElasticTestContainer esContainer = new ElasticTestContainer();

    @Autowired
    private UpbitCandleRepository candleRepository;

    private static EsIndexOps esIndexOps = new EsIndexOps();

    @BeforeAll
    static void setUp() {
        esContainer.start();
    }

    @BeforeEach
    public void reCreateIndex() throws IOException {
        String sp = "elastic/upbit/tick_setting.json";
        String mp = "elastic/upbit/tick_mapping.json";
        String prefix = "upbit_tick_";

        for (UpbitCoinCode code : UpbitCoinCode.values()) {
            String[] splitted = code.toString().toLowerCase(Locale.ROOT).split("-");
            String idx = prefix + String.join("_", splitted);
            esIndexOps.deleteIndex(idx);
            esIndexOps.createIndexWithMappingAndSetting(idx, mp, sp);
        }
    }

    @Test
    public void empty() {
        assertNotNull(candleRepository);
    }

    @Test
    @DisplayName("같은 아이디에 저장")
    public void saveSamePW() throws InterruptedException {
        UpbitCoinCode code = UpbitCoinCode.KRW_ADA;
        String index = "upbit_candle_krw_ada";

        UpbitCandleDoc doc1 = UpbitCandleDoc.builder().timestamp(1L).build();
        UpbitCandleDoc doc2 = UpbitCandleDoc.builder().timestamp(2L).build();

        candleRepository.index(index, "same", doc1);
        candleRepository.index(index, "same", doc2);

        Thread.sleep(1000); // TODO : native query 로 forceMerge 가능한지 확인 필요.

        Page<UpbitCandleDoc> page = candleRepository.findAll(index, PageRequest.of(0, 100));
        assertEquals(1, page.getContent().size());

    }

    @Test
    @DisplayName(" 저장된 칼럼 확인 ")
    public void checkAllColumn() throws InterruptedException {
        UpbitCoinCode code = UpbitCoinCode.KRW_ADA;
        String index = "upbit_candle_krw_ada";

        for (int i = 0; i < 100; i++) {
            UpbitCandleDoc doc = UpbitCandleDoc.builder()
                    .candleAccTradePrice(10.0)
                    .timestamp(0L + i)
                    .market(code.toString())
                    .candleDateTimeUtc(new Date())
                    .candleDateTimeKst(new Date())
                    .openingPrice(10.0)
                    .highPrice(10.0)
                    .lowPrice(10.0)
                    .tradePrice(10.0)
                    .build();
            String id = "" + i;
            UpbitCandleDoc indexed = candleRepository.index(index, doc);
            System.out.println(indexed);
        }

        Pageable pageable = PageRequest.of(0, 3);
        Page<UpbitCandleDoc> res = candleRepository.findAll(index, pageable);

        for (UpbitCandleDoc doc : res.getContent()) {

            assertNotNull(doc.getTimestamp());
            assertNotNull(doc.getMarket());
            assertNotNull(doc.getCandleAccTradePrice());
            assertNotNull(doc.getCandleAccTradeVolume());
            assertNotNull(doc.getHighPrice());
            assertNotNull(doc.getLowPrice());
            assertNotNull(doc.getCandleDateTimeKst());
            assertNotNull(doc.getCandleDateTimeUtc());

        }

    }
}
