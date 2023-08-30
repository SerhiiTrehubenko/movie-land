package com.tsa.movieland.currency;

import com.tsa.movieland.dao.DaoBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyExchangeServiceITest extends DaoBaseTest {

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