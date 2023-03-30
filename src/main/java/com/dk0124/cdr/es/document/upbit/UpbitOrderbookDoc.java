package com.dk0124.cdr.es.document.upbit;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UpbitOrderbookDoc {
    @NotNull
    @JsonProperty("market")
    @JsonAlias("code")
    private String code;

    @NotNull
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("total_ask_size")
    private Double totalAskSize;

    @JsonProperty("total_bid_size")
    private Double totalBidSize;

    @JsonProperty("orderbook_units")
    private List<UpbitOrderbookUnit> orderBookUnits;
}
