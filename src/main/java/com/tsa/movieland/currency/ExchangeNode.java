package com.tsa.movieland.currency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ExchangeNode {
    private int r030;
    private String txt;
    private double rate;
    private String cc;
    private String exchangedate;
}
