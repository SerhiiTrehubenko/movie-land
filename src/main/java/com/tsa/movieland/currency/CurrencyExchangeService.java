package com.tsa.movieland.currency;

import com.tsa.movieland.context.CurrencyExchange;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CurrencyExchange
@Slf4j
public class CurrencyExchangeService {
    private final static String DATE_FORMAT = "yyyyMMdd";
    private final static RestTemplate REST_TEMPLATE = new RestTemplate();
    @Value("${currency.url}")
    private String url;
    private volatile Map<Integer, ExchangeRateHolder> exchangeRates;
    @Scheduled(cron = "${currency.refresh-cron}")
    @PostConstruct
    @Retryable(retryFor = Exception.class)
    private void init() {
        extractCurrencyRates();
        log.info("Currency exchange refreshed");
    }

    @SneakyThrows
    private void extractCurrencyRates() {
        String urlWithDate = getUrlWithDate();
        ExchangeRateHolder[] exchangeNodes = REST_TEMPLATE.getForObject(urlWithDate, ExchangeRateHolder[].class);

        Objects.requireNonNull(exchangeNodes);

        exchangeRates = Arrays.stream(exchangeNodes)
                .collect(Collectors.toConcurrentMap(ExchangeRateHolder::getR030, entry -> entry));
    }

    private String getUrlWithDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = dateFormat.format(new Date());
        return url.replaceFirst(DATE_FORMAT, formattedDate);
    }

    public double excange (CurrencyType to, double amount) {
        double divider = getRate(to);

        return calculate(amount, divider);
    }

    private double calculate(double amount, double divider) {
        BigDecimal bigDecimal = new BigDecimal(amount / divider);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private double getRate(CurrencyType to) {
        final int code = to.getCode();
        ExchangeRateHolder node = exchangeRates.get(code);
        return node.getRate();
    }
}
