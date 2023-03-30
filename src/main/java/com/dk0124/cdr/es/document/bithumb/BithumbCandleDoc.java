package com.dk0124.cdr.es.document.bithumb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BithumbCandleDoc {
    @NotEmpty
    private String code;

    @NotNull
    public Long timestamp;

    @JsonProperty("opening_price")
    private Double openingPrice;

    @JsonProperty("closing_price")
    private Double closingPrice;

    @JsonProperty("high_price")
    private Double highPrice;

    @JsonProperty("low_price")
    private Double lowPrice;

    @JsonProperty("trade_amount")
    private Double tradeAmount;
}
