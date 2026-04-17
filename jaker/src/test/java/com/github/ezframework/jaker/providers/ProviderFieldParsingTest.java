package com.github.ezframework.jaker.providers;

import com.github.ezframework.jaker.data.DataLoader;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderFieldParsingTest {

    private static final class FirstItemRandom extends Random {
        @Override
        public int nextInt(final int bound) {
            return 0;
        }
    }

    private static final Random FIRST_ITEM_RANDOM = new FirstItemRandom();

    @Test
    void malformedRowsPreserveFallbackBehavior() {
        final DataLoader loader = new DataLoader();
        final Locale locale = Locale.forLanguageTag("x-malformed");

        final AnimalProvider animalProvider = new AnimalProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Fox", animalProvider.name());
        assertEquals("Mammal", animalProvider.category());
        assertEquals("unknown", animalProvider.gender());

        final CountryProvider countryProvider = new CountryProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Wonderland", countryProvider.name());
        assertEquals("US", countryProvider.code());

        final FileProvider fileProvider = new FileProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("report", fileProvider.name());
        assertEquals("txt", fileProvider.extension());
        assertEquals(1024L, fileProvider.size());

        final GameServerProvider gameServerProvider = new GameServerProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Minecraft", gameServerProvider.game());
        assertEquals("203.0.113.1", gameServerProvider.ip());
        assertEquals("1.0.0", gameServerProvider.version());
    }

    @Test
    void missingDelimitersUseFallbackForUnavailableFieldsOnly() {
        final DataLoader loader = new DataLoader();
        final Locale locale = Locale.forLanguageTag("x-missing-delimiter");

        final AnimalProvider animalProvider = new AnimalProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Wolf", animalProvider.name());
        assertEquals("Mammal", animalProvider.category());
        assertEquals("unknown", animalProvider.gender());

        final CountryProvider countryProvider = new CountryProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("USA", countryProvider.name());
        assertEquals("US", countryProvider.code());

        final FileProvider fileProvider = new FileProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("archive", fileProvider.name());
        assertEquals("txt", fileProvider.extension());
        assertEquals(1024L, fileProvider.size());

        final GameServerProvider gameServerProvider = new GameServerProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Minecraft", gameServerProvider.game());
        assertEquals("203.0.113.1", gameServerProvider.ip());
        assertEquals("1.0.0", gameServerProvider.version());
    }

    @Test
    void extraDelimitersDoNotAffectIndexedFields() {
        final DataLoader loader = new DataLoader();
        final Locale locale = Locale.forLanguageTag("x-extra-delimiter");

        final AnimalProvider animalProvider = new AnimalProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Otter", animalProvider.name());
        assertEquals("Mammal", animalProvider.category());
        assertEquals("neutral", animalProvider.gender());

        final CountryProvider countryProvider = new CountryProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Canada", countryProvider.name());
        assertEquals("CA", countryProvider.code());

        final FileProvider fileProvider = new FileProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("backup", fileProvider.name());
        assertEquals("tar.gz", fileProvider.extension());
        assertEquals(2048L, fileProvider.size());

        final GameServerProvider gameServerProvider = new GameServerProvider(loader, locale, FIRST_ITEM_RANDOM);
        assertEquals("Valheim", gameServerProvider.game());
        assertEquals("198.51.100.10", gameServerProvider.ip());
        assertEquals("0.218.15", gameServerProvider.version());
    }
}
