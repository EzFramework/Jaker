package com.github.ezframework.jaker;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class DeterminismIntegrationTest {

    @Test
    void sameSeedAndLocaleProducesIdenticalSequenceAcrossProviders() {
        final Faker first = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(20260416L).build().faker();
        final Faker second = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(20260416L).build().faker();

        final SampleTuple firstTuple = sampleTuple(first);
        final SampleTuple secondTuple = sampleTuple(second);

        assertThat(firstTuple).isEqualTo(secondTuple);
    }

    @Test
    void differentSeedsProduceDifferentTupleForSameLocale() {
        final Faker first = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(20260416L).build().faker();
        final Faker second = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(20260417L).build().faker();

        final SampleTuple firstTuple = sampleTuple(first);
        final SampleTuple secondTuple = sampleTuple(second);

        // Compare several fields together to avoid flaky assertions based on a single accidental collision.
        assertThat(firstTuple).isNotEqualTo(secondTuple);
    }

    private static SampleTuple sampleTuple(final Faker faker) {
        // Intentionally excluded from strict determinism assertions:
        // person().uuid(), person().createdTimestamp(), and person().updatedTimestamp()
        // because they are time/UUID-oriented outputs that should not be treated as stable deterministic fixtures.
        return new SampleTuple(
            faker.name().firstName(),
            faker.person().lastname(),
            faker.address().city(),
            faker.address().zipCode(),
            faker.company().companyName(),
            faker.internet().domain(),
            faker.bank().iban(),
            faker.text().word(),
            faker.number().numberBetween(1, 100)
        );
    }

    private record SampleTuple(
        String firstName,
        String lastName,
        String city,
        String zipCode,
        String companyName,
        String domain,
        String iban,
        String word,
        int numberBetween1And100
    ) {
    }
}
