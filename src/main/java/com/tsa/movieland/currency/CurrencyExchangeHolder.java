package com.tsa.movieland.currency;

import com.tsa.movieland.context.CurrencyExchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CurrencyExchange
public class CurrencyExchangeHolder {
    private final Map<CurrencyType, Double> ratings = new ConcurrentHashMap<>();

    public void setRatings(CurrencyType currency, Double rate) {
        ratings.put(currency, rate);
    }

    public Double getRating(CurrencyType currency) {
        return ratings.get(currency);
    }
}
