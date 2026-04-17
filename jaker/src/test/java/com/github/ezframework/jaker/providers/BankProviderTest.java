package com.github.ezframework.jaker.providers;

import com.github.ezframework.jaker.data.DataLoader;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BankProviderTest {

    @Test
    void ibanReturnsSafeExampleFromDatasetWhenPresent() {
        final BankProvider p = new BankProvider(new DataLoader(), Locale.forLanguageTag("nl"), new Random(42));
        final String iban = p.iban();
        assertNotNull(iban);
        assertFalse(iban.isBlank());
        // safe dataset entries start with country code + checksum
        assertTrue(iban.matches("^[A-Z]{2}\\d{2}.+"));
    }

    @Test
    void ibanFallsBackWhenDatasetMissing() {
        final BankProvider p = new BankProvider(new DataLoader(), Locale.forLanguageTag("xx-XX"), new Random(1));
        final String iban = p.iban();
        assertNotNull(iban);
        assertFalse(iban.isBlank());
        assertTrue(iban.matches("^[A-Z]{2}\\d{2}.+"));
    }

    @Test
    void unsafeIbanProducesSyntheticPattern() {
        final BankProvider p = new BankProvider(new DataLoader(), Locale.forLanguageTag("en-US"), new Random(42));
        final String unsafe = p.unsafe().iban();
        assertNotNull(unsafe);
        // Unsafe generation produces a DE-based synthetic IBAN in current implementation
        assertTrue(unsafe.matches("^DE\\d{2}000000000000\\d{2}$"));
    }

    @Test
    void formatReturnsBankFormatTemplate() {
        final BankProvider p = new BankProvider(new DataLoader(), Locale.forLanguageTag("en-US"), new Random(42));
        final String fmt = p.format();
        assertNotNull(fmt);
        assertFalse(fmt.isBlank());
        // basic expectation: contains country/checksum placeholder 'C' or 'k' or 'B'
        assertTrue(fmt.contains("C") || fmt.contains("k") || fmt.contains("B") || fmt.contains("X"));
    }
}
