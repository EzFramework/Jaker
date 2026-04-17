package com.github.ezframework.jaker.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

public final class CompanyProvider {
    /** Random instance. */
    private final Random random;

    /** Cached company names dataset for configured locale. */
    private final List<String> companyNames;

    public CompanyProvider(final DataLoader loader, final Locale locale, final Random random) {
        final String localeTag = locale.toLanguageTag();
        this.random = random;
        this.companyNames = loader.load(localeTag, "company_names");
    }

    /**
     * Return a random company name for the configured locale.
     *
     * @return a random company name or fallback.
     */
    public String companyName() {
        if (companyNames.isEmpty()) {
            return "ACME Corp";
        }
        return companyNames.get(random.nextInt(companyNames.size()));
    }

    /**
     * Return multiple random company names for the configured locale.
     *
     * @param amount number of company names to generate.
     * @return list with {@code amount} random company names.
     */
    public List<String> companyNames(final int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }
        final List<String> companies = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            companies.add(companyName());
        }
        return Collections.unmodifiableList(companies);
    }
}
