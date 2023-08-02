package com.tsa.movieland.currency;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {com.tsa.movieland.config.CurrencyConfigTest.class})
@ActiveProfiles("test")
class CurrencyExchangeExtractorTest {

    @Autowired
    private CurrencyExchangeHolder currencyExchangeHolder;

    @Test
    void shouldReturnCurrencyRates() {

        Double usd = currencyExchangeHolder.getRating(CurrencyType.USD);
        Double eur = currencyExchangeHolder.getRating(CurrencyType.EUR);

        assertNotNull(usd);
        assertEquals(36.5686, usd);
        assertNotNull(eur);
        assertEquals(40.3242, eur);
    }
}