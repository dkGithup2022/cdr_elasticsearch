package com.dk0124.cdr.es.document.upbit;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UpbitCandleDoc {

    @NotNull
    @JsonProperty("timestamp")
    private Long timestamp;

    @NotNull
    @JsonAlias({"code","market","cd"})
    @JsonProperty("market")
    private String market;

    @JsonAlias("candle_date_time_utc")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime candleDateTimeUtc;


    @JsonAlias("candle_date_time_kst")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime candleDateTimeKst;

    @JsonAlias("opening_price")
    private Double openingPrice;

    @JsonAlias("high_price")
    private Double highPrice;

    @JsonAlias("low_price")
    private Double lowPrice;

    @JsonAlias("trade_price")
    private Double tradePrice;

    @JsonAlias("candle_acc_trade_price")
    @JsonProperty("candleAccTradePrice")
    private Double candleAccTradePrice;

    @JsonAlias("candle_acc_trade_volume")
    private Double candleAccTradeVolume;

}
