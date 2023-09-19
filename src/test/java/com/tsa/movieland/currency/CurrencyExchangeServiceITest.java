package com.tsa.movieland.currency;

import com.tsa.movieland.CommonContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrencyExchangeExtractorTest extends CommonContainer {

    @Autowired
    private CurrencyExchangeService currencyExchangeHolder;

    @Test
    void shouldReturnCurrencyRates() {
        double price = 200.00;

        double usdPrice = currencyExchangeHolder.excange(CurrencyType.USD, price);
        double eurPrice = currencyExchangeHolder.excange(CurrencyType.EUR, price);

        assertTrue(usdPrice < price);
        assertTrue(eurPrice < price);
    }
}