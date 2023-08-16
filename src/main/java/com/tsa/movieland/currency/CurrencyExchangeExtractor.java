package com.tsa.movieland.currency;

import com.tsa.movieland.context.CurrencyExchange;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@CurrencyExchange
public class CurrencyExchangeExtractor {
    private final CurrencyExchangeHolder holder;
    @Value("${currency.url}")
    private String url;

    @Scheduled(cron = "${currency.refresh-cron}")
    @PostConstruct
    public void afterPropertiesSet() {
        extractCurrencyRates();
        log.info("Currency exchange refreshed");
    }

    @SneakyThrows
    private void extractCurrencyRates() {
        ExchangeNode[] exchangeNodes = new RestTemplate().getForObject(url, ExchangeNode[].class);

        Objects.requireNonNull(exchangeNodes);

        ConcurrentMap<Integer, ExchangeNode> nodesMap = Arrays.stream(exchangeNodes)
                .collect(Collectors.toConcurrentMap(ExchangeNode::getR030, entry -> entry));

        holder.setNodesMap(nodesMap);
    }
}
