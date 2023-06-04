package com.dk0124.cdr.test.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.util.Pair;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.es.EsUtils;
import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import com.dk0124.cdr.es.document.bithumb.BithumbOrderbookDoc;
import com.dk0124.cdr.es.document.bithumb.BithumbTickDoc;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.dk0124.cdr.es.document.upbit.UpbitOrderbookDoc;
import com.dk0124.cdr.es.document.upbit.UpbitTickDoc;

public class EsUtilsTest {

	@ParameterizedTest()
	@MethodSource("upbit_candle_index_pairs")
	public void testGenerateIndex_UpbitCandleDoc(Pair<UpbitCoinCode, String> pair) {
		//G
		UpbitCoinCode code = (UpbitCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		UpbitCandleDoc sampleDoc = new UpbitCandleDoc();
		sampleDoc.setMarket(code.toString());

		//W
		String indexFromString = EsUtils.upbitCandleIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> upbit_candle_index_pairs() {
		String UPBIT_CANDLE_PREFIX = "upbit_candle";

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", UPBIT_CANDLE_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("upbit_orderbook_index_pairs")
	public void testGenerateIndex_UpbitOrderbookDoc(Pair<UpbitCoinCode, String> pair) {
		//G
		UpbitCoinCode code = (UpbitCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		UpbitOrderbookDoc sampleDoc = new UpbitOrderbookDoc();
		sampleDoc.setCode(code.toString());

		//W
		String indexFromString = EsUtils.upbitOrderbookIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> upbit_orderbook_index_pairs() {
		String UPBIT_ORDERBOOK_PREFIX = "upbit_orderbook";

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", UPBIT_ORDERBOOK_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("upbit_tick_index_pairs")
	public void testGenerateIndex_UpbitTickDoc(Pair<UpbitCoinCode, String> pair) {
		//G
		UpbitCoinCode code = (UpbitCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		UpbitTickDoc sampleDoc = new UpbitTickDoc();
		sampleDoc.setCode(code.toString());

		//W
		String indexFromString = EsUtils.upbitTickIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> upbit_tick_index_pairs() {
		String UPBIT_TICK_PREFIX = "upbit_tick";

		return Arrays.stream(UpbitCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", UPBIT_TICK_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("bithumb_candle_index_pairs")
	public void testGenerateIndex_BithumbCandleDoc(Pair<BithumbCoinCode, String> pair) {
		//G
		BithumbCoinCode code = (BithumbCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		BithumbCandleDoc sampleDoc = new BithumbCandleDoc();
		sampleDoc.setCode(code.toString());

		//W
		String indexFromString = EsUtils.bithumbCandleIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> bithumb_candle_index_pairs() {
		String BITHUMB_CANDLE_PREFIX = "bithumb_candle";

		return Arrays.stream(BithumbCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", BITHUMB_CANDLE_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}

	@ParameterizedTest()
	@MethodSource("bithumb_orderbook_index_pairs")
	public void testGenerateIndex_BithumbOrderbookDoc(Pair<BithumbCoinCode, String> pair) {
		//G
		BithumbCoinCode code = (BithumbCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		BithumbOrderbookDoc sampleDoc = new BithumbOrderbookDoc();
		sampleDoc.setCode(code.toString());

		//W
		String indexFromString = EsUtils.bithumbOrderbookIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> bithumb_orderbook_index_pairs() {
		String BITHUMB_ORDERBOOK_PREFIX = "bithumb_orderbook";

		return Arrays.stream(BithumbCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", BITHUMB_ORDERBOOK_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}


	@ParameterizedTest()
	@MethodSource("bithumb_tick_index_pairs")
	public void testGenerateIndex_BithumbTickDoc(Pair<BithumbCoinCode, String> pair) {
		//G
		BithumbCoinCode code = (BithumbCoinCode)pair.getFirst();
		String expectedIndex = (String)pair.getSecond();

		BithumbTickDoc sampleDoc = new BithumbTickDoc();
		sampleDoc.setCode(code.toString());

		//W
		String indexFromString = EsUtils.bithumbTickIndexFromCode(code.toString());
		String indexFromDoc = EsUtils.generateIndex(sampleDoc);

		//T
		assertEquals(expectedIndex, indexFromString);
		assertEquals(expectedIndex, indexFromDoc);
	}

	static Stream<Arguments> bithumb_tick_index_pairs() {
		String BITHUMB_TICK_PREFIX = "bithumb_tick";

		return Arrays.stream(BithumbCoinCode.values())
			.map(code -> Pair.of(
				code,
				String.join("_", BITHUMB_TICK_PREFIX, code.toString().toLowerCase(Locale.ROOT).replace("-", "_"))))
			.map(Arguments::of);
	}


}
