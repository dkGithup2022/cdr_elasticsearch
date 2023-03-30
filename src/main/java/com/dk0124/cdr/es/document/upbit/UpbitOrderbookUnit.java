package com.dk0124.cdr.es.document.upbit;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UpbitOrderbookUnit {
    @JsonProperty("ask_price")
    private Double askPrice;
    @JsonProperty("bid_price")
    private Double bidPrice;

    @JsonProperty("ask_size")
    private Double askSize;
    @JsonProperty("bid_size")
    private Double bidSize;
}
