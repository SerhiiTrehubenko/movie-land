package com.tsa.movieland.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsa.movieland.context.CurrencyExchange;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
@CurrencyExchange
public class CurrencyExchangeExtractor implements InitializingBean {
    private final static String FIELD_CURRENCY_TYPE = "r030";
    private final static String FIELD_RATE_EXCHANGE = "rate";
    private final NbuExchangeFetcher fetcher;
    private final CurrencyExchangeHolder holder;
    private String jsonFromNbu;
    @Value("${currency.url}")
    private String url;

    @Scheduled(cron = "${currency.refresh-cron}")
    public void afterPropertiesSet() {
        jsonFromNbu = fetcher.getCurrencyExchange(url);
        extractCurrencyRates();
        log.info("Currency exchange refreshed");
    }

    @SneakyThrows
    private void extractCurrencyRates() {
        final JsonNode jsonNode = new ObjectMapper().readTree(jsonFromNbu);
        jsonNode.forEach(
                node -> {
                    setCurrency(node, CurrencyType.EUR);
                    setCurrency(node, CurrencyType.USD);
                }
        );
    }

    private void setCurrency(JsonNode node, CurrencyType currency) {
        if (node.findValue(FIELD_CURRENCY_TYPE).asInt() == currency.getCode()) {
            double rate = node.findValue(FIELD_RATE_EXCHANGE).asDouble();
            holder.setRatings(currency, rate);
        }
    }
}
