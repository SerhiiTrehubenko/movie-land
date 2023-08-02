package com.tsa.movieland.currency;

import com.tsa.movieland.context.CurrencyExchange;
import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@CurrencyExchange
public class NbuExchangeFetcher {

    @SneakyThrows
    public String getCurrencyExchange(String url) {
        URLConnection connection = new URL(url).openConnection();
        return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
