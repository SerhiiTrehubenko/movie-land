package com.tsa.movieland.config;

import com.tsa.movieland.currency.CurrencyExchangeExtractor;
import com.tsa.movieland.currency.CurrencyExchangeHolder;
import com.tsa.movieland.currency.NbuExchangeFetcher;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Profile("test")
@Configuration
public class CurrencyConfigTest {
    @Bean
    @Primary
    public NbuExchangeFetcher nbuExchangeFetcherTest() {
        NbuExchangeFetcher mock = Mockito.mock(NbuExchangeFetcher.class);
        when(mock.getCurrencyExchange(anyString())).thenReturn(getContent("currency/exchange.json"));
        return mock;
    }

    @Bean
    @Primary
    public CurrencyExchangeHolder currencyExchangeHolderTest() {
        return new CurrencyExchangeHolder();
    }

    @Bean
    @Primary
    public CurrencyExchangeExtractor currencyExchangeExtractorTest() {
        return new CurrencyExchangeExtractor(nbuExchangeFetcherTest(), currencyExchangeHolderTest());
    }

    private String getContent(String filePath) {
        try (InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {
            Objects.requireNonNull(file);
            return new String(file.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("File with path: [%s] was not found".formatted(filePath), e);
        }
    }
}
