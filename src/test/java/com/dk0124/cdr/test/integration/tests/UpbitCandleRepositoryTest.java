package com.dk0124.cdr.test.integration.tests;


import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.es.dao.upbit.UpbitCandleRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.dk0124.cdr.test.integration.ElasticTestContainer;
import com.dk0124.cdr.test.util.EsIndexOps;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpbitCandleRepositoryTest {

    @Container
    private static ElasticTestContainer esContainer = new ElasticTestContainer();


    private static EsIndexOps esIndexOps = new EsIndexOps();

    @BeforeAll
    static void setUp() {
        esContainer.start();
    }

    @Autowired
    private UpbitCandleRepository candleRepository;

    @BeforeEach
    public void re_create_index() throws IOException {
        String sp = "elastic/upbit/candle_setting.json";
        String mp = "elastic/upbit/candle_mapping.json";
        String prefix = "upbit_tick_";

        for (UpbitCoinCode code : UpbitCoinCode.values()) {
            String[] splitted = code.toString().toLowerCase(Locale.ROOT).split("-");
            String idx = prefix + String.join("_", splitted);
            esIndexOps.deleteIndex(idx);
            esIndexOps.forceMergeAll();
            esIndexOps.createIndexWithMappingAndSetting(idx, mp, sp);
            esIndexOps.forceMerge(idx);
        }
    }


    @Test
    @DisplayName(" index : 같은 아이디에 저장")
    public void save_on_same_id() throws InterruptedException {
        UpbitCoinCode code = UpbitCoinCode.KRW_ADA;
        String index = "upbit_candle_krw_btc";

        UpbitCandleDoc doc1 = UpbitCandleDoc.builder().timestamp(1L).build();
        UpbitCandleDoc doc2 = UpbitCandleDoc.builder().timestamp(2L).build();

        candleRepository.index(index, "same", doc1);
        candleRepository.index(index, "same", doc2);

        esIndexOps.forceMerge(index);
        Thread.sleep(1000);

        Page<UpbitCandleDoc> page = candleRepository.findAll(index, PageRequest.of(0, 100));
        assertEquals(1, page.getContent().size());

    }

    @Test
    @DisplayName(" 저장된 칼럼 확인 ")
    public void check_all_column() throws InterruptedException {
        UpbitCoinCode code = UpbitCoinCode.KRW_ADA;
        String index = "upbit_candle_krw_ada";


        String dateString = "2023-04-17T10:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        for (int i = 0; i < 100; i++) {
            UpbitCandleDoc doc = UpbitCandleDoc.builder()
                    .candleAccTradePrice(10.0)
                    .timestamp(0L + i)
                    .market(code.toString())
                    .candleDateTimeUtc(dateTime)
                    .candleDateTimeKst(dateTime)
                    .candleAccTradeVolume(10.0)
                    .candleAccTradePrice(10.0)
                    .openingPrice(10.0)
                    .highPrice(10.0)
                    .lowPrice(10.0)
                    .tradePrice(10.0)
                    .build();
            String id = "" + i;
            UpbitCandleDoc indexed = candleRepository.index(index, doc);
            System.out.println(indexed);
        }

        Thread.sleep(1000);

        Pageable pageable = PageRequest.of(0, 3);
        Page<UpbitCandleDoc> res = candleRepository.findAll(index, pageable);

        for (UpbitCandleDoc doc : res.getContent()) {
            System.out.println(doc.toString());
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
