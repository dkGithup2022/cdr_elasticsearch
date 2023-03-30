package com.dk0124.cdr.es.document.bithumb;

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

    private Double openingPrice;

    private Double closingPrice;

    private Double highPrice;

    private Double lowPrice;

    private Double tradeAmount;
}
