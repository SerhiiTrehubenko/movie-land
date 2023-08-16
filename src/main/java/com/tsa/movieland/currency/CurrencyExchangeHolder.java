package com.tsa.movieland.currency;

import com.tsa.movieland.context.CurrencyExchange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@CurrencyExchange
public class CurrencyExchangeHolder {
    private volatile Map<Integer, ExchangeNode> ratings;

    public void setNodesMap(ConcurrentMap<Integer, ExchangeNode> nodesMap) {
        ratings = nodesMap;
    }

    public double excange (CurrencyType to, double amount) {
        double divider = getDivider(to);

        BigDecimal bigDecimal = new BigDecimal(amount / divider);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.CEILING);
        return bigDecimal.doubleValue();
    }

    private double getDivider(CurrencyType to) {
        final int code = to.getCode();
        ExchangeNode node = ratings.get(code);
        return node.getRate();
    }
}
