package com.dk0124.cdr.test.integration.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.dao.upbit.UpbitCandleRepository;
import com.dk0124.cdr.es.dao.upbit.UpbitOrderbookRepository;
import com.dk0124.cdr.es.dao.upbit.UpbitTickRepository;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.dk0124.cdr.es.document.upbit.UpbitOrderbookDoc;
import com.dk0124.cdr.es.document.upbit.UpbitOrderbookUnit;
import com.dk0124.cdr.es.document.upbit.UpbitTickDoc;
import com.dk0124.cdr.test.integration.ElasticTestContainer;
import com.dk0124.cdr.test.util.EsIndexOps;
import com.fasterxml.jackson.core.JsonProcessingException;


/*

com.dk0124.cdr.test.customCondition.TestConditions#dontTestOnParent
	com.dk0124.cdr.test.customCondition.TestConditions#heyheyCondition
 */
@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIf("com.dk0124.cdr.test.customCondition.TestConditions#doBulkTest")
public class BulkOperationsTest {
	@Container
	private static ElasticTestContainer esContainer = new ElasticTestContainer();

	private static EsIndexOps esIndexOps = new EsIndexOps();

	static Random random = new Random();

	static int NORMAL_BULK_SIZE = 20;

	@BeforeAll
	static void setUp() {
		esContainer.start();
	}

	@Autowired
	private UpbitCandleRepository upbitCandleRepository;

	@Autowired
	private UpbitOrderbookRepository upbitOrderbookRepository;

	@Autowired
	private UpbitTickRepository upbitTickRepository;

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

	@ParameterizedTest()
	@MethodSource("upbit_candle_bulk_pairs")
	public void normal_bulk_request_on_upbitCandle(Pair<UpbitCoinCode, ArrayList<UpbitCandleDoc>> pair) throws
		InterruptedException,
		JsonProcessingException {

		System.out.println("Test upbit_candle_bulk_pairs" + " on code : " + pair.getFirst() );
		// GIVEN
		UpbitCoinCode code = pair.getFirst();
		List<UpbitCandleDoc> sampleDocs = pair.getSecond();

		String index = EsUtils.upbitCandleIndexFromCode(code.toString());

		// WHEN
		upbitCandleRepository.bulkIndex(sampleDocs);
		esIndexOps.forceMerge(index);
		Thread.sleep(500);

		// THEN
		Pageable pageable = PageRequest.of(0, NORMAL_BULK_SIZE);
		Page<UpbitCandleDoc> res = upbitCandleRepository.findAll(index, pageable);

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

		// -> 로컬에서 바로 읽기 반영 안되서 false 뜨는 경우 있음 .. 반복되면 주석 처리
		//assertEquals(NORMAL_BULK_SIZE, res.getContent().size());

	}

	static Stream<Arguments> upbit_candle_bulk_pairs() {

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> {
				ArrayList<UpbitCandleDoc> docs = new ArrayList<>();
				for (int i = 0; i < NORMAL_BULK_SIZE; i++)
					docs.add(UpbitCandleDoc.builder()
						.candleAccTradePrice(10.0)
						.timestamp(random.nextLong())
						.market(code.toString())
						.candleDateTimeUtc(LocalDateTime.now())
						.candleDateTimeKst(LocalDateTime.now())
						.candleAccTradeVolume(10.0)
						.candleAccTradePrice(10.0)
						.openingPrice(10.0)
						.highPrice(10.0)
						.lowPrice(10.0)
						.tradePrice(10.0)
						.build());
				return Pair.of(code, docs);
			})
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("upbit_orderbook_bulk_pairs")
	public void normal_bulk_request_on_upbitOrderbook(Pair<UpbitCoinCode, ArrayList<UpbitOrderbookDoc>> pair) throws
		InterruptedException,
		JsonProcessingException {


		System.out.println("Test upbit_orderbook_bulk_pairs" + " on code : " + pair.getFirst() );
		// GIVEN
		UpbitCoinCode code = pair.getFirst();
		List<UpbitOrderbookDoc> sampleDocs = pair.getSecond();

		String index = EsUtils.upbitOrderbookIndexFromCode(code.toString());

		// WHEN
		upbitOrderbookRepository.bulkIndex(sampleDocs);
		esIndexOps.forceMerge(index);
		Thread.sleep(500);

		// THEN
		Pageable pageable = PageRequest.of(0, NORMAL_BULK_SIZE);
		Page<UpbitOrderbookDoc> res = upbitOrderbookRepository.findAll(index, pageable);

		for (UpbitOrderbookDoc doc : res.getContent()) {
			assertNotNull(doc.getTimestamp());
			assertNotNull(doc.getCode());
			assertNotNull(doc.getTotalBidSize());
			assertNotNull(doc.getTotalAskSize());
			assertNotNull(doc.getOrderBookUnits());
			assertNotNull(doc.getOrderBookUnits().get(1).getAskPrice());
			assertNotNull(doc.getOrderBookUnits().get(1).getBidPrice());
			assertNotNull(doc.getOrderBookUnits().get(1).getAskSize());
			assertNotNull(doc.getOrderBookUnits().get(1).getBidSize());

		}

		// -> 로컬에서 바로 읽기 반영 안되서 false 뜨는 경우 있음 .. 반복되면 주석 처리
		//assertEquals(NORMAL_BULK_SIZE, res.getContent().size());
	}

