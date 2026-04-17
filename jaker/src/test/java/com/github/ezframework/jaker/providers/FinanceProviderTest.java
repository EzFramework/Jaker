package com.github.ezframework.jaker.providers;

import com.github.ezframework.jaker.data.DataLoader;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FinanceProviderTest {

    @Test
    void creditCardNumberReturnsDatasetOrFallback() {
        final FinanceProvider p = new FinanceProvider(new DataLoader(), Locale.forLanguageTag("en-US"), new Random(42));
        final String cc = p.creditCardNumber();
        assertNotNull(cc);
        assertFalse(cc.isBlank());
        // dataset entries contain digits and dashes
        assertTrue(cc.matches("^[0-9-]+$") || cc.matches("^[0-9]+$"));
    }

    @Test
    void unsafeCreditCardIsLuhnValid() {
        final FinanceProvider.UnsafeFinanceProvider unsafe = new FinanceProvider.UnsafeFinanceProvider(new Random(42));
        final String cc = unsafe.creditCardNumber();
        assertNotNull(cc);
        assertFalse(cc.isBlank());

        final String digits = cc.replaceAll("[^0-9]", "");
        assertTrue(digits.length() >= 13 && digits.length() <= 19);
        assertTrue(isLuhnValid(digits));
    }

    private boolean isLuhnValid(final String digits) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int d = digits.charAt(i) - '0';
            if (doubleDigit) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }
}
