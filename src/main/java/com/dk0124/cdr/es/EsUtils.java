package com.dk0124.cdr.es;

import java.util.Locale;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.es.document.bithumb.BithumbCandleDoc;
import com.dk0124.cdr.es.document.bithumb.BithumbOrderbookDoc;
import com.dk0124.cdr.es.document.bithumb.BithumbTickDoc;
import com.dk0124.cdr.es.document.upbit.UpbitCandleDoc;
import com.dk0124.cdr.es.document.upbit.UpbitOrderbookDoc;
import com.dk0124.cdr.es.document.upbit.UpbitTickDoc;

public class EsUtils {

	private static final String UPBIT_TICK_INDEX_PREFIX = "upbit_tick";
	private static final String BITHUMB_TICK_INDEX_PREFIX = "bithumb_tick";
	private static final String UPBIT_CANDLE_INDEX_PREFIX = "upbit_candle";
	private static final String BITHUMB_CANDLE_INDEX_PREFIX = "bithumb_candle";
	private static final String UPBIT_ORDERBOOK_INDEX_PREFIX = "upbit_orderbook";
	private static final String BITHUMB_ORDERBOOK_INDEX_PREFIX = "bithumb_orderbook";


	/**
	 * Upbit
	 */
	// 틱 인덱스 생성
	public static String generateIndex(UpbitTickDoc tickDoc) {
		return upbitTickIndexFromCode(tickDoc.getCode());
	}

	// 틱 ID 생성
	public static String generateId(UpbitTickDoc tickDoc) {
		String code = tickDoc.getCode().toLowerCase(Locale.ROOT);
		long timestamp = tickDoc.getTimestamp();
		return code + "_" + timestamp;
	}

	// 캔들 인덱스 생성
	public static String generateIndex(UpbitCandleDoc candleDoc) {
		return upbitCandleIndexFromCode(candleDoc.getMarket());
	}

	// 캔들 ID 생성
	public static String generateId(UpbitCandleDoc candleDoc) {
		String market = candleDoc.getMarket().toLowerCase(Locale.ROOT);
		long timestamp = candleDoc.getTimestamp();
		return market + "_" + timestamp;
	}

	// 오더북 인덱스 생성
	public static String generateIndex(UpbitOrderbookDoc orderbookDoc) {
		return upbitOrderbookIndexFromCode(orderbookDoc.getCode());
	}

	// 오더북 ID 생성
	public static String generateId(UpbitOrderbookDoc orderbookDoc) {
		String code = orderbookDoc.getCode().toLowerCase(Locale.ROOT);
		long timestamp = orderbookDoc.getTimestamp();
		return code + "_" + timestamp;
	}

	/**
	 * Bithumb
	 */

	// 빗섬 캔들 아이디
	public static String generateId(BithumbCandleDoc doc) {
		return doc.getCode() + "_" + doc.getTimestamp();
	}

	// 빗섬 캔들 인덱스
	public static String generateIndex(BithumbCandleDoc doc) {
		return bithumbCandleIndexFromCode(doc.getCode());
	}

	// 빗섬 오더북 아이디
	public static String generateId(BithumbOrderbookDoc doc) {
		return doc.getCode() + "_" + doc.getDatetime();
	}

	// 빗섬 오더북 인덱스
	public static String generateIndex(BithumbOrderbookDoc doc) {
		return bithumbOrderbookIndexFromCode(doc.getCode());
	}

	// 빗섬 틱 아이디
	public static String generateId(BithumbTickDoc doc) {
		return doc.getCode() + "_" + doc.getTimestamp();
	}

	// 빗섬 틱 인덱스
	public static String generateIndex(BithumbTickDoc doc) {
		return bithumbTickIndexFromCode(doc.getCode());
	}

	/**
	 * get Index of each document type;
	 * */

	public static String bithumbOrderbookIndexFromCode(String code) {
		if (BithumbCoinCode.fromString(code) == null)
			throw new RuntimeException("INVALID CODE");
		String[] splitted = code.toString().toLowerCase(Locale.ROOT).split("-");
		return BITHUMB_ORDERBOOK_INDEX_PREFIX + "_" + String.join("_", splitted);
	}

	public static String bithumbCandleIndexFromCode(String code) {
		if (BithumbCoinCode.fromString(code) == null)
			throw new RuntimeException("INVALID CODE");
		String[] splitted = code.toString().toLowerCase(Locale.ROOT).split("-");
		return BITHUMB_CANDLE_INDEX_PREFIX + "_" + String.join("_", splitted);
	}

	public static String bithumbTickIndexFromCode(String code) {
		if (BithumbCoinCode.fromString(code) == null)
			throw new RuntimeException("INVALID CODE");
		String[] splitted = code.toString().toLowerCase(Locale.ROOT).split("-");
		return BITHUMB_TICK_INDEX_PREFIX + "_" + String.join("_", splitted);
	}

	public static String upbitOrderbookIndexFromCode(String code) {
		if (UpbitCoinCode.fromString(code.toUpperCase(Locale.ROOT)) == null)
			throw new IllegalArgumentException("Invalid market: " + code);

		String[] splitted = code.toLowerCase(Locale.ROOT).split("-");
		return UPBIT_ORDERBOOK_INDEX_PREFIX + "_" + String.join("_", splitted);
	}

	public static String upbitCandleIndexFromCode(String code) {
		if (UpbitCoinCode.fromString(code.toUpperCase(Locale.ROOT)) == null)
			throw new IllegalArgumentException("Invalid market: " + code);

		String[] splitted = code.toLowerCase(Locale.ROOT).split("-");
		return UPBIT_CANDLE_INDEX_PREFIX + "_" + String.join("_", splitted);
	}

	public static String upbitTickIndexFromCode(String code) {
		if (UpbitCoinCode.fromString(code.toUpperCase(Locale.ROOT)) == null)
			throw new IllegalArgumentException("Invalid market: " + code);

		String[] splitted = code.toLowerCase(Locale.ROOT).split("-");
		return UPBIT_TICK_INDEX_PREFIX + "_" + String.join("_", splitted);
	}
}