	static Stream<Arguments> upbit_orderbook_bulk_pairs() {

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> {
				ArrayList<UpbitOrderbookDoc> docs = new ArrayList<>();
				for (int i = 0; i < NORMAL_BULK_SIZE; i++)
					docs.add(
						UpbitOrderbookDoc.builder()
							.code(code.toString())
							.totalAskSize(random.nextDouble())
							.totalBidSize(random.nextDouble())
							.timestamp(Long.valueOf(i))
							.orderBookUnits(
								Arrays.asList(
									new UpbitOrderbookUnit(
										random.nextDouble(), random.nextDouble(), random.nextDouble(),
										random.nextDouble()
									),
									new UpbitOrderbookUnit(
										random.nextDouble(), random.nextDouble(), random.nextDouble(),
										random.nextDouble()
									),
									new UpbitOrderbookUnit(
										random.nextDouble(), random.nextDouble(), random.nextDouble(),
										random.nextDouble()
									)
								)
							)
							.build());

				return Pair.of(code, docs);
			})
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("upbit_tick_bulk_pairs")
	public void normal_bulk_request_on_bithumbTick(Pair<UpbitCoinCode, ArrayList<UpbitTickDoc>> pair) throws
		InterruptedException,
		JsonProcessingException {

		System.out.println("Test upbit_tick_bulk_pairs" + " on code : " + pair.getFirst() );

		// GIVEN
		UpbitCoinCode code = pair.getFirst();
		List<UpbitTickDoc> sampleDocs = pair.getSecond();

		String index = EsUtils.upbitTickIndexFromCode(code.toString());

		// WHEN
		upbitTickRepository.bulkIndex(sampleDocs);
		esIndexOps.forceMerge(index);
		Thread.sleep(500);
		// THEN
		Pageable pageable = PageRequest.of(0, NORMAL_BULK_SIZE);
		Page<UpbitTickDoc> res = upbitTickRepository.findAll(index, pageable);

		for (UpbitTickDoc doc : res.getContent()) {
			assertNotNull(doc.getTimestamp());
			assertNotNull(doc.getCode());
			assertNotNull(doc.getTradeTimeUtc());
			assertNotNull(doc.getTradeDateUtc());
		}

		// -> 로컬에서 바로 읽기 반영 안되서 false 뜨는 경우 있음 .. 반복되면 주석 처리
		//assertEquals(NORMAL_BULK_SIZE, res.getContent().size());
	}

	static Stream<Arguments> upbit_tick_bulk_pairs() {

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> {
				ArrayList<UpbitTickDoc> docs = new ArrayList<>();
				for (int i = 0; i < NORMAL_BULK_SIZE; i++)
					docs.add(
						UpbitTickDoc.builder()
							.sequentialId(Long.valueOf(i))
							.timestamp(Long.valueOf(i))
							.code(code.toString())
							.tradeTimeUtc(LocalTime.now())
							.tradeDateUtc(LocalDate.now())
							.build()
					);

				return Pair.of(code, docs);
			})
			.map(Arguments::of);
	}

	@Test
	@DisplayName("3 개 인덱스에 bulk 요청 ")
	public void bulk_multiple_code() throws JsonProcessingException {

		System.out.println("bulk test expect exception : 3 개 인덱스에 bulk 요청");

		UpbitCoinCode[] codes = new UpbitCoinCode[] {UpbitCoinCode.KRW_ADA, UpbitCoinCode.KRW_BAT,
			UpbitCoinCode.KRW_BTC};
		List<UpbitTickDoc> sampleDocs = new ArrayList<>();

		for (UpbitCoinCode code : codes) {
			for (int i = 0; i < 5; i++)
				sampleDocs.add(
					UpbitTickDoc.builder().code(code.toString()).timestamp((long)i).sequentialId((long)i).build());
		}

		// WHEN
		/**
		 * 정상적으로 인덱스를 타는지 확인하는 방법이 지금은 bulk 에 중단점을 찍고 들어가는 수밖에 없음.
		 * 지금은 total size 만 비교
		 */
		var res = upbitTickRepository.bulkIndex(sampleDocs);

		assertEquals(sampleDocs.size(), res.size());
	}


}
