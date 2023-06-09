package com.dk0124.cdr.es.document.upbit;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UpbitTickDoc {

    @NotNull
    @JsonProperty("sid")
    @JsonAlias("sequential_id")
    private Long sequentialId;

    @JsonProperty("cd")
    @JsonAlias({"code","market"})
    private String code;

    @JsonProperty("ty")
    @JsonAlias("type")
    private String type;

    @JsonProperty("tp")
    @JsonAlias("trade_price")
    private Double tradePrice;

    @JsonProperty("tv")
    @JsonAlias("trade_volume")
    private Double tradeVolume;

    @JsonProperty("ab")
    @JsonAlias("ask_bid")
    private String askBid;

    @JsonProperty("pcp")
    @JsonAlias("prev_closing_price")
    private Double prevClosingPrice;

    @JsonProperty("c")
    @JsonAlias("change")
    private String change;

    @JsonProperty("cp")
    @JsonAlias("change_price")
    private Double changePrice;

    @JsonProperty("td")
    @JsonAlias({"trade_date","trade_date_utc"})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate tradeDateUtc;

    @JsonProperty("ttm")
    @JsonAlias({"trade_time","trade_time_utc"})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.hour_minute_second)
    private LocalTime tradeTimeUtc;

    @JsonProperty("ttms")
    @JsonAlias("trade_timestamp")
    private Long tradeTimestamp;

    @JsonProperty("tms")
    @JsonAlias("timestamp")
    private Long timestamp;

    @JsonProperty("st")
    @JsonAlias("stream_type")
    private String streamType;

}
