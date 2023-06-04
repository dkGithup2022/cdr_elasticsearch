package com.dk0124.cdr.es.document.bithumb;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BithumbOrderbookUnit {
    @JsonProperty("code")
    @JsonAlias("symbol")
    private String code;

    @JsonAlias("buySellGb")
    @JsonProperty("buy_sell_gb")
    private int buySellGb;

    @JsonProperty("cont_price")
    @JsonAlias("contPrice")
    private Double contPrice;

    @JsonProperty("cont_qty")
    @JsonAlias("contQty")
    private Double contQty;

    @JsonProperty("cont_amt")
    @JsonAlias("contAmt")
    private Double contAmt;

    @JsonProperty ("cont_dtm")
    @JsonAlias("contDtm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime contDtm;

    @JsonProperty ("updn")
    private String dpdn;

    // 생성된 값 -> 저장 시점에 생성 필요.
    private Long timestamp;
}
