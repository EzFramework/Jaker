package com.github.ezframework.jaker;

import com.github.ezframework.jaker.exceptions.LocaleNotSupportedException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JakerBuilderIntegrationTest {

    @Test
    void defaultBuildPathProducesUsableFakerWhenEnUsDataIsPresent() {
        final Faker faker = Jaker.builder().build().faker();

        assertNotNull(faker);

        final String firstName = faker.name().firstName();
        assertThat(firstName).isNotBlank();
    }

    @Test
    void localeStringAndLocaleObjectProduceEquivalentProviderOutput() {
        final Faker fromStringTag = Jaker.builder().locale("en-US").seed(1234L).build().faker();
        final Faker fromLocale = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(1234L).build().faker();

        final String firstFromStringTag = fromStringTag.name().firstName();
        final String firstFromLocale = fromLocale.name().firstName();

        assertThat(firstFromStringTag).isNotBlank();
        assertThat(firstFromLocale).isNotBlank();
    }

    @Test
    void unsupportedLocaleThrowsAndMentionsFailingLanguageTag() {
        final LocaleNotSupportedException ex = assertThrows(
            LocaleNotSupportedException.class,
            () -> Jaker.builder().locale("zz-ZZ").build()
        );

        assertThat(ex.getMessage()).contains("zz-ZZ");
    }

    @Test
    void sameLocaleAndSeedProduceDeterministicFields() {
        final Jaker a = Jaker.builder().locale("en-US").seed(4242L).build();
        final Jaker b = Jaker.builder().locale("en-US").seed(4242L).build();

        final String firstNameA = a.faker().name().firstName();
        final String firstNameB = b.faker().name().firstName();
        final String zipA = a.faker().address().zipCode();
        final String zipB = b.faker().address().zipCode();

        assertThat(firstNameA).isEqualTo(firstNameB);
        assertThat(zipA).isEqualTo(zipB);
    }
}
