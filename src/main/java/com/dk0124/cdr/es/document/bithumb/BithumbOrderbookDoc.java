package com.dk0124.cdr.es.document.bithumb;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BithumbOrderbookDoc {

    // TODO : 이거  원본 따를지, timestamp 로 통일할지 결정 필요.
    @JsonProperty("datetime")
    private Long datetime;

    @JsonProperty("code")
    @JsonAlias("symbol")
    private String code;

    @JsonAlias("orderbookUnit")
    @JsonProperty("orderbook_units")
    private List<BithumbOrderbookUnit> orderbookUnits;
}
