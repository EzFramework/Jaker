package com.github.ezframework.jaker.data;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DataLoaderIntegrationTest {

    @Test
    void loadUsesBinaryPackWhenAvailable() {
        final List<String> phones = new DataLoader().load("en-US", "phones");

        assertThat(phones)
            .isNotNull()
            .isNotEmpty();
        assertThat(phones)
            .allSatisfy(phone -> assertThat(phone).matches("^1\\d{10}$"));
    }

    @Test
    void loadFallsBackToTextWhenBinaryIsUnavailable() {
        final List<String> fruits = new DataLoader().load("en-US", "fruits");

        assertThat(fruits)
            .isNotNull()
            .isNotEmpty();
        assertThat(fruits).allSatisfy(fruit -> {
            assertThat(fruit)
                .isNotBlank()
                .isEqualTo(fruit.trim())
                .doesNotStartWith("#");
        });
    }

    @Test
    void loadReturnsEmptyListForMissingCategory() {
        final List<String> missing = new DataLoader().load("en-US", "does_not_exist");

        assertThat(missing)
            .isNotNull()
            .isEmpty();
    }

    @Test
    void loadReusesCachedListForSameLocaleAndCategory() {
        final DataLoader loader = new DataLoader();

        final List<String> first = loader.load("en-US", "names");
        final List<String> second = loader.load("en-US", "names");

        assertThat(second).isSameAs(first);
    }

    @Test
    void loadReusesCachedListAcrossLoaderInstances() {
        final DataLoader firstLoader = new DataLoader();
        final DataLoader secondLoader = new DataLoader();

        final List<String> first = firstLoader.load("en-US", "names");
        final List<String> second = secondLoader.load("en-US", "names");

        assertThat(second).isSameAs(first);
    }

}
